package ru.viaznin.tgcrosspromotionhelper.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.viaznin.tgcrosspromotionhelper.domain.models.CrossPromotion;

/**
 * @author Ilya Viaznin
 */
@Repository
public interface CrossPromotionRepository extends JpaRepository<CrossPromotion, Long> {
}