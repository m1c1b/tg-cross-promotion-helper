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
import java.util.stream.Collectors;


/**
 * @author Ilya Viaznin
 */
@Service
public class TelegramApiExecutorService {
    private final TelegramProperties telegramProperties;
    private final TelegramClient client;

    // region Auth

    private volatile String queryParam;
    private static volatile String result;
    private TdApi.AuthorizationState authorizationState;
    private static volatile boolean next;

    //endregion

    //region Filled from client

    private static final ConcurrentHashMap<Long, TdApi.Chat> chats = new ConcurrentHashMap<>();

    //endregion

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

//                case TdApi.UpdateUser.CONSTRUCTOR:
//                    TdApi.UpdateUser updateUser = (TdApi.UpdateUser) object;
//                    users.put(updateUser.user.id, updateUser.user);
//                    break;
//                case TdApi.UpdateUserStatus.CONSTRUCTOR: {
//                    TdApi.UpdateUserStatus updateUserStatus = (TdApi.UpdateUserStatus) object;
//                    TdApi.User user = users.get(updateUserStatus.userId);
//                    synchronized (user) {
//                        user.status = updateUserStatus.status;
//                    }
//                    break;
//                }
//                case TdApi.UpdateBasicGroup.CONSTRUCTOR:
//                    TdApi.UpdateBasicGroup updateBasicGroup = (TdApi.UpdateBasicGroup) object;
//                    basicGroups.put(updateBasicGroup.basicGroup.id, updateBasicGroup.basicGroup);
//                    break;
//                case TdApi.UpdateSupergroup.CONSTRUCTOR:
//                    TdApi.UpdateSupergroup updateSupergroup = (TdApi.UpdateSupergroup) object;
//                    supergroups.put(updateSupergroup.supergroup.id, updateSupergroup.supergroup);
//                    break;
//                case TdApi.UpdateSecretChat.CONSTRUCTOR:
//                    TdApi.UpdateSecretChat updateSecretChat = (TdApi.UpdateSecretChat) object;
//                    secretChats.put(updateSecretChat.secretChat.id, updateSecretChat.secretChat);
//                    break;

