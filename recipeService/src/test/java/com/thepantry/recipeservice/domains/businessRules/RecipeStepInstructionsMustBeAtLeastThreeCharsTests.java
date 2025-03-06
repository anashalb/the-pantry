package com.thepantry.recipeservice.domains.businessRules;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecipeStepInstructionsMustBeAtLeastThreeCharsTests {

    @Test
    void testIsBroken_WhenStepInstructionsAreTooShort_ShouldReturnTrue() {
        String shortInstruction = "Hi";
        RecipeStepInstructionsMustBeAtLeastThreeChars rule = new RecipeStepInstructionsMustBeAtLeastThreeChars(shortInstruction);

        assertTrue(rule.isBroken(), "Expected rule to be broken when step instructions are shorter than three characters.");
        assertEquals("Step instructions should be at least three characters long", rule.getMessage());
    }

    @Test
    void testIsBroken_WhenStepInstructionsAreThreeOrMoreCharacters_ShouldReturnFalse() {
        String validInstruction = "Mix";
        RecipeStepInstructionsMustBeAtLeastThreeChars rule = new RecipeStepInstructionsMustBeAtLeastThreeChars(validInstruction);

        assertFalse(rule.isBroken(), "Expected rule NOT to be broken when step instructions have three or more characters.");
    }

    @Test
    void testIsBroken_WhenStepInstructionsAreNull_ShouldReturnFalse() {
        RecipeStepInstructionsMustBeAtLeastThreeChars rule = new RecipeStepInstructionsMustBeAtLeastThreeChars(null);

        assertFalse(rule.isBroken(), "Expected rule NOT to be broken when step instructions are null.");
    }
}