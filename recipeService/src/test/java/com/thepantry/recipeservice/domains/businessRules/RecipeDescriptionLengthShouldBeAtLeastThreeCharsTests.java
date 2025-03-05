package com.thepantry.recipeservice.domains.businessRules;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecipeDescriptionLengthShouldBeAtLeastThreeCharsTests {

    @Test
    void testIsBroken_DescriptionTooShort() {
        // Arrange
        RecipeDescriptionLengthShouldBeAtLeastThreeChars rule = new RecipeDescriptionLengthShouldBeAtLeastThreeChars("Hi");

        // Act
        boolean result = rule.isBroken();

        // Assert
        assertTrue(result, "Expected rule to be broken when description length is less than 3.");
    }

    @Test
    void testIsBroken_DescriptionExactlyThreeChars() {
        // Arrange
        RecipeDescriptionLengthShouldBeAtLeastThreeChars rule = new RecipeDescriptionLengthShouldBeAtLeastThreeChars("Hey");

        // Act
        boolean result = rule.isBroken();

        // Assert
        assertFalse(result, "Expected rule to NOT be broken when description length is 3 or more.");
    }

    @Test
    void testIsBroken_DescriptionLongerThanThreeChars() {
        // Arrange
        RecipeDescriptionLengthShouldBeAtLeastThreeChars rule = new RecipeDescriptionLengthShouldBeAtLeastThreeChars("Hello");

        // Act
        boolean result = rule.isBroken();

        // Assert
        assertFalse(result, "Expected rule to NOT be broken when description length is greater than 3.");
    }

    @Test
    void testIsBroken_NullDescription() {
        // Arrange
        RecipeDescriptionLengthShouldBeAtLeastThreeChars rule = new RecipeDescriptionLengthShouldBeAtLeastThreeChars(null);

        // Act
        boolean result = rule.isBroken();

        // Assert
        assertFalse(result, "Expected rule to NOT be broken when description is null.");
    }

    @Test
    void testGetMessage() {
        // Arrange
        RecipeDescriptionLengthShouldBeAtLeastThreeChars rule = new RecipeDescriptionLengthShouldBeAtLeastThreeChars("Hi");

        // Act
        String message = rule.getMessage();

        // Assert
        assertEquals("Recipe description should be at least three characters long", message);
    }
}