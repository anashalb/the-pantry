package com.thepantry.recipeservice.application.recipes.upsertRecipe;

import java.util.UUID;

public record UpsertRecipeDto(
        UUID recipeId
) {
}