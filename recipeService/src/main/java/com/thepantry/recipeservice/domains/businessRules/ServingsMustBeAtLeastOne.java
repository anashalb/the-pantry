package com.thepantry.recipeservice.domains.businessRules;

import com.thepantry.recipeservice.domains.common.IBusinessRule;

public class ServingsMustBeAtLeastOne implements IBusinessRule {

    private final short MIN_SERVIINGS = 1;

    private final Short servings;

    public ServingsMustBeAtLeastOne(Short servings) {
        this.servings = servings;
    }

    @Override
    public boolean isBroken() {
        return servings != null && servings < MIN_SERVIINGS;
    }

    @Override
    public String getMessage() {
        return "A recipe should be for at least one serving";
    }
}
