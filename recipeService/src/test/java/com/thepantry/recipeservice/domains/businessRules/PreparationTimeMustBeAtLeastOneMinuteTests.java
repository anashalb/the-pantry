package com.thepantry.recipeservice.domains.businessRules;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class PreparationTimeMustBeAtLeastOneMinuteTests {

    @Test
    void testIsBroken_WhenPreparationTimeIsLessThanOneMinute_ShouldReturnTrue() {

        Duration shortPreparationTime = Duration.ofSeconds(30);
        PreparationTimeMustBeAtLeastOneMinute rule = new PreparationTimeMustBeAtLeastOneMinute(shortPreparationTime);

        assertTrue(rule.isBroken(), "Expected rule to be broken when preparation time is less than one minute.");
    }

    @Test
    void testIsBroken_WhenPreparationTimeIsExactlyOneMinute_ShouldReturnFalse() {

        Duration exactPreparationTime = Duration.ofSeconds(60);
        PreparationTimeMustBeAtLeastOneMinute rule = new PreparationTimeMustBeAtLeastOneMinute(exactPreparationTime);

        assertFalse(rule.isBroken(), "Expected rule NOT to be broken when preparation time is exactly one minute.");
    }

    @Test
    void testIsBroken_WhenPreparationTimeIsMoreThanOneMinute_ShouldReturnFalse() {

        Duration longPreparationTime = Duration.ofMinutes(2);
        PreparationTimeMustBeAtLeastOneMinute rule = new PreparationTimeMustBeAtLeastOneMinute(longPreparationTime);

        assertFalse(rule.isBroken(), "Expected rule NOT to be broken when preparation time is more than one minute.");
    }

    @Test
    void testIsBroken_WhenPreparationTimeIsNull_ShouldReturnFalse() {

        PreparationTimeMustBeAtLeastOneMinute rule = new PreparationTimeMustBeAtLeastOneMinute(null);

        assertFalse(rule.isBroken(), "Expected rule NOT to be broken when preparation time is null.");
    }

    @Test
    void testGetMessage_ShouldReturnCorrectMessage() {
        PreparationTimeMustBeAtLeastOneMinute rule = new PreparationTimeMustBeAtLeastOneMinute(Duration.ofSeconds(30));

        assertEquals("Preparation time must be at least one minute", rule.getMessage());
    }
}