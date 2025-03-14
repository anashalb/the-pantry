package com.thepantry.recipeservice.application.recipes.createRecipe;

import java.util.UUID;

public record RecipeIngredientRequest(
        UUID ingredientId,
        double quantity,
        String measurementUnit,
        String preparationMethod
) {
}
