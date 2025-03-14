package com.thepantry.recipeservice.domains;

import com.thepantry.recipeservice.domains.businessRules.RecipeIngredientQuantityShouldBeMoreThanZero;
import com.thepantry.recipeservice.domains.common.BusinessRuleException;
import com.thepantry.recipeservice.domains.common.DomainModel;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class RecipeIngredient extends DomainModel {

    Ingredient ingredient;
    String preparationMethod;
    double quantity;
    MeasurementUnit measurementUnit;

    private RecipeIngredient(
            Ingredient ingredient,
            String preparationMethod,
            double quantity,
            MeasurementUnit measurementUnit) {
        this.ingredient = ingredient;
        this.preparationMethod = preparationMethod;
        this.quantity = quantity;
        this.measurementUnit = measurementUnit;
    }

    public static RecipeIngredient of(
            Ingredient ingredient,
            String preparationMethod,
            double quantity,
            MeasurementUnit measurementUnit) throws BusinessRuleException {

        checkRule(new RecipeIngredientQuantityShouldBeMoreThanZero(quantity));
        return new RecipeIngredient(ingredient, preparationMethod, quantity, measurementUnit);
    }
}
