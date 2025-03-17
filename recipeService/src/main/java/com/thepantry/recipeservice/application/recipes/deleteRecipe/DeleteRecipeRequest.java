package com.thepantry.recipeservice.application.recipes.deleteRecipe;

import java.util.UUID;

public record DeleteRecipeRequest(UUID recipeId) {
}
