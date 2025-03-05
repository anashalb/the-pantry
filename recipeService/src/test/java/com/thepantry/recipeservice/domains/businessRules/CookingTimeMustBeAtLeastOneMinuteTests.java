package com.thepantry.recipeservice.domains.businessRules;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

class CookingTimeMustBeAtLeastOneMinuteTests {

    @Test
    void testIsBroken_WhenCookingTimeIsLessThanOneMinute_ShouldReturnTrue() {
        // Arrange
        Duration shortCookingTime = Duration.ofSeconds(30);
        CookingTimeMustBeAtLeastOneMinute rule = new CookingTimeMustBeAtLeastOneMinute(shortCookingTime);

        // Act
        boolean result = rule.isBroken();

        // Assert
        assertTrue(result, "Expected rule to be broken when cooking time is less than one minute.");
    }

    @Test
    void testIsBroken_WhenCookingTimeIsExactlyOneMinute_ShouldReturnFalse() {
        // Arrange
        Duration exactCookingTime = Duration.ofSeconds(60);
        CookingTimeMustBeAtLeastOneMinute rule = new CookingTimeMustBeAtLeastOneMinute(exactCookingTime);

        // Act
        boolean result = rule.isBroken();

        // Assert
        assertFalse(result, "Expected rule NOT to be broken when cooking time is exactly one minute.");
    }

    @Test
    void testIsBroken_WhenCookingTimeIsMoreThanOneMinute_ShouldReturnFalse() {
        // Arrange
        Duration longCookingTime = Duration.ofMinutes(2);
        CookingTimeMustBeAtLeastOneMinute rule = new CookingTimeMustBeAtLeastOneMinute(longCookingTime);

        // Act
        boolean result = rule.isBroken();

        // Assert
        assertFalse(result, "Expected rule NOT to be broken when cooking time is more than one minute.");
    }

    @Test
    void testIsBroken_WhenCookingTimeIsNull_ShouldReturnFalse() {
        // Arrange
        CookingTimeMustBeAtLeastOneMinute rule = new CookingTimeMustBeAtLeastOneMinute(null);

        // Act
        boolean result = rule.isBroken();

        // Assert
        assertFalse(result, "Expected rule NOT to be broken when cooking time is null.");
    }

    @Test
    void testGetMessage_ShouldReturnCorrectMessage() {
        // Arrange
        CookingTimeMustBeAtLeastOneMinute rule = new CookingTimeMustBeAtLeastOneMinute(Duration.ofSeconds(30));

        // Act
        String message = rule.getMessage();

        // Assert
        assertEquals("Cooking time must be at least one minute", message);
    }
}