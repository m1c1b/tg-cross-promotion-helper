package ru.viaznin.tgcrosspromotionhelper.domain.models.telegram;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Domain chat event
 *
 * @author Ilya Viaznin
 */
@Getter
@Setter
public class ChatEvent {
    /**
     * User of event
     */
    public User user;

    /**
     * Event date
     */
    public Date date;

    public ChatEvent(User user, Date date) {
        this.user = user;
        this.date = date;
    }
}
