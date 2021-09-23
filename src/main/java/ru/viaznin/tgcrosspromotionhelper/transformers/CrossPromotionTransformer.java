package ru.viaznin.tgcrosspromotionhelper.transformers;

import org.mapstruct.Mapper;
import ru.viaznin.tgcrosspromotionhelper.domain.models.CrossPromotion;
import ru.viaznin.tgcrosspromotionhelper.dto.CrossPromotionDto;

/**
 * @author Ilya Viaznin
 */
@Mapper(componentModel = "spring", uses = {AdministratingChannelTransformer.class, UserTransformer.class})
public interface CrossPromotionTransformer extends Transformer<CrossPromotion, CrossPromotionDto> {
}
