package ru.viaznin.tgcrosspromotionhelper.domain.models.telegram;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.viaznin.tgcrosspromotionhelper.domain.models.CrossPromotion;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * @author Ilya Viaznin
 */
@Setter
@Getter
@NoArgsConstructor
@Entity(name = "t_users")
public class User extends TelegramObject {
    @Column(name = "nickname")
    public String nickname;

    @ManyToOne
    public CrossPromotion crossPromotion;

    public User(String nickname, String firstName, int telegramId) {
        this.nickname = nickname;
        this.name = firstName;
        this.telegramId = (long) telegramId;
    }
}