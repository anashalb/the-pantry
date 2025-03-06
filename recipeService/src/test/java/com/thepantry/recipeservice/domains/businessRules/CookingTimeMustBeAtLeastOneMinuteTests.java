package com.thepantry.recipeservice.domains.businessRules;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class CookingTimeMustBeAtLeastOneMinuteTests {

    @Test
    void testIsBroken_WhenCookingTimeIsLessThanOneMinute_ShouldReturnTrue() {

        Duration shortCookingTime = Duration.ofSeconds(30);
        CookingTimeMustBeAtLeastOneMinute rule = new CookingTimeMustBeAtLeastOneMinute(shortCookingTime);

        assertTrue(rule.isBroken(), "Expected rule to be broken when cooking time is less than one minute.");
    }

    @Test
    void testIsBroken_WhenCookingTimeIsExactlyOneMinute_ShouldReturnFalse() {
        Duration exactCookingTime = Duration.ofSeconds(60);
        CookingTimeMustBeAtLeastOneMinute rule = new CookingTimeMustBeAtLeastOneMinute(exactCookingTime);

        assertFalse(rule.isBroken(), "Expected rule NOT to be broken when cooking time is exactly one minute.");
    }

    @Test
    void testIsBroken_WhenCookingTimeIsMoreThanOneMinute_ShouldReturnFalse() {
        Duration longCookingTime = Duration.ofMinutes(2);
        CookingTimeMustBeAtLeastOneMinute rule = new CookingTimeMustBeAtLeastOneMinute(longCookingTime);

        assertFalse(rule.isBroken(), "Expected rule NOT to be broken when cooking time is more than one minute.");
    }

    @Test
    void testIsBroken_WhenCookingTimeIsNull_ShouldReturnFalse() {

        CookingTimeMustBeAtLeastOneMinute rule = new CookingTimeMustBeAtLeastOneMinute(null);

        assertFalse(rule.isBroken(), "Expected rule NOT to be broken when cooking time is null.");
    }

    @Test
    void testGetMessage_ShouldReturnCorrectMessage() {

        CookingTimeMustBeAtLeastOneMinute rule = new CookingTimeMustBeAtLeastOneMinute(Duration.ofSeconds(30));

        assertEquals("Cooking time must be at least one minute", rule.getMessage());
    }
}