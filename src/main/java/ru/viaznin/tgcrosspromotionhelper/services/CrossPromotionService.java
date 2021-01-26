package ru.viaznin.tgcrosspromotionhelper.services;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.viaznin.tgcrosspromotionhelper.domain.models.CrossPromotion;
import ru.viaznin.tgcrosspromotionhelper.domain.models.telegram.ChatEvent;
import ru.viaznin.tgcrosspromotionhelper.domain.models.telegram.channels.AdministratedChannel;
import ru.viaznin.tgcrosspromotionhelper.repositories.AdministratingChannelsRepository;
import ru.viaznin.tgcrosspromotionhelper.repositories.CrossPromotionRepository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for cross promotion actions
 *
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

    /**
     * Get telegram id by cross promotion id
     *
     * @param crossPromotionId Cross promotion id
     * @return telegram id
     */
    public Long getAdministratingChannelTelegramId(Long crossPromotionId) {
        var crossPromotion = crossPromotionRepository
                .findById(crossPromotionId)
                .orElseThrow(() -> new IllegalArgumentException("Cross promotion with id: " + crossPromotionId + "does not exist."));

        return crossPromotion.getAdministratingChannel().telegramId;
    }

    /**
     * End cross promotion
     *
     * @param crossPromotionId Cross promotion id
     * @param allJoinedUsers   Joined users
     * @return Current cross promotion id
     */
    @SneakyThrows
    public Long end(Long crossPromotionId, List<ChatEvent> allJoinedUsers) {
        var crossPromotion = crossPromotionRepository
                .findById(crossPromotionId)
                .orElseThrow(() -> new IllegalArgumentException("Cross promotion with id: " + crossPromotionId + "does not exist."));

        if (crossPromotion.getEndDate() != null)
            throw new IllegalAccessException("This cross promotion is ended!");

        var crossPromotionStartDate = crossPromotion.getStartDate();

        var joinedAfterStart = allJoinedUsers
                .stream()
                .filter(ce -> crossPromotionStartDate.before(ce.getDate()))
                .map(ChatEvent::getUser)
                .collect(Collectors.toList());

        joinedAfterStart.forEach(u -> u.setCrossPromotion(crossPromotion));

        crossPromotion.setEnteredUsers(joinedAfterStart);
        crossPromotion.setEndDate(new Date());

        crossPromotionRepository.save(crossPromotion);

        return crossPromotion.getId();
    }
}
