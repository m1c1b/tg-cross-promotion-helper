package ru.viaznin.tgcrosspromotionhelper.transformers;

import org.mapstruct.Mapper;
import ru.viaznin.tgcrosspromotionhelper.domain.models.telegram.User;
import ru.viaznin.tgcrosspromotionhelper.dto.UserDto;

/**
 * @author Ilya Viaznin
 */
@Mapper(componentModel = "spring", uses = CrossPromotionTransformer.class)
public interface UserTransformer extends Transformer<User, UserDto> {
}
