package com.thepantry.recipeservice.domains.businessRules;

import com.thepantry.recipeservice.domains.common.IBusinessRule;

public class RecipeIngredientQuantityShouldBeMoreThanZero implements IBusinessRule {

    private final double MIN_QUANTITY = 0.0;

    private final double quantity;

    public RecipeIngredientQuantityShouldBeMoreThanZero(double quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean isBroken() {
        return quantity <= MIN_QUANTITY;
    }

    @Override
    public String getMessage() {
        return "Recipe ingredient quantity should be more than 0";
    }
}