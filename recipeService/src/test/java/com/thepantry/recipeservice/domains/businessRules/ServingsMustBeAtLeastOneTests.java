package com.thepantry.recipeservice.domains.businessRules;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServingsMustBeAtLeastOneTests {

    @Test
    void testIsBroken_WhenServingsIsOne_ShouldReturnFalse() {
        ServingsMustBeAtLeastOne rule = new ServingsMustBeAtLeastOne((short) 1);
        assertFalse(rule.isBroken(), "Expected rule NOT to be broken when servings is exactly one.");
    }

    @Test
    void testIsBroken_WhenServingsIsZero_ShouldReturnTrue() {
        ServingsMustBeAtLeastOne rule = new ServingsMustBeAtLeastOne((short) 0);
        assertTrue(rule.isBroken(), "Expected rule to be broken when servings is zero.");
        assertEquals("A recipe should be for at least one serving", rule.getMessage());
    }

    @Test
    void testIsBroken_WhenServingsIsNull_ShouldReturnFalse() {
        ServingsMustBeAtLeastOne rule = new ServingsMustBeAtLeastOne(null);
        assertFalse(rule.isBroken(), "Expected rule NOT to be broken when servings is null.");
    }
}