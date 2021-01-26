package ru.viaznin.tgcrosspromotionhelper.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.viaznin.tgcrosspromotionhelper.domain.models.telegram.channels.AdministratingChannel;

/**
 * Administrating channel repository
 *
 * @author Ilya Viaznin
 */
public interface AdministratingChannelsRepository extends JpaRepository<AdministratingChannel, Long> {
    /**
     * Find first administrating channel by telegram id
     *
     * @param id telegram id
     *
     * @return Administrating channel
     */
    AdministratingChannel findFirstByTelegramId(Long id);
}