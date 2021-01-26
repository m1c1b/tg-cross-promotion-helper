package ru.viaznin.tgcrosspromotionhelper.domain.models.telegram;

import lombok.Getter;

import java.util.Date;

/**
 * Domain chat event
 *
 * @author Ilya Viaznin
 */
@Getter
public class ChatEvent {
    /**
     * User of event
     */
    private final User user;

    /**
     * Event date
     */
    private final Date date;

    public ChatEvent(User user, Date date) {
        this.user = user;
        this.date = date;
    }
}