                case TdApi.UpdateNewChat.CONSTRUCTOR -> {
                    var updateNewChat = (TdApi.UpdateNewChat) object;
                    var chat = updateNewChat.chat;
                    chats.put(chat.id, chat);
                }
//                case TdApi.UpdateChatTitle.CONSTRUCTOR: {
//                    TdApi.UpdateChatTitle updateChat = (TdApi.UpdateChatTitle) object;
//                    TdApi.Chat chat = chats.get(updateChat.chatId);
//                    synchronized (chat) {
//                        chat.title = updateChat.title;
//                    }
//                    break;
//                }
//                case TdApi.UpdateChatPhoto.CONSTRUCTOR: {
//                    TdApi.UpdateChatPhoto updateChat = (TdApi.UpdateChatPhoto) object;
//                    TdApi.Chat chat = chats.get(updateChat.chatId);
//                    synchronized (chat) {
//                        chat.photo = updateChat.photo;
//                    }
//                    break;
//                }
//                case TdApi.UpdateChatLastMessage.CONSTRUCTOR: {
//                    TdApi.UpdateChatLastMessage updateChat = (TdApi.UpdateChatLastMessage) object;
//                    TdApi.Chat chat = chats.get(updateChat.chatId);
//                    synchronized (chat) {
//                        chat.lastMessage = updateChat.lastMessage;
//                        setChatPositions(chat, updateChat.positions);
//                    }
//                    break;
//                }
//                case TdApi.UpdateChatPosition.CONSTRUCTOR: {
//                    TdApi.UpdateChatPosition updateChat = (TdApi.UpdateChatPosition) object;
//                    if (updateChat.position.list.getConstructor() != TdApi.ChatListMain.CONSTRUCTOR) {
//                        break;
//                    }
//
//                    TdApi.Chat chat = chats.get(updateChat.chatId);
//                    synchronized (chat) {
//                        int i;
//                        for (i = 0; i < chat.positions.length; i++) {
//                            if (chat.positions[i].list.getConstructor() == TdApi.ChatListMain.CONSTRUCTOR) {
//                                break;
//                            }
//                        }
//                        TdApi.ChatPosition[] new_positions = new TdApi.ChatPosition[chat.positions.length + (updateChat.position.order == 0 ? 0 : 1) - (i < chat.positions.length ? 1 : 0)];
//                        int pos = 0;
//                        if (updateChat.position.order != 0) {
//                            new_positions[pos++] = updateChat.position;
//                        }
//                        for (int j = 0; j < chat.positions.length; j++) {
//                            if (j != i) {
//                                new_positions[pos++] = chat.positions[j];
//                            }
//                        }
//                        assert pos == new_positions.length;
//
//                        setChatPositions(chat, new_positions);
//                    }
//                    break;
//                }
//                case TdApi.UpdateChatReadInbox.CONSTRUCTOR: {
//                    TdApi.UpdateChatReadInbox updateChat = (TdApi.UpdateChatReadInbox) object;
//                    TdApi.Chat chat = chats.get(updateChat.chatId);
//                    synchronized (chat) {
//                        chat.lastReadInboxMessageId = updateChat.lastReadInboxMessageId;
//                        chat.unreadCount = updateChat.unreadCount;
//                    }
//                    break;
//                }
//                case TdApi.UpdateChatReadOutbox.CONSTRUCTOR: {
//                    TdApi.UpdateChatReadOutbox updateChat = (TdApi.UpdateChatReadOutbox) object;
//                    TdApi.Chat chat = chats.get(updateChat.chatId);
//                    synchronized (chat) {
//                        chat.lastReadOutboxMessageId = updateChat.lastReadOutboxMessageId;
//                    }
//                    break;
//                }
//                case TdApi.UpdateChatUnreadMentionCount.CONSTRUCTOR: {
//                    TdApi.UpdateChatUnreadMentionCount updateChat = (TdApi.UpdateChatUnreadMentionCount) object;
//                    TdApi.Chat chat = chats.get(updateChat.chatId);
//                    synchronized (chat) {
//                        chat.unreadMentionCount = updateChat.unreadMentionCount;
//                    }
//                    break;
//                }
//                case TdApi.UpdateMessageMentionRead.CONSTRUCTOR: {
//                    TdApi.UpdateMessageMentionRead updateChat = (TdApi.UpdateMessageMentionRead) object;
//                    TdApi.Chat chat = chats.get(updateChat.chatId);
//                    synchronized (chat) {
//                        chat.unreadMentionCount = updateChat.unreadMentionCount;
//                    }
//                    break;
//                }
//                case TdApi.UpdateChatReplyMarkup.CONSTRUCTOR: {
//                    TdApi.UpdateChatReplyMarkup updateChat = (TdApi.UpdateChatReplyMarkup) object;
//                    TdApi.Chat chat = chats.get(updateChat.chatId);
//                    synchronized (chat) {
//                        chat.replyMarkupMessageId = updateChat.replyMarkupMessageId;
//                    }
//                    break;
//                }
//                case TdApi.UpdateChatDraftMessage.CONSTRUCTOR: {
//                    TdApi.UpdateChatDraftMessage updateChat = (TdApi.UpdateChatDraftMessage) object;
//                    TdApi.Chat chat = chats.get(updateChat.chatId);
//                    synchronized (chat) {
//                        chat.draftMessage = updateChat.draftMessage;
//                        setChatPositions(chat, updateChat.positions);
//                    }
//                    break;
//                }
//                case TdApi.UpdateChatPermissions.CONSTRUCTOR: {
//                    TdApi.UpdateChatPermissions update = (TdApi.UpdateChatPermissions) object;
//                    TdApi.Chat chat = chats.get(update.chatId);
//                    synchronized (chat) {
//                        chat.permissions = update.permissions;
//                    }
//                    break;
//                }
//                case TdApi.UpdateChatNotificationSettings.CONSTRUCTOR: {
//                    TdApi.UpdateChatNotificationSettings update = (TdApi.UpdateChatNotificationSettings) object;
//                    TdApi.Chat chat = chats.get(update.chatId);
//                    synchronized (chat) {
//                        chat.notificationSettings = update.notificationSettings;
//                    }
//                    break;
//                }
//                case TdApi.UpdateChatDefaultDisableNotification.CONSTRUCTOR: {
//                    TdApi.UpdateChatDefaultDisableNotification update = (TdApi.UpdateChatDefaultDisableNotification) object;
//                    TdApi.Chat chat = chats.get(update.chatId);
//                    synchronized (chat) {
//                        chat.defaultDisableNotification = update.defaultDisableNotification;
//                    }
//                    break;
//                }
//                case TdApi.UpdateChatIsMarkedAsUnread.CONSTRUCTOR: {
//                    TdApi.UpdateChatIsMarkedAsUnread update = (TdApi.UpdateChatIsMarkedAsUnread) object;
//                    TdApi.Chat chat = chats.get(update.chatId);
//                    synchronized (chat) {
//                        chat.isMarkedAsUnread = update.isMarkedAsUnread;
//                    }
//                    break;
//                }
//                case TdApi.UpdateChatIsBlocked.CONSTRUCTOR: {
//                    TdApi.UpdateChatIsBlocked update = (TdApi.UpdateChatIsBlocked) object;
//                    TdApi.Chat chat = chats.get(update.chatId);
//                    synchronized (chat) {
//                        chat.isBlocked = update.isBlocked;
//                    }
//                    break;
//                }
//                case TdApi.UpdateChatHasScheduledMessages.CONSTRUCTOR: {
//                    TdApi.UpdateChatHasScheduledMessages update = (TdApi.UpdateChatHasScheduledMessages) object;
//                    TdApi.Chat chat = chats.get(update.chatId);
//                    synchronized (chat) {
//                        chat.hasScheduledMessages = update.hasScheduledMessages;
//                    }
//                    break;
//                }
//
//                case TdApi.UpdateUserFullInfo.CONSTRUCTOR:
//                    TdApi.UpdateUserFullInfo updateUserFullInfo = (TdApi.UpdateUserFullInfo) object;
//                    usersFullInfo.put(updateUserFullInfo.userId, updateUserFullInfo.userFullInfo);
//                    break;
//                case TdApi.UpdateBasicGroupFullInfo.CONSTRUCTOR:
//                    TdApi.UpdateBasicGroupFullInfo updateBasicGroupFullInfo = (TdApi.UpdateBasicGroupFullInfo) object;
//                    basicGroupsFullInfo.put(updateBasicGroupFullInfo.basicGroupId, updateBasicGroupFullInfo.basicGroupFullInfo);
//                    break;
//                case TdApi.UpdateSupergroupFullInfo.CONSTRUCTOR:
//                    TdApi.UpdateSupergroupFullInfo updateSupergroupFullInfo = (TdApi.UpdateSupergroupFullInfo) object;
//                    supergroupsFullInfo.put(updateSupergroupFullInfo.supergroupId, updateSupergroupFullInfo.supergroupFullInfo);
//                    break;
            }
        }
    }

    /**
     * Get channels by title substring
     *
     * @param titleSubstring title substring
     * @return id - title pairs
     */
    public List<ImmutablePair<Long, String>> getChannels(String titleSubstring) {
        client.send(new TdApi.GetChats(new TdApi.ChatListMain(), Long.MAX_VALUE, 0, Integer.MAX_VALUE), object -> {
            switch (object.getConstructor()) {
                case TdApi.Error.CONSTRUCTOR -> System.err.println("Receive an error for GetChats:" + object);
                case TdApi.Chats.CONSTRUCTOR -> {
                }
                default -> System.err.println("Receive wrong response from TDLib:" + object);
            }
        });

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
     * @return Joined domain events
     */
    public List<ChatEvent> getJoinedDomainEventsByChannelId(Long channelId) {
        var domainEvents = new ConcurrentHashMap<Date, ChatEvent>();
        var events = getJoinedLogUsers(channelId);

        events.forEach(e ->
                client.send(new TdApi.GetUser(e.userId), object -> {
                    var tgUser = (TdApi.User) object;

                    var user = new User(tgUser.username, tgUser.firstName, tgUser.id);
                    var eventDate = new Date(e.date * 1000L);

                    var domainEvent = new ChatEvent(user, eventDate);

                    domainEvents.put(eventDate, domainEvent);
                }));

        while (domainEvents.size() != events.size())
            Thread.onSpinWait();

        return new ArrayList<>(domainEvents.values());
    }

    //region Auth

    /**
     * Authorize
     *
     * @param param String for auth step
     * @return Auth step result
     */
    @SneakyThrows
    public String authorize(String param) {
        queryParam = param;
        if (authorizationState != null)
            onAuthorizationStateUpdated(authorizationState);

        next = false;
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