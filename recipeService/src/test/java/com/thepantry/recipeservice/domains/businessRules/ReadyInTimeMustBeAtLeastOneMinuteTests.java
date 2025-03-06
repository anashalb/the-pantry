package com.thepantry.recipeservice.domains.businessRules;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class ReadyInTimeMustBeAtLeastOneMinuteTests {
    @Test
    void testIsBroken_WhenReadyInTimeIsLessThanOneMinute_ShouldReturnTrue() {
        Duration shortReadyInTime = Duration.ofSeconds(30);
        ReadyInTimeMustBeAtLeastOneMinute rule = new ReadyInTimeMustBeAtLeastOneMinute(shortReadyInTime);

        assertTrue(rule.isBroken(), "Expected rule to be broken when ready-in time is less than one minute.");
    }

    @Test
    void testIsBroken_WhenReadyInTimeIsExactlyOneMinute_ShouldReturnFalse() {
        Duration exactReadyInTime = Duration.ofSeconds(60);
        ReadyInTimeMustBeAtLeastOneMinute rule = new ReadyInTimeMustBeAtLeastOneMinute(exactReadyInTime);

        assertFalse(rule.isBroken(), "Expected rule NOT to be broken when ready-in time is exactly one minute.");
    }

    @Test
    void testIsBroken_WhenReadyInTimeIsMoreThanOneMinute_ShouldReturnFalse() {
        Duration longReadyInTime = Duration.ofMinutes(2);
        ReadyInTimeMustBeAtLeastOneMinute rule = new ReadyInTimeMustBeAtLeastOneMinute(longReadyInTime);

        assertFalse(rule.isBroken(), "Expected rule NOT to be broken when ready-in time is more than one minute.");
    }

    @Test
    void testIsBroken_WhenReadyInTimeIsNull_ShouldReturnFalse() {
        ReadyInTimeMustBeAtLeastOneMinute rule = new ReadyInTimeMustBeAtLeastOneMinute(null);

        assertFalse(rule.isBroken(), "Expected rule NOT to be broken when ready-in time is null.");
    }

    @Test
    void testGetMessage_ShouldReturnCorrectMessage() {
        ReadyInTimeMustBeAtLeastOneMinute rule = new ReadyInTimeMustBeAtLeastOneMinute(Duration.ofSeconds(30));

        assertEquals("Ready-in time must be at least one minute", rule.getMessage());
    }
}