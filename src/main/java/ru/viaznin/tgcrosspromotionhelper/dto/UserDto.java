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
public class UserDto {

    private Long id;

    /**
     * Telegram id
     */
    private Long telegramId;

    /**
     * User name
     */
    private String name;

    /**
     * User nickname without @
     */
    private String nickname;

    /**
     * Cross promotion reference
     */
    private CrossPromotionDto crossPromotion;
}
