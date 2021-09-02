package ru.viaznin.tgcrosspromotionhelper.validators;

import br.com.fluentvalidator.AbstractValidator;
import ru.viaznin.tgcrosspromotionhelper.domain.models.CrossPromotion;

import static br.com.fluentvalidator.predicate.StringPredicate.*;
import static br.com.fluentvalidator.predicate.LogicalPredicate.not;


/**
 * @author Ilya Viaznin
 */
public class CrossPromotionValidator extends AbstractValidator<CrossPromotion> {
    @Override
    public void rules() {
        ruleFor(CrossPromotion::getInviteLink)
                .must(stringContains("https://t.me/joinchat/"))
                .when(not(stringEmptyOrNull()))
                .withMessage("Incorrect telegram invite link!");
    }
}