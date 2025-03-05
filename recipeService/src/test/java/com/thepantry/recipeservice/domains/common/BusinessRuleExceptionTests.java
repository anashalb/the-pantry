package com.thepantry.recipeservice.domains.common;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BusinessRuleExceptionTests {

    @Test
    void shouldStoreBrokenRuleAndMessage() {
        // Given
        IBusinessRule mockRule = mock(IBusinessRule.class);
        when(mockRule.getMessage()).thenReturn("Rule violated");

        // When
        BusinessRuleException exception = new BusinessRuleException(mockRule);

        // Then
        assertEquals(mockRule, exception.getBrokenRule());
        assertEquals("Rule violated", exception.getDetails());
        assertEquals("Rule violated", exception.getMessage());
    }

    @Test
    void shouldReturnMessageAsToString() {
        // Given
        IBusinessRule mockRule = mock(IBusinessRule.class);
        when(mockRule.getMessage()).thenReturn("Another rule violation");

        // When
        BusinessRuleException exception = new BusinessRuleException(mockRule);

        // Then
        assertEquals("Another rule violation", exception.toString());
    }
}