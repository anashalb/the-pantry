package com.thepantry.recipeservice.domains.businessRules;

import com.thepantry.recipeservice.domains.common.IBusinessRule;

public class RecipeDescriptionLengthShouldBeAtLeastThreeChars implements IBusinessRule {

    private final short MIN_DESCRIPTION_CHARS_LENGTH = 3;

    private final String description;

    public RecipeDescriptionLengthShouldBeAtLeastThreeChars(String description) {
        this.description = description;
    }

    @Override
    public boolean isBroken() {
        return description != null && description.length() < MIN_DESCRIPTION_CHARS_LENGTH;
    }

    @Override
    public String getMessage() {
        return "Recipe description should be at least three characters long";
    }
}