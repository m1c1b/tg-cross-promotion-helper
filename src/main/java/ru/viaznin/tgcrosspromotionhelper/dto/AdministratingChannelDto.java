package ru.viaznin.tgcrosspromotionhelper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ilya Viaznin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
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
