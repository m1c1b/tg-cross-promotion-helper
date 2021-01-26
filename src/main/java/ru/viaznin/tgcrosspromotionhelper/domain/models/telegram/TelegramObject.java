package ru.viaznin.tgcrosspromotionhelper.domain.models.telegram;

import javax.persistence.*;

/**
 * Default telegram object
 *
 * @author Ilya Viaznin
 */
@MappedSuperclass
public abstract class TelegramObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    /**
     * Telegram id
     */
    @Column(name = "telegram_id")
    public Long telegramId;

    /**
     * Object name
     */
    @Column(name = "name")
    public String name;
}