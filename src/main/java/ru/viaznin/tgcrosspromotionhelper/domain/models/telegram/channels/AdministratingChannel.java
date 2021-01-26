package ru.viaznin.tgcrosspromotionhelper.domain.models.telegram.channels;

import lombok.NoArgsConstructor;
import ru.viaznin.tgcrosspromotionhelper.domain.models.telegram.TelegramObject;

import javax.persistence.Entity;

/**
 * Administrating channel
 *
 * @author Ilya Viaznin
 */
@Entity(name = "t_administrating_channels")
@NoArgsConstructor
public class AdministratingChannel extends TelegramObject {
    public AdministratingChannel(Long telegramId, String name) {
        this.telegramId = telegramId;
        this.name = name;
    }
}