package com.thepantry.recipeservice.application.recipes.getRecipeDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RecipeStepDto {

    private int stepNumber;
    private String instructions;
}
