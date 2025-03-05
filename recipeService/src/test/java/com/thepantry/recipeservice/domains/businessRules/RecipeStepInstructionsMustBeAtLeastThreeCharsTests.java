package com.thepantry.recipeservice.domains.businessRules;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecipeStepInstructionsMustBeAtLeastThreeCharsTests {

    @Test
    void shouldReturnBrokenWhenStepInstructionsAreTooShort() {
        // Given
        String shortInstruction = "Hi"; // Less than 3 characters
        RecipeStepInstructionsMustBeAtLeastThreeChars rule = new RecipeStepInstructionsMustBeAtLeastThreeChars(shortInstruction);

        // When
        boolean result = rule.isBroken();

        // Then
        assertTrue(result);
        assertEquals("Step instructions should be at least three characters long", rule.getMessage());
    }

    @Test
    void shouldNotBeBrokenWhenStepInstructionsAreThreeOrMoreCharacters() {
        // Given
        String validInstruction = "Mix";
        RecipeStepInstructionsMustBeAtLeastThreeChars rule = new RecipeStepInstructionsMustBeAtLeastThreeChars(validInstruction);

        // When
        boolean result = rule.isBroken();

        // Then
        assertFalse(result);
    }

    @Test
    void shouldNotBeBrokenWhenStepInstructionsAreNull() {
        // Given
        RecipeStepInstructionsMustBeAtLeastThreeChars rule = new RecipeStepInstructionsMustBeAtLeastThreeChars(null);

        // When
        boolean result = rule.isBroken();

        // Then
        assertFalse(result);
    }
}