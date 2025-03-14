package com.thepantry.recipeservice.domains;

import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface IUnitConfiguration {

    /**
     * Retrieves unit information based on the provided unit identifier.
     *
     * @param unit The unit short name.
     * @return UnitInfo object containing details about the unit.
     */
    UnitInfo getUnitInfo(String unit);

    /**
     * Checks if a given unit is valid within the defined system.
     *
     * @param unit The unit short name.
     * @return true if the unit exists, false otherwise.
     */
    boolean isValidUnit(String unit);

    /**
     * Retrieves all metric units.
     *
     * @return A set of metric unit short names.
     */
    Set<String> getAllMetricUnits();

    /**
     * Retrieves all imperial/US customary units.
     *
     * @return A set of imperial/US customary unit short names.
     */
    Set<String> getAllImperialUnits();

    enum UnitType {
        VOLUME, WEIGHT
    }

    enum UnitSystem {
        METRIC, IMPERIAL
    }

    record UnitInfo(
            String shortName,
            String longName,
            UnitType type,
            UnitSystem system) {
    }
}
