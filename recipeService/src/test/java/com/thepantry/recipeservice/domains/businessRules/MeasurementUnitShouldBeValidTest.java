package com.thepantry.recipeservice.domains.businessRules;

import com.thepantry.recipeservice.domains.IUnitConfiguration;
import com.thepantry.recipeservice.infrastructure.persistence.UnitConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MeasurementUnitShouldBeValidTest {

    private IUnitConfiguration unitConfiguration;

    @BeforeEach
    void setUp() {
        this.unitConfiguration = mock(UnitConfiguration.class);
        when(this.unitConfiguration.isValidUnit("kg")).thenReturn(true);
        when(this.unitConfiguration.isValidUnit("shoes")).thenReturn(false);
    }

    @Test
    void testIsBroken_WhenMeasurementUnitIsValid_ShouldReturnTrue() {
        MeasurementUnitShouldBeValid measurementUnitShouldBeValid = new MeasurementUnitShouldBeValid(
                "kg",
                this.unitConfiguration);

        assertFalse(measurementUnitShouldBeValid.isBroken());
    }

    @Test
    void testIsBroken_WhenMeasurementUnitIsNotValid_ShouldReturnFalse() {
        MeasurementUnitShouldBeValid measurementUnitShouldBeValid = new MeasurementUnitShouldBeValid(
                "shoes",
                this.unitConfiguration);

        assertTrue(measurementUnitShouldBeValid.isBroken());
    }

    @Test
    void testGetMessage_ShouldReturnCorrectMessage() {
        MeasurementUnitShouldBeValid measurementUnitShouldBeValid = new MeasurementUnitShouldBeValid(
                "shoes",
                this.unitConfiguration);

        assertEquals("Measurement unit shoes is invalid", measurementUnitShouldBeValid.getMessage());
    }

}