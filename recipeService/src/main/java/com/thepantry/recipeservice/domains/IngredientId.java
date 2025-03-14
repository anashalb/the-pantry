package com.thepantry.recipeservice.domains;

import com.thepantry.recipeservice.domains.common.TypedIdValueBase;

import java.util.UUID;

public class IngredientId extends TypedIdValueBase {
    public IngredientId(UUID value) {
        super(value);
    }
}
