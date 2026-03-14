package com.thepantry.recipeservice.domains.businessRules;

import com.thepantry.recipeservice.domains.common.IBusinessRule;

import java.time.Duration;

public class PreparationTimeMustBeAtLeastOneMinute implements IBusinessRule {

    private final short MIN_PREPARATION_TIME_SECONDS = 60;

    private final Duration preparationTimeMinutes;

    public PreparationTimeMustBeAtLeastOneMinute(Duration preparationTimeMinutes) {
        this.preparationTimeMinutes = preparationTimeMinutes;
    }

    @Override
    public boolean isBroken() {
        return this.preparationTimeMinutes != null &&
                this.preparationTimeMinutes.toSeconds() < MIN_PREPARATION_TIME_SECONDS;
    }

    @Override
    public String getMessage() {
        return "Preparation time must be at least one minute";
    }
}
