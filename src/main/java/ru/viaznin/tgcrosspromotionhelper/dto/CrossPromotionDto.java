package ru.viaznin.tgcrosspromotionhelper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @author Ilya Viaznin
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public final class CrossPromotionDto {
    private Long id;

    /**
     * Start date
     */
    private Date startDate;

    /**
     * End date
     */
    private Date endDate;

    /**
     * Link the user joined
     */
    private String inviteLink;

    /**
     * Joined users
     */
    private List<UserDto> joinedUsers;

    /**
     * Administrating channel
     */
    private AdministratingChannelDto administratingChannel;
}