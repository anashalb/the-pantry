package com.thepantry.recipeservice.domains.businessRules;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class PreparationTimeMustBeAtLeastOneMinuteTests {

    @Test
    void testIsBroken_WhenPreparationTimeIsLessThanOneMinute_ShouldReturnTrue() {
        // Arrange
        Duration shortPreparationTime = Duration.ofSeconds(30);
        PreparationTimeMustBeAtLeastOneMinute rule = new PreparationTimeMustBeAtLeastOneMinute(shortPreparationTime);

        // Act
        boolean result = rule.isBroken();

        // Assert
        assertTrue(result, "Expected rule to be broken when preparation time is less than one minute.");
    }

    @Test
    void testIsBroken_WhenPreparationTimeIsExactlyOneMinute_ShouldReturnFalse() {
        // Arrange
        Duration exactPreparationTime = Duration.ofSeconds(60);
        PreparationTimeMustBeAtLeastOneMinute rule = new PreparationTimeMustBeAtLeastOneMinute(exactPreparationTime);

        // Act
        boolean result = rule.isBroken();

        // Assert
        assertFalse(result, "Expected rule NOT to be broken when preparation time is exactly one minute.");
    }

    @Test
    void testIsBroken_WhenPreparationTimeIsMoreThanOneMinute_ShouldReturnFalse() {
        // Arrange
        Duration longPreparationTime = Duration.ofMinutes(2);
        PreparationTimeMustBeAtLeastOneMinute rule = new PreparationTimeMustBeAtLeastOneMinute(longPreparationTime);

        // Act
        boolean result = rule.isBroken();

        // Assert
        assertFalse(result, "Expected rule NOT to be broken when preparation time is more than one minute.");
    }

    @Test
    void testIsBroken_WhenPreparationTimeIsNull_ShouldReturnFalse() {
        // Arrange
        PreparationTimeMustBeAtLeastOneMinute rule = new PreparationTimeMustBeAtLeastOneMinute(null);

        // Act
        boolean result = rule.isBroken();

        // Assert
        assertFalse(result, "Expected rule NOT to be broken when preparation time is null.");
    }

    @Test
    void testGetMessage_ShouldReturnCorrectMessage() {
        // Arrange
        PreparationTimeMustBeAtLeastOneMinute rule = new PreparationTimeMustBeAtLeastOneMinute(Duration.ofSeconds(30));

        // Act
        String message = rule.getMessage();

        // Assert
        assertEquals("Preparation time must be at least one minute", message);
    }
}