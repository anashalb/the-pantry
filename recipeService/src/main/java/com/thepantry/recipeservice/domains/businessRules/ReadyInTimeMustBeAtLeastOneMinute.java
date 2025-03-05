package com.thepantry.recipeservice.domains.businessRules;

import com.thepantry.recipeservice.domains.common.IBusinessRule;

import java.time.Duration;

public class ReadyInTimeMustBeAtLeastOneMinute implements IBusinessRule {

    private final short MIN_READY_IN_TIME_SECONDS = 60;

    private final Duration readyInTimeMinutes;

    public ReadyInTimeMustBeAtLeastOneMinute(Duration preparationTimeMinutes) {
        this.readyInTimeMinutes = preparationTimeMinutes;
    }

    @Override
    public boolean isBroken() {
        return this.readyInTimeMinutes != null &&
                this.readyInTimeMinutes.getSeconds() < MIN_READY_IN_TIME_SECONDS;
    }

    @Override
    public String getMessage() {
        return "Ready-in time must be at least one minute";
    }
}
