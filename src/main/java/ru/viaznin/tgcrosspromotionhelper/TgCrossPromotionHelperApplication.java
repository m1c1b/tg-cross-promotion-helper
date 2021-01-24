package ru.viaznin.tgcrosspromotionhelper;

import lombok.SneakyThrows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.apache.tomcat.jni.Library;

import java.io.File;
import java.util.logging.Logger;

@SpringBootApplication
public class TgCrossPromotionHelperApplication {

    public static void main(String[] args) {
        SpringApplication.run(TgCrossPromotionHelperApplication.class, args);
        loadTelegramNativeLibrary();
    }

    @SneakyThrows
    private static void loadTelegramNativeLibrary() {
        var lib = new File(System.mapLibraryName("tdjni"));
        Library.load(lib.getAbsolutePath());
        Logger.getAnonymousLogger().info("tdjni successfully load");
    }
}