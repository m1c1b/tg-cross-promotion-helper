package ru.viaznin.tgcrosspromotionhelper.services;


import it.tdlight.common.ResultHandler;
import it.tdlight.common.TelegramClient;
import it.tdlight.jni.TdApi;
import lombok.SneakyThrows;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.viaznin.tgcrosspromotionhelper.configuration.TelegramProperties;
import ru.viaznin.tgcrosspromotionhelper.domain.models.telegram.ChatEvent;
import ru.viaznin.tgcrosspromotionhelper.domain.models.telegram.User;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


/**
 * Service for executing telegram API
 *
 * @author Ilya Viaznin
 */
@Service
public class TelegramApiExecutorService {
    /**
     * Fetched chats
     */
    private static final ConcurrentHashMap<Long, TdApi.Chat> chats = new ConcurrentHashMap<>();

    /**
     * Auth result
     */
    private static volatile String result;

    /**
     * Spin wait flag
     */
    private static volatile boolean next;

    /**
     * Client configuration
     */
    private final TelegramProperties telegramProperties;

    /**
     * Client
     */
    private final TelegramClient client;

    /**
     * Auth query string
     */
    private volatile String queryParam;

    /**
     * Current auth state
     */
    private TdApi.AuthorizationState authorizationState;

    @Autowired
    public TelegramApiExecutorService(TelegramProperties telegramProperties, TelegramClient client) {
        this.telegramProperties = telegramProperties;
        this.client = client;
        client.initialize(new UpdateHandler(), null, null);
        client.execute(new TdApi.SetLogVerbosityLevel(0));
    }

    /**
     * Default update handler
     */
    private class UpdateHandler implements ResultHandler {
        @Override
        public void onResult(TdApi.Object object) {
            switch (object.getConstructor()) {
                case TdApi.UpdateAuthorizationState.CONSTRUCTOR -> onAuthorizationStateUpdated(((TdApi.UpdateAuthorizationState) object).authorizationState);
                case TdApi.UpdateNewChat.CONSTRUCTOR -> {
                    var updateNewChat = (TdApi.UpdateNewChat) object;
                    var chat = updateNewChat.chat;
                    chats.put(chat.id, chat);
                }
            }
        }
    }

    /**
     * Get channels by title substring
     *
     * @param titleSubstring title substring
     *
     * @return id - title pairs
     */
    @SneakyThrows
    public List<ImmutablePair<Long, String>> getChannels(String titleSubstring) {
        var errorMessage = new AtomicReference<String>();

        next = false;

        client.send(new TdApi.GetChats(new TdApi.ChatListMain(), Long.MAX_VALUE, 0, Integer.MAX_VALUE), object -> {
            switch (object.getConstructor()) {
                case TdApi.Error.CONSTRUCTOR -> errorMessage.set("Receive an error for getChannels:" + object);
                case TdApi.Chats.CONSTRUCTOR -> {
                }
                default -> System.err.println("Receive wrong response from TDLib:" + object);
            }
            next = true;
        });

        while (!next)
            Thread.onSpinWait();

        if (errorMessage.get() != null)
            throw new Exception(errorMessage.get());

        return chats.values()
                .stream()
                .filter(c -> c.type.getConstructor() == TdApi.ChatTypeSupergroup.CONSTRUCTOR && ((TdApi.ChatTypeSupergroup) c.type).isChannel)
                .filter(c -> c.title.toLowerCase().contains(titleSubstring.toLowerCase()))
                .sorted(Comparator.comparing(c -> c.title))
                .map(c -> new ImmutablePair<>(c.id, c.title))
                .collect(Collectors.toList());
    }

    /**
     * Get joined events
     *
     * @param channelId Telegram id
     *
     * @return Joined events
     */
    @SuppressWarnings("LoopConditionNotUpdatedInsideLoop")
    public List<TdApi.ChatEvent> getJoinedLogUsers(Long channelId) {
        var filter = new TdApi.ChatEventLogFilters();
        filter.memberJoins = true;

        // You can't get channel event log before fetching this channel
        if (!chats.containsKey(channelId))
            getChannels("");

        while (!chats.containsKey(channelId))
            Thread.onSpinWait();

        next = false;
        List<TdApi.ChatEvent> events = new ArrayList<>();
        client.send(new TdApi.GetChatEventLog(channelId, null, 0, Integer.MAX_VALUE, filter, new int[]{}), object -> {
            if (object.getConstructor() == TdApi.ChatEvents.CONSTRUCTOR)
                events.addAll(Arrays.asList(((TdApi.ChatEvents) object).events));
            next = true;
        });

        while (!next)
            Thread.onSpinWait();

        return events;
    }

    /**
     * Get joined domain events
     *
     * @param channelId Telegram id
     *
     * @return Joined domain events
     */
    public List<ChatEvent> getJoinedDomainEventsByChannelId(Long channelId) {
        var domainEvents = new ConcurrentHashMap<Date, ChatEvent>();
        var events = getJoinedLogUsers(channelId);

        var counter = new AtomicInteger(events.size());
        events.forEach(e ->
                client.send(new TdApi.GetUser(e.userId), object -> {
                    if (object.getConstructor() == TdApi.User.CONSTRUCTOR) {
                        var tgUser = (TdApi.User) object;

                        var user = new User(tgUser.username, tgUser.firstName, tgUser.id);
                        var eventDate = new Date(e.date * 1000L);

                        var domainEvent = new ChatEvent(user, eventDate);

                        domainEvents.put(eventDate, domainEvent);
                    }

                    counter.set(counter.decrementAndGet());
                }));

        while (counter.get() != 0)
            Thread.onSpinWait();

        return new ArrayList<>(domainEvents.values());
    }

    //region Auth

    /**
     * Authorize
     *
     * @param param String for auth step
     *
     * @return Auth step result
     */
    @SneakyThrows
    public String authorize(String param) {
        queryParam = param;

        next = false;
        if (authorizationState != null)
            onAuthorizationStateUpdated(authorizationState);

        while (!next)
            Thread.onSpinWait();

        queryParam = null;

        return result;
    }

    /**
     * Authorization steps actions
     *
     * @param authorizationState current auth state
     */
    private void onAuthorizationStateUpdated(TdApi.AuthorizationState authorizationState) {
        if (authorizationState != null)
            this.authorizationState = authorizationState;

        switch (this.authorizationState.getConstructor()) {
            case TdApi.AuthorizationStateWaitTdlibParameters.CONSTRUCTOR -> {
                var parameters = new TdApi.TdlibParameters();

                parameters.useMessageDatabase = telegramProperties.isUseMessageDatabase();
                parameters.useSecretChats = telegramProperties.isUseSecretChats();
                parameters.apiId = telegramProperties.getApiId();
                parameters.apiHash = telegramProperties.getApiHash();
                parameters.systemLanguageCode = telegramProperties.getSystemLanguageCode();
                parameters.deviceModel = telegramProperties.getDeviceModel();
                parameters.applicationVersion = telegramProperties.getApplicationVersion();
                parameters.enableStorageOptimizer = telegramProperties.isEnableStorageOptimizer();

                client.send(new TdApi.SetTdlibParameters(parameters), new TelegramApiExecutorService.AuthorizationRequestHandler());
                return;
            }
            case TdApi.AuthorizationStateWaitEncryptionKey.CONSTRUCTOR -> {
                client.send(new TdApi.CheckDatabaseEncryptionKey(), new AuthorizationRequestHandler());
                return;
            }
            case TdApi.AuthorizationStateWaitPhoneNumber.CONSTRUCTOR -> {
                if (queryParam == null) {
                    setAuthResultAndGoNext(AuthMessage.Enter_phone_number);
                    break;
                }
                client.send(new TdApi.SetAuthenticationPhoneNumber(queryParam, null), new TelegramApiExecutorService.AuthorizationRequestHandler());
            }
            case TdApi.AuthorizationStateWaitCode.CONSTRUCTOR -> {
                if (queryParam == null) {
                    setAuthResultAndGoNext(AuthMessage.Enter_authentication_code);
                    break;
                }
                client.send(new TdApi.CheckAuthenticationCode(queryParam), new AuthorizationRequestHandler());
            }

            case TdApi.AuthorizationStateWaitPassword.CONSTRUCTOR -> {
                if (queryParam == null) {
                    setAuthResultAndGoNext(AuthMessage.Enter_password);
                    break;
                }
                client.send(new TdApi.CheckAuthenticationPassword(queryParam), new TelegramApiExecutorService.AuthorizationRequestHandler());
            }
            case TdApi.AuthorizationStateReady.CONSTRUCTOR -> setAuthResultAndGoNext(AuthMessage.You_are_authorized);
            case TdApi.AuthorizationStateClosed.CONSTRUCTOR -> setAuthResultAndGoNext(AuthMessage.Enter_phone_number);
            default -> {
                result = "Unsupported authorization state:" + authorizationState;
                next = true;
            }
        }

        queryParam = null;
    }

    /**
     * Set auth result and go to nest auth step
     *
     * @param message Auth result message
     */
    private void setAuthResultAndGoNext(AuthMessage message) {
        result = message.toString();
        next = true;
    }

    private enum AuthMessage {
        Enter_phone_number,
        Enter_authentication_code,
        Enter_password,
        You_are_authorized
    }

    private class AuthorizationRequestHandler implements ResultHandler {
        @Override
        public void onResult(TdApi.Object object) {
            switch (object.getConstructor()) {
                case TdApi.Error.CONSTRUCTOR:
                    System.err.println("Receive an error:" + object);
                    onAuthorizationStateUpdated(null); // repeat last action
                    break;
                case TdApi.Ok.CONSTRUCTOR:
                    // result is already received through UpdateAuthorizationState, nothing to do
                    break;
                default:
                    System.err.println("Receive wrong response from TDLib:" + object);
            }
        }
    }

//endregion
}