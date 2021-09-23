package ru.viaznin.tgcrosspromotionhelper.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.viaznin.tgcrosspromotionhelper.domain.models.CrossPromotion;
import ru.viaznin.tgcrosspromotionhelper.dto.CrossPromotionDto;
import ru.viaznin.tgcrosspromotionhelper.services.CrossPromotionService;
import ru.viaznin.tgcrosspromotionhelper.services.TelegramApiExecutorService;

import java.util.List;

/**
 * Controller for cross promotion actions
 *
 * @author Ilya Viaznin
 */
@RestController
@RequestMapping("/crossPromotion")
@RequiredArgsConstructor
public class CrossPromotionController {
    private final CrossPromotionService crossPromotionService;

    private final TelegramApiExecutorService telegramApiExecutorService;

    /**
     * Start cross promotion
     *
     * @param administratingChannelId Channel id
     * @param newChannelName          Channel name if it doesn't exists
     *
     * @return Created cross promotion
     */
    @PostMapping("/start")
    public CrossPromotion start(@RequestParam Long administratingChannelId,
                                @RequestParam(required = false) String inviteLink,
                                @RequestParam(required = false) String newChannelName) {
        return crossPromotionService.start(administratingChannelId, inviteLink, newChannelName);
    }

    /**
     * End cross promotion
     *
     * @param crossPromotionId Cross promotion id
     *
     * @return Current cross promotion id
     */
    @PatchMapping("/end")
    public Long end(@RequestParam Long crossPromotionId) {
        var tgId = crossPromotionService.getAdministratingChannelTelegramId(crossPromotionId);

        var allJoinedUsers = telegramApiExecutorService.getJoinedDomainEventsByChannelId(tgId);

        return crossPromotionService.end(crossPromotionId, allJoinedUsers);
    }

    /**
     * Update cross promotion model
     *
     * @param crossPromotionDto Model from request
     *
     * @return Updated model
     */
    @PatchMapping("/update")
    public CrossPromotionDto update(@RequestBody CrossPromotionDto crossPromotionDto) {
        return crossPromotionService.update(crossPromotionDto);
    }

    /**
     * Get cross promotion report
     *
     * @param crossPromotionId Cross promotion id
     *
     * @return Cross promotion report
     */
    @GetMapping("/getReport")
    public String getReport(@RequestParam Long crossPromotionId) {
        return crossPromotionService.getReport(crossPromotionId);
    }

    /**
     * Get ongoing cross promotions
     *
     * @return Ongoing cross promotions
     */
    @GetMapping("/getOngoing")
    public List<CrossPromotion> getOngoing() {
        return crossPromotionService.getOngoing();
    }
}
