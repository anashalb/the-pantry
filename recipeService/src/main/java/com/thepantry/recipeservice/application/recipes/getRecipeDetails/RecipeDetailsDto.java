package com.thepantry.recipeservice.application.recipes.getRecipeDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class RecipeDetailsDto {

    private UUID recipeId;
    private String name;
    private String description;
    private Long cookingTimeMinutes;
    private Long preparationTimeMinutes;
    private Long ReadyInTimeMinutes;
    private Short servings;
    private List<RecipeIngredientDto> ingredients;
    private List<RecipeStepDto> steps;
}
