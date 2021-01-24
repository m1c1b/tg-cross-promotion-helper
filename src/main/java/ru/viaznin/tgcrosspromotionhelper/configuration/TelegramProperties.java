package ru.viaznin.tgcrosspromotionhelper.configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Ilya Viaznin
 */
@Getter
@Setter
public class TelegramProperties {
    public boolean useMessageDatabase;
    public boolean useSecretChats;
    public int apiId;
    public String apiHash;
    public String systemLanguageCode;
    public String deviceModel;
    public String applicationVersion;
    public boolean enableStorageOptimizer;
}