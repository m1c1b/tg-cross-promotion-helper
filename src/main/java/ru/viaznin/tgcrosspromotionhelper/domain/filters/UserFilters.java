package ru.viaznin.tgcrosspromotionhelper.domain.filters;

import ru.viaznin.tgcrosspromotionhelper.domain.models.CrossPromotion;
import ru.viaznin.tgcrosspromotionhelper.domain.models.telegram.ChatEvent;

import java.util.Map;
import java.util.function.BiFunction;

import static ru.viaznin.tgcrosspromotionhelper.domain.filters.UserFilters.FilterType.*;

/**
 * @author Ilya Viaznin
 */
public class UserFilters {
    public enum FilterType {
        /**
         * Empty filter
         */
        NONE,
        /**
         * Filter by invite link
         */
        INVITE_LINK
    }

    /**
     * Filters for users on cross promotion ending
     */
    public static final Map<FilterType, BiFunction<CrossPromotion, ChatEvent, Boolean>> userFilters = Map.of(
            NONE, ((crossPromotion, chatEvent) -> true),

            INVITE_LINK, ((crossPromotion, chatEvent) -> {
                var link = chatEvent.getInviteLink();

                // Telegram hiding links that created by other admins
                var usefulPartOfLink = link.replace("...", "");

                return crossPromotion.getInviteLink().contains(usefulPartOfLink);
            })
    );

    public static FilterType getFilterType(CrossPromotion crossPromotion) {

        return crossPromotion.getInviteLink().isBlank()
                ? NONE
                : INVITE_LINK;
    }
}
