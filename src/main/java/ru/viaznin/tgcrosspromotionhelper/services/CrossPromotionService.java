package ru.viaznin.tgcrosspromotionhelper.services;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.viaznin.tgcrosspromotionhelper.domain.models.CrossPromotion;
import ru.viaznin.tgcrosspromotionhelper.domain.models.telegram.ChatEvent;
import ru.viaznin.tgcrosspromotionhelper.domain.models.telegram.channels.AdministratingChannel;
import ru.viaznin.tgcrosspromotionhelper.repositories.AdministratingChannelsRepository;
import ru.viaznin.tgcrosspromotionhelper.repositories.CrossPromotionRepository;
import ru.viaznin.tgcrosspromotionhelper.validators.CrossPromotionValidator;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static ru.viaznin.tgcrosspromotionhelper.domain.filters.UserFilters.*;

/**
 * Service for cross promotion actions
 *
 * @author Ilya Viaznin
 */
@Service
public class CrossPromotionService implements ValidationService {
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
     * @param inviteLink              Link the user joined
     *
     * @return Created cross promotion
     */
    public CrossPromotion start(Long administratingChannelId, String inviteLink, String newChannelName) {
        var administratingChannel = administratingChannelsRepository
                .findFirstByTelegramId(administratingChannelId);

        if (administratingChannel == null)
            administratingChannel = new AdministratingChannel(administratingChannelId, newChannelName);

        var newCrossPromo = new CrossPromotion(new Date(), administratingChannel, inviteLink);

        validateAndThrowIfInvalid(new CrossPromotionValidator(), newCrossPromo);

        crossPromotionRepository.save(newCrossPromo);

        return newCrossPromo;
    }

    /**
     * Get telegram id by cross promotion id
     *
     * @param crossPromotionId Cross promotion id
     *
     * @return telegram id
     */
    public Long getAdministratingChannelTelegramId(Long crossPromotionId) {
        var crossPromotion = crossPromotionRepository
                .findById(crossPromotionId)
                .orElseThrow(() -> new IllegalArgumentException(createExceptionMessage(crossPromotionId)));

        return crossPromotion.getAdministratingChannel().getTelegramId();
    }

    /**
     * End cross promotion
     *
     * @param crossPromotionId Cross promotion id
     * @param allJoinedUsers   Joined users
     *
     * @return Current cross promotion id
     */
    @SneakyThrows
    public Long end(Long crossPromotionId, List<ChatEvent> allJoinedUsers) {
        var crossPromotion = crossPromotionRepository
                .findById(crossPromotionId)
                .orElseThrow(() -> new IllegalArgumentException(createExceptionMessage(crossPromotionId)));

        if (crossPromotion.getEndDate() != null)
            throw new IllegalAccessException("This cross promotion is ended!");

        var crossPromotionStartDate = crossPromotion.getStartDate();

        var filterType = getFilterType(crossPromotion);
        var joinedAfterStart = allJoinedUsers
                .stream()
                .filter(ce -> crossPromotionStartDate.before(ce.getDate()))
                .filter(ce -> userFilters.get(filterType).apply(crossPromotion, ce))
                .map(ChatEvent::getUser)
                .collect(Collectors.toList());

        joinedAfterStart.forEach(u -> u.setCrossPromotion(crossPromotion));

        crossPromotion.setJoinedUsers(joinedAfterStart);
        crossPromotion.setEndDate(new Date());

        crossPromotionRepository.save(crossPromotion);

        return crossPromotion.getId();
    }

    /**
     * Get cross promotion report
     *
     * @param crossPromotionId Cross promotion id
     *
     * @return Cross promotion report
     */
    public String getReport(Long crossPromotionId) {
        var crossPromotion = crossPromotionRepository
                .findById(crossPromotionId)
                .orElseThrow(() -> new IllegalArgumentException(createExceptionMessage(crossPromotionId)));

        if (crossPromotion.getEndDate() == null)
            throw new IllegalArgumentException("Cross promotion with id: " + crossPromotionId + " isn't over yet.");

        var builder = new StringBuilder();

        crossPromotion.getJoinedUsers().forEach(u -> builder.append(u).append('\n'));

        return builder.toString();
    }

    private String createExceptionMessage(Long crossPromotionId) {
        return "Cross promotion with id: " + crossPromotionId + " does not exist.";
    }

    /**
     * Get ongoing cross promotions
     *
     * @return Ongoing cross promotions
     */
    public List<CrossPromotion> getOngoing() {
        return crossPromotionRepository.findAllByEndDateIsNullOrderByStartDate();
    }
}
