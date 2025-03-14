package com.thepantry.recipeservice.application.recipes.createRecipe;

import java.util.List;

public record CreateRecipeRequest(
        String name,
        String description,
        Long readyInTimeMinutes,
        Long cookingTimeMinutes,
        Long preparationTimeMinutes,
        Short servings,
        List<RecipeIngredientRequest> ingredients,
        List<RecipeStepRequest> steps
) {
}
