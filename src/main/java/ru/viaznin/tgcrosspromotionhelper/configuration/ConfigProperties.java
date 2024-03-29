package ru.viaznin.tgcrosspromotionhelper.configuration;

import it.tdlight.common.TelegramClient;
import it.tdlight.tdlight.ClientManager;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Bean configuration
 *
 * @author Ilya Viaznin
 */
@Configuration
public class ConfigProperties {
    @Bean
    @ConfigurationProperties(prefix = "telegram")
    public TelegramProperties telegramProperties() {
        return new TelegramProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "heroku")
    public HerokuProperties herokuProperties() {
        return new HerokuProperties();
    }

    @Bean
    public TelegramClient telegramClient() {
        return ClientManager.create();
    }
}