package ru.viaznin.tgcrosspromotionhelper.controllers;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.viaznin.tgcrosspromotionhelper.services.TelegramApiExecutorService;

import java.util.List;

/**
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

    /**
     * Get channels by title substring
     * @param titleSubstring title substring
     * @return id - title pairs
     */
    @GetMapping("/getChannels")
    public List<ImmutablePair<Integer, String>> getChannels(@RequestParam String titleSubstring) {

        return telegramApiExecutor.getChannels(titleSubstring);
    }
}
