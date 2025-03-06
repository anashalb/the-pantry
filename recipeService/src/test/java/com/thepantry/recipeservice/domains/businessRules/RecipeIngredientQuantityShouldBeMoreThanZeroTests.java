package com.thepantry.recipeservice.domains.businessRules;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecipeIngredientQuantityShouldBeMoreThanZeroTests {

    @Test
    void testIsBroken_WhenQuantityIsZero_ShouldReturnTrue() {
        RecipeIngredientQuantityShouldBeMoreThanZero rule = new RecipeIngredientQuantityShouldBeMoreThanZero(0.0);

        assertTrue(rule.isBroken(), "Expected rule to be broken when ingredient quantity is zero.");
    }

    @Test
    void testIsBroken_WhenQuantityIsNegative_ShouldReturnTrue() {
        RecipeIngredientQuantityShouldBeMoreThanZero rule = new RecipeIngredientQuantityShouldBeMoreThanZero(-1.0);

        assertTrue(rule.isBroken(), "Expected rule to be broken when ingredient quantity is negative.");
    }

    @Test
    void testIsBroken_WhenQuantityIsMoreThanZero_ShouldReturnFalse() {
        RecipeIngredientQuantityShouldBeMoreThanZero rule = new RecipeIngredientQuantityShouldBeMoreThanZero(0.01);

        assertFalse(rule.isBroken(), "Expected rule NOT to be broken when ingredient quantity is more than zero.");
    }

    @Test
    void testGetMessage_ShouldReturnCorrectMessage() {
        RecipeIngredientQuantityShouldBeMoreThanZero rule = new RecipeIngredientQuantityShouldBeMoreThanZero(0);

        assertEquals("Recipe ingredient quantity should be more than 0", rule.getMessage());
    }
}