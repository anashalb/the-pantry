package com.thepantry.recipeservice.domains;

import com.thepantry.recipeservice.domains.common.TypedIdValueBase;

import java.util.UUID;

public class RecipeId extends TypedIdValueBase {

    public RecipeId() {
        super();
    }

    public RecipeId(UUID value) {
        super(value);
    }
}
