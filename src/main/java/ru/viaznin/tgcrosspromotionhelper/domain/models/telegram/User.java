package ru.viaznin.tgcrosspromotionhelper.domain.models.telegram;

import lombok.Getter;
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
@Entity(name = "t_users")
public class User extends TelegramObject {
    @Column(name = "nickname")
    public String nickname;

    @ManyToOne
    public CrossPromotion crossPromotion;
}