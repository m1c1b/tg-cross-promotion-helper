package ru.viaznin.tgcrosspromotionhelper.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * @author Ilya Viaznin
 */
@Data
@RequiredArgsConstructor
public class AdministratingChannelDto {

    private Long id;

    /**
     * Telegram id
     */
    private Long telegramId;

    /**
     * Channel name
     */
    private String name;
}
