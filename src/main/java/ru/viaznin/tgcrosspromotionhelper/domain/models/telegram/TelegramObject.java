package ru.viaznin.tgcrosspromotionhelper.domain.models.telegram;

import javax.persistence.*;

/**
 * @author Ilya Viaznin
 */
@MappedSuperclass
public abstract class TelegramObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Column(name = "name")
    public String name;
}