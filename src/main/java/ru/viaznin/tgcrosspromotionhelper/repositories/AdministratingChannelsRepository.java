package ru.viaznin.tgcrosspromotionhelper.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.viaznin.tgcrosspromotionhelper.domain.models.telegram.channels.AdministratedChannel;

/**
 * @author Ilya Viaznin
 */
public interface AdministratingChannelsRepository extends JpaRepository<AdministratedChannel, Long> {
    AdministratedChannel findFirstByTelegramId(Long id);
}