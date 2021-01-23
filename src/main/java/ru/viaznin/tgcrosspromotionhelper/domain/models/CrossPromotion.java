package ru.viaznin.tgcrosspromotionhelper.domain.models;

import lombok.Getter;
import lombok.Setter;
import ru.viaznin.tgcrosspromotionhelper.domain.models.telegram.User;
import ru.viaznin.tgcrosspromotionhelper.domain.models.telegram.channels.AdministratedChannel;
import ru.viaznin.tgcrosspromotionhelper.domain.models.telegram.channels.OutsideChannel;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author Ilya Viaznin
 */
@Getter
@Setter
@Entity(name = "t_cross_promotions")
public class CrossPromotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "start_date")
    public Date startDate;

    @Column(name = "end_date")
    public Date endDate;

    @OneToMany
    public List<User> enteredUsers;

    @OneToOne
    @JoinColumn(name = "outside_channel_id")
    public OutsideChannel outsideChannel;

    @OneToOne
    @JoinColumn(name = "administrating_channel_id")
    public AdministratedChannel administratingChannel;
}