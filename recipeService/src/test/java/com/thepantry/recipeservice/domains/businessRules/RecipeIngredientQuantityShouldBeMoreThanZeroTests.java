package com.thepantry.recipeservice.domains.businessRules;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecipeIngredientQuantityShouldBeMoreThanZeroTests {

    @Test
    void testIsBroken_WhenQuantityIsZero_ShouldReturnTrue() {
        // Arrange
        RecipeIngredientQuantityShouldBeMoreThanZero rule = new RecipeIngredientQuantityShouldBeMoreThanZero(0.0);

        // Act
        boolean result = rule.isBroken();

        // Assert
        assertTrue(result, "Expected rule to be broken when ingredient quantity is zero.");
    }

    @Test
    void testIsBroken_WhenQuantityIsNegative_ShouldReturnTrue() {
        // Arrange
        RecipeIngredientQuantityShouldBeMoreThanZero rule = new RecipeIngredientQuantityShouldBeMoreThanZero(-1.0);

        // Act
        boolean result = rule.isBroken();

        // Assert
        assertTrue(result, "Expected rule to be broken when ingredient quantity is negative.");
    }

    @Test
    void testIsBroken_WhenQuantityIsMoreThanZero_ShouldReturnFalse() {
        // Arrange
        RecipeIngredientQuantityShouldBeMoreThanZero rule = new RecipeIngredientQuantityShouldBeMoreThanZero(0.01);

        // Act
        boolean result = rule.isBroken();

        // Assert
        assertFalse(result, "Expected rule NOT to be broken when ingredient quantity is more than zero.");
    }

    @Test
    void testGetMessage_ShouldReturnCorrectMessage() {
        // Arrange
        RecipeIngredientQuantityShouldBeMoreThanZero rule = new RecipeIngredientQuantityShouldBeMoreThanZero(0);

        // Act
        String message = rule.getMessage();

        // Assert
        assertEquals("Recipe ingredient quantity should be more than 0", message);
    }
}