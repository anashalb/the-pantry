package com.thepantry.recipeservice.application.recipes.createRecipe;

public record CreateRecipeRequest(
        String name,
        String description,
        Long readyInTimeMinutes,
        Long cookingTimeMinutes,
        Long preparationTimeMinutes,
        Short servings) {
}
