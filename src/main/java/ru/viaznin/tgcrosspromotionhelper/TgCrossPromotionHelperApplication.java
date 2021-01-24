package ru.viaznin.tgcrosspromotionhelper;

import it.tdlight.common.Init;
import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class TgCrossPromotionHelperApplication {

    public static void main(String[] args) {
        loadTelegramNativeLibrary();
        SpringApplication.run(TgCrossPromotionHelperApplication.class, args);
    }

    @SneakyThrows
    private static void loadTelegramNativeLibrary() {
        Init.start();
    }
}