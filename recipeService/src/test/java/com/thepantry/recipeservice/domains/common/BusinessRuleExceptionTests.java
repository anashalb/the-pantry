package com.thepantry.recipeservice.domains.common;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BusinessRuleExceptionTests {

    @Test
    void testConstructor_WhenRuleIsBroken_ShouldStoreBrokenRuleAndMessage() {
        IBusinessRule mockRule = mock(IBusinessRule.class);
        when(mockRule.getMessage()).thenReturn("Rule violated");

        BusinessRuleException exception = new BusinessRuleException(mockRule);

        assertEquals(mockRule, exception.getBrokenRule(), "Expected exception to store the broken rule.");
        assertEquals("Rule violated", exception.getDetails(), "Expected exception details to match the rule message.");
        assertEquals("Rule violated", exception.getMessage(), "Expected exception message to match the rule message.");
    }

    @Test
    void testToString_WhenRuleIsBroken_ShouldReturnMessage() {
        IBusinessRule mockRule = mock(IBusinessRule.class);
        when(mockRule.getMessage()).thenReturn("Another rule violation");

        BusinessRuleException exception = new BusinessRuleException(mockRule);

        assertEquals("Another rule violation", exception.toString(), "Expected toString() to return the rule violation message.");
    }
}