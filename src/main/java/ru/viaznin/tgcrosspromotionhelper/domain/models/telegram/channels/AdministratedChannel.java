package ru.viaznin.tgcrosspromotionhelper.domain.models.telegram.channels;

import lombok.NoArgsConstructor;
import ru.viaznin.tgcrosspromotionhelper.domain.models.telegram.TelegramObject;

import javax.persistence.Entity;

/**
 * @author Ilya Viaznin
 */
@Entity(name = "t_administrated_channels")
@NoArgsConstructor
public class AdministratedChannel extends TelegramObject {
    public AdministratedChannel(Long telegramId, String name) {
        this.telegramId = telegramId;
        this.name = name;
    }
}