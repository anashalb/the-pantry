package com.thepantry.recipeservice.domains.businessRules;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecipeDescriptionLengthShouldBeAtLeastThreeCharsTests {

    @Test
    void testIsBroken_DescriptionTooShort() {
        RecipeDescriptionLengthShouldBeAtLeastThreeChars rule = new RecipeDescriptionLengthShouldBeAtLeastThreeChars("Hi");

        assertTrue(rule.isBroken(), "Expected rule to be broken when description length is less than 3.");
    }

    @Test
    void testIsBroken_DescriptionExactlyThreeChars() {
        RecipeDescriptionLengthShouldBeAtLeastThreeChars rule = new RecipeDescriptionLengthShouldBeAtLeastThreeChars("Hey");

        assertFalse(rule.isBroken(), "Expected rule to NOT be broken when description length is 3 or more.");
    }

    @Test
    void testIsBroken_DescriptionLongerThanThreeChars() {
        RecipeDescriptionLengthShouldBeAtLeastThreeChars rule = new RecipeDescriptionLengthShouldBeAtLeastThreeChars("Hello");

        assertFalse(rule.isBroken(), "Expected rule to NOT be broken when description length is greater than 3.");
    }

    @Test
    void testIsBroken_NullDescription() {
        RecipeDescriptionLengthShouldBeAtLeastThreeChars rule = new RecipeDescriptionLengthShouldBeAtLeastThreeChars(null);

        assertFalse(rule.isBroken(), "Expected rule to NOT be broken when description is null.");
    }

    @Test
    void testGetMessage() {
        RecipeDescriptionLengthShouldBeAtLeastThreeChars rule = new RecipeDescriptionLengthShouldBeAtLeastThreeChars("Hi");

        assertEquals("Recipe description should be at least three characters long", rule.getMessage());
    }
}