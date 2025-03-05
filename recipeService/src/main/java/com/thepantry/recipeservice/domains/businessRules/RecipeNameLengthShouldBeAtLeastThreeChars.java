package com.thepantry.recipeservice.domains.businessRules;

import com.thepantry.recipeservice.domains.common.IBusinessRule;

public class RecipeNameLengthShouldBeAtLeastThreeChars implements IBusinessRule {

    private final short MIN_NAME_CHARS_LENGTH = 3;

    private final String name;

    public RecipeNameLengthShouldBeAtLeastThreeChars(String name) {
        this.name = name;
    }

    @Override
    public boolean isBroken() {
        return name != null && name.length() < MIN_NAME_CHARS_LENGTH;
    }

    @Override
    public String getMessage() {
        return "Recipe name should be at least three characters long";
    }
}
