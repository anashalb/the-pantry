package com.thepantry.recipeservice.domains;

import com.thepantry.recipeservice.domains.businessRules.MeasurementUnitShouldBeValid;
import com.thepantry.recipeservice.domains.common.BusinessRuleException;
import com.thepantry.recipeservice.domains.common.DomainModel;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class MeasurementUnit extends DomainModel {

    String name;

    private MeasurementUnit(String name) {
        this.name = name;
    }

    public static MeasurementUnit of(String name, IUnitConfiguration unitConfiguration) throws BusinessRuleException {
        checkRule(new MeasurementUnitShouldBeValid(name, unitConfiguration));
        return new MeasurementUnit(name);
    }
}
