package ru.viaznin.tgcrosspromotionhelper.controllers;

import it.tdlight.jni.TdApi;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.viaznin.tgcrosspromotionhelper.services.TelegramApiExecutorService;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for executing telegram API
 *
 * @author Ilya Viaznin
 */
@RestController
@RequestMapping("/telegram")
public class TelegramController {
    private final TelegramApiExecutorService telegramApiExecutor;

    @Autowired
    public TelegramController(TelegramApiExecutorService telegramApiExecutor) {
        this.telegramApiExecutor = telegramApiExecutor;
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String query) {

        return telegramApiExecutor.authorize(query);
    }

    @DeleteMapping("/logout")
    public String logout() {

        return telegramApiExecutor.logout();
    }

    /**
     * Get channels by title substring
     *
     * @param titleSubstring title substring
     *
     * @return id - title pairs
     */
    @GetMapping("/getChannels")
    public List<ImmutablePair<Long, String>> getChannels(@RequestParam String titleSubstring) {

        return telegramApiExecutor.getChannels(titleSubstring);
    }

    /**
     * Get joined users channel events
     *
     * @param channelId Channel id
     *
     * @return List of channel events
     */
    @GetMapping("/getJoinedLogUsers")
    public List<TdApi.ChatEvent> getJoinedLogUsers(@RequestParam Long channelId) {

        return telegramApiExecutor.getJoinedLogUsers(channelId, new ArrayList<>(), 0);
    }
}
