package com.thepantry.recipeservice.domains.businessRules;

import com.thepantry.recipeservice.domains.common.IBusinessRule;

import java.time.Duration;

public class CookingTimeMustBeAtLeastOneMinute implements IBusinessRule {

    private final short MIN_COOKING_TIME_SECONDS = 60;

    private final Duration cookingTimeMinutes;

    public CookingTimeMustBeAtLeastOneMinute(Duration cookingTimeMinutes) {
        this.cookingTimeMinutes = cookingTimeMinutes;
    }

    @Override
    public boolean isBroken() {
        return this.cookingTimeMinutes != null &&
                this.cookingTimeMinutes.toSeconds() < MIN_COOKING_TIME_SECONDS;
    }

    @Override
    public String getMessage() {
        return "Cooking time must be at least one minute";
    }
}
