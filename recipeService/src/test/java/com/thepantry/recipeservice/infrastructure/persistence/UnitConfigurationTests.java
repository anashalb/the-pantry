package com.thepantry.recipeservice.infrastructure.persistence;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitConfigurationTests {

    private final UnitConfiguration unitConfiguration = new UnitConfiguration();

    @Test
    public void testGetUnitInfo_ShouldReturnUnitInfo_WhenImperialUnitExists() {

        String imperialUnit = unitConfiguration.getAllImperialUnits().stream().findFirst().get();
        UnitConfiguration.UnitInfo unitInfo = unitConfiguration.getUnitInfo(imperialUnit);
        assertEquals(imperialUnit, unitInfo.shortName());
    }

    @Test
    public void testGetUnitInfo_ShouldReturnUnitInfo_WhenMetricUnitExists() {

        String metricUnit = unitConfiguration.getAllMetricUnits().stream().findFirst().get();
        UnitConfiguration.UnitInfo unitInfo = unitConfiguration.getUnitInfo(metricUnit);
        assertEquals(metricUnit, unitInfo.shortName());
    }

    @Test
    public void testGetUnitInfo_ShouldReturnNull_WhenUnitDoesNotExist() {
        assertNull(unitConfiguration.getUnitInfo("invalidUnit"));
    }

    @Test
    public void testIsValidUnit_ShouldReturnTrue_WhenImperialUnitExists() {

        String imperialUnit = unitConfiguration.getAllImperialUnits().stream().findFirst().get();
        assertTrue(unitConfiguration.isValidUnit(imperialUnit));
    }

    @Test
    public void testIsValidUnit_ShouldReturnTrue_WhenMetricUnitExists() {

        String metricUnit = unitConfiguration.getAllMetricUnits().stream().findFirst().get();
        assertTrue(unitConfiguration.isValidUnit(metricUnit));
    }

    @Test
    public void testIsValidUnit_ShouldReturnFalse_WhenUnitDoesNotExist() {
        assertFalse(unitConfiguration.isValidUnit("invalidUnit"));
    }
}