package com.thepantry.recipeservice.application.recipes.getRecipeDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RecipeIngredientDto {

    private Long ingredient;
    private String preparationMethod;
    private double quantity;
    private String measurementUnit;
}