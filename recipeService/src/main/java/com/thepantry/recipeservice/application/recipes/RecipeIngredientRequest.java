package com.thepantry.recipeservice.application.recipes;

import java.util.UUID;

public record RecipeIngredientRequest(
        UUID ingredientId,
        Double quantity,
        String measurementUnit,
        String preparationMethod
) {
}
