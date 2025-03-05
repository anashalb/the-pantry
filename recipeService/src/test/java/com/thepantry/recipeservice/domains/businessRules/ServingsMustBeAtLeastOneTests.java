package com.thepantry.recipeservice.domains.businessRules;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServingsMustBeAtLeastOneTests {

    @Test
    void shouldNotBeBrokenWhenServingsIsOne() {
        ServingsMustBeAtLeastOne rule = new ServingsMustBeAtLeastOne((short) 1);
        assertFalse(rule.isBroken(), "Rule should not be broken when servings is 1");
    }

    @Test
    void shouldBeBrokenWhenServingsIsZero() {
        ServingsMustBeAtLeastOne rule = new ServingsMustBeAtLeastOne((short) 0);
        assertTrue(rule.isBroken(), "Rule should be broken when servings is 0");
        assertEquals("A recipe should be for at least one serving", rule.getMessage());
    }

    @Test
    void shouldNotBeBrokenWhenServingsIsNull() {
        ServingsMustBeAtLeastOne rule = new ServingsMustBeAtLeastOne(null);
        assertFalse(rule.isBroken(), "Rule should not be broken when servings is null");
    }
}