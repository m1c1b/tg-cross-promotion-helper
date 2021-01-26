package ru.viaznin.tgcrosspromotionhelper.domain.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.viaznin.tgcrosspromotionhelper.domain.models.telegram.User;
import ru.viaznin.tgcrosspromotionhelper.domain.models.telegram.channels.AdministratedChannel;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * @author Ilya Viaznin
 */
@Getter
@Setter
@NoArgsConstructor
@Entity(name = "t_cross_promotions")
public class CrossPromotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "start_date")
    public Date startDate;

    @Column(name = "end_date")
    public Date endDate;

    @OneToMany(cascade = CascadeType.ALL)
    public List<User> enteredUsers;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "administrating_channel_id")
    public AdministratedChannel administratingChannel;

    public CrossPromotion(Date startDate, AdministratedChannel administratingChannel) {
        this.startDate = startDate;
        this.administratingChannel = administratingChannel;
    }
}