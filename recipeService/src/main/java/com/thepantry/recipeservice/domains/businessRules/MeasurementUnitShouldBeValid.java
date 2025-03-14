package com.thepantry.recipeservice.domains.businessRules;

import com.thepantry.recipeservice.domains.IUnitConfiguration;
import com.thepantry.recipeservice.domains.common.IBusinessRule;

public class MeasurementUnitShouldBeValid implements IBusinessRule {

    private final String measurementUnit;
    private final IUnitConfiguration unitConfiguration;

    public MeasurementUnitShouldBeValid(String measurementUnit, IUnitConfiguration unitConfiguration) {
        this.measurementUnit = measurementUnit;
        this.unitConfiguration = unitConfiguration;
    }

    @Override
    public boolean isBroken() {
        return !unitConfiguration.isValidUnit(measurementUnit);
    }

    @Override
    public String getMessage() {
        return "Measurement unit " + measurementUnit + " is invalid";
    }
}
