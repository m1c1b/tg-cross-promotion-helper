package ru.viaznin.tgcrosspromotionhelper.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.viaznin.tgcrosspromotionhelper.services.TelegramApiExecutorService;

/**
 * @author Ilya Viaznin
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final TelegramApiExecutorService telegramApiExecutor;

    @Autowired
    public AuthController(TelegramApiExecutorService telegramApiExecutor) {
        this.telegramApiExecutor = telegramApiExecutor;
    }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String query){

        return telegramApiExecutor.authorize(query);
    }
}
