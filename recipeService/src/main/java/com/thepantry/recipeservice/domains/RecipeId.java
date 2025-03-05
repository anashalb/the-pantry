package com.thepantry.recipeservice.domains;

import com.thepantry.recipeservice.domains.common.TypedIdValueBase;

import java.util.UUID;

public class RecipeId extends TypedIdValueBase {

    protected RecipeId() {
        super();
    }

    protected RecipeId(UUID value) {
        super(value);
    }
}
