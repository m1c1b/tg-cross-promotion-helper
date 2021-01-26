package ru.viaznin.tgcrosspromotionhelper.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.viaznin.tgcrosspromotionhelper.domain.models.CrossPromotion;
import ru.viaznin.tgcrosspromotionhelper.services.CrossPromotionService;

/**
 * Class for cross promotion actions
 *
 * @author Ilya Viaznin
 */
@RestController
@RequestMapping("/crossPromotion")
public class CrossPromotionController {
    private final CrossPromotionService crossPromotionService;

    @Autowired
    public CrossPromotionController(CrossPromotionService crossPromotionService) {
        this.crossPromotionService = crossPromotionService;
    }

    /**
     * Start cross promotion
     *
     * @param administratingChannelId Channel id
     * @param newChannelName          Channel name if it doesn't exists
     * @return Created cross promotion
     */
    @PostMapping("/start")
    public CrossPromotion start(@RequestParam Long administratingChannelId, @RequestParam(required = false) String newChannelName) {
        return crossPromotionService.start(administratingChannelId, newChannelName);
    }
}
