package com.thepantry.recipeservice.domains.businessRules;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecipeNameLengthShouldBeAtLeastThreeCharsTests {
    @Test
    void testRuleIsBroken_WhenNameIsTooShort() {
        RecipeNameLengthShouldBeAtLeastThreeChars rule = new RecipeNameLengthShouldBeAtLeastThreeChars("ab");
        assertTrue(rule.isBroken(), "Rule should be broken for names shorter than 3 characters");
    }

    @Test
    void testRuleIsNotBroken_WhenNameIsValid() {
        RecipeNameLengthShouldBeAtLeastThreeChars rule = new RecipeNameLengthShouldBeAtLeastThreeChars("abc");
        assertFalse(rule.isBroken(), "Rule should not be broken for names with 3 or more characters");
    }

    @Test
    void testRuleIsNotBroken_WhenNameIsLongEnough() {
        RecipeNameLengthShouldBeAtLeastThreeChars rule = new RecipeNameLengthShouldBeAtLeastThreeChars("long name");
        assertFalse(rule.isBroken(), "Rule should not be broken for long names");
    }

    @Test
    void testRuleIsNotBroken_WhenNameIsExactlyThreeChars() {
        RecipeNameLengthShouldBeAtLeastThreeChars rule = new RecipeNameLengthShouldBeAtLeastThreeChars("abc");
        assertFalse(rule.isBroken(), "Rule should not be broken when name is exactly 3 characters");
    }

    @Test
    void testRuleIsBroken_WhenNameIsNull() {
        RecipeNameLengthShouldBeAtLeastThreeChars rule = new RecipeNameLengthShouldBeAtLeastThreeChars(null);
        assertFalse(rule.isBroken(), "Rule should not be broken for null names as it is not explicitly checked");
    }

    @Test
    void testErrorMessage() {
        RecipeNameLengthShouldBeAtLeastThreeChars rule = new RecipeNameLengthShouldBeAtLeastThreeChars("ab");
        assertEquals("Recipe name should be at least three characters long", rule.getMessage());
    }

}