package ru.viaznin.tgcrosspromotionhelper.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.viaznin.tgcrosspromotionhelper.domain.models.CrossPromotion;
import ru.viaznin.tgcrosspromotionhelper.domain.models.telegram.channels.AdministratedChannel;
import ru.viaznin.tgcrosspromotionhelper.repositories.AdministratingChannelsRepository;
import ru.viaznin.tgcrosspromotionhelper.repositories.CrossPromotionRepository;

import java.util.Date;

/**
 * @author Ilya Viaznin
 */
@Service
public class CrossPromotionService {
    private final CrossPromotionRepository crossPromotionRepository;
    private final AdministratingChannelsRepository administratingChannelsRepository;

    @Autowired
    public CrossPromotionService(CrossPromotionRepository crossPromotionRepository, AdministratingChannelsRepository administratingChannelsRepository) {
        this.crossPromotionRepository = crossPromotionRepository;
        this.administratingChannelsRepository = administratingChannelsRepository;
    }

    /**
     * Start cross promotion
     *
     * @param administratingChannelId Channel id
     * @param newChannelName          Channel name if it doesn't exists
     * @return Created cross promotion
     */
    public CrossPromotion start(Long administratingChannelId, String newChannelName) {
        var administratingChannel = administratingChannelsRepository
                .findFirstByTelegramId(administratingChannelId);

        if (administratingChannel == null) {
            administratingChannel = new AdministratedChannel(administratingChannelId, newChannelName);
            administratingChannel.telegramId = administratingChannelId;
        }

        var newCrossPromo = new CrossPromotion(new Date(), administratingChannel);

        crossPromotionRepository.save(newCrossPromo);

        return newCrossPromo;
    }
}
