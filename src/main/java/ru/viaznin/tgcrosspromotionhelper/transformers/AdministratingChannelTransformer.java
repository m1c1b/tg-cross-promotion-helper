package ru.viaznin.tgcrosspromotionhelper.transformers;

import org.mapstruct.Mapper;
import ru.viaznin.tgcrosspromotionhelper.domain.models.telegram.channels.AdministratingChannel;
import ru.viaznin.tgcrosspromotionhelper.dto.AdministratingChannelDto;

/**
 * @author Ilya Viaznin
 */
@Mapper(componentModel = "spring")
public interface AdministratingChannelTransformer extends Transformer<AdministratingChannel, AdministratingChannelDto> {
}
