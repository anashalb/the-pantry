package com.thepantry.recipeservice.domains.businessRules;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class ReadyInTimeMustBeAtLeastOneMinuteTests {
    @Test
    void testIsBroken_WhenReadyInTimeIsLessThanOneMinute_ShouldReturnTrue() {
        // Arrange
        Duration shortReadyInTime = Duration.ofSeconds(30);
        ReadyInTimeMustBeAtLeastOneMinute rule = new ReadyInTimeMustBeAtLeastOneMinute(shortReadyInTime);

        // Act
        boolean result = rule.isBroken();

        // Assert
        assertTrue(result, "Expected rule to be broken when ready-in time is less than one minute.");
    }

    @Test
    void testIsBroken_WhenReadyInTimeIsExactlyOneMinute_ShouldReturnFalse() {
        // Arrange
        Duration exactReadyInTime = Duration.ofSeconds(60);
        ReadyInTimeMustBeAtLeastOneMinute rule = new ReadyInTimeMustBeAtLeastOneMinute(exactReadyInTime);

        // Act
        boolean result = rule.isBroken();

        // Assert
        assertFalse(result, "Expected rule NOT to be broken when ready-in time is exactly one minute.");
    }

    @Test
    void testIsBroken_WhenReadyInTimeIsMoreThanOneMinute_ShouldReturnFalse() {
        // Arrange
        Duration longReadyInTime = Duration.ofMinutes(2);
        ReadyInTimeMustBeAtLeastOneMinute rule = new ReadyInTimeMustBeAtLeastOneMinute(longReadyInTime);

        // Act
        boolean result = rule.isBroken();

        // Assert
        assertFalse(result, "Expected rule NOT to be broken when ready-in time is more than one minute.");
    }

    @Test
    void testIsBroken_WhenReadyInTimeIsNull_ShouldReturnFalse() {
        // Arrange
        ReadyInTimeMustBeAtLeastOneMinute rule = new ReadyInTimeMustBeAtLeastOneMinute(null);

        // Act
        boolean result = rule.isBroken();

        // Assert
        assertFalse(result, "Expected rule NOT to be broken when ready-in time is null.");
    }

    @Test
    void testGetMessage_ShouldReturnCorrectMessage() {
        // Arrange
        ReadyInTimeMustBeAtLeastOneMinute rule = new ReadyInTimeMustBeAtLeastOneMinute(Duration.ofSeconds(30));

        // Act
        String message = rule.getMessage();

        // Assert
        assertEquals("Ready-in time must be at least one minute", message);
    }
}