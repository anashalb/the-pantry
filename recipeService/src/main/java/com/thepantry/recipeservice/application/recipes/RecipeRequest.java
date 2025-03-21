package com.thepantry.recipeservice.application.recipes;

import java.util.List;

public record RecipeRequest(
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
