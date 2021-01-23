package ru.viaznin.tgcrosspromotionhelper;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class TgCrossPromotionHelperApplication {

    public static void main(String[] args) {
        loadTelegramNativeLibrary();
        SpringApplication.run(TgCrossPromotionHelperApplication.class, args);
    }

    @SneakyThrows
    private static void loadTelegramNativeLibrary() {
        var lib = new File(System.mapLibraryName("tdjni"));
        System.load(lib.getAbsolutePath());
    }
}