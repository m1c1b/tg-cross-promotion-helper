package ru.viaznin.tgcrosspromotionhelper.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author Ilya Viaznin
 */
@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping("/imOk")
    public String imOk() {
        return new Date().toString();
    }
}