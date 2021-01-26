package ru.viaznin.tgcrosspromotionhelper.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.viaznin.tgcrosspromotionhelper.domain.models.CrossPromotion;
import ru.viaznin.tgcrosspromotionhelper.services.CrossPromotionService;
import ru.viaznin.tgcrosspromotionhelper.services.TelegramApiExecutorService;

/**
 * Controller for cross promotion actions
 *
 * @author Ilya Viaznin
 */
@RestController
@RequestMapping("/crossPromotion")
public class CrossPromotionController {
    private final CrossPromotionService crossPromotionService;
    private final TelegramApiExecutorService telegramApiExecutorService;

    @Autowired
    public CrossPromotionController(CrossPromotionService crossPromotionService, TelegramApiExecutorService telegramApiExecutorService) {
        this.crossPromotionService = crossPromotionService;
        this.telegramApiExecutorService = telegramApiExecutorService;
    }

    /**
     * Start cross promotion
     *
     * @param administratingChannelId Channel id
     * @param newChannelName          Channel name if it doesn't exists
     *
     * @return Created cross promotion
     */
    @PostMapping("/start")
    public CrossPromotion start(@RequestParam Long administratingChannelId, @RequestParam(required = false) String newChannelName) {
        return crossPromotionService.start(administratingChannelId, newChannelName);
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
}
