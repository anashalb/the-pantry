package com.thepantry.recipeservice.domains.businessRules;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecipeNameLengthShouldBeAtLeastThreeCharsTests {

    @Test
    void testIsBroken_WhenNameIsTooShort_ShouldReturnTrue() {
        RecipeNameLengthShouldBeAtLeastThreeChars rule = new RecipeNameLengthShouldBeAtLeastThreeChars("ab");
        assertTrue(rule.isBroken(), "Expected rule to be broken when name is shorter than three characters.");
    }

    @Test
    void testIsBroken_WhenNameIsValid_ShouldReturnFalse() {
        RecipeNameLengthShouldBeAtLeastThreeChars rule = new RecipeNameLengthShouldBeAtLeastThreeChars("abc");
        assertFalse(rule.isBroken(), "Expected rule NOT to be broken when name has three or more characters.");
    }

    @Test
    void testIsBroken_WhenNameIsLongEnough_ShouldReturnFalse() {
        RecipeNameLengthShouldBeAtLeastThreeChars rule = new RecipeNameLengthShouldBeAtLeastThreeChars("long name");
        assertFalse(rule.isBroken(), "Expected rule NOT to be broken when name is long enough.");
    }

    @Test
    void testIsBroken_WhenNameIsExactlyThreeChars_ShouldReturnFalse() {
        RecipeNameLengthShouldBeAtLeastThreeChars rule = new RecipeNameLengthShouldBeAtLeastThreeChars("abc");
        assertFalse(rule.isBroken(), "Expected rule NOT to be broken when name is exactly three characters.");
    }

    @Test
    void testIsBroken_WhenNameIsNull_ShouldReturnFalse() {
        RecipeNameLengthShouldBeAtLeastThreeChars rule = new RecipeNameLengthShouldBeAtLeastThreeChars(null);
        assertFalse(rule.isBroken(), "Expected rule NOT to be broken when name is null as it is not explicitly checked.");
    }

    @Test
    void testGetMessage_WhenNameIsTooShort_ShouldReturnExpectedMessage() {
        RecipeNameLengthShouldBeAtLeastThreeChars rule = new RecipeNameLengthShouldBeAtLeastThreeChars("ab");
        assertEquals("Recipe name should be at least three characters long", rule.getMessage());
    }
}