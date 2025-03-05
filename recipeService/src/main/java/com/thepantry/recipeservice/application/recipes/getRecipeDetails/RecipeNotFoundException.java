package com.thepantry.recipeservice.application.recipes.getRecipeDetails;

import lombok.Getter;

import java.util.UUID;

@Getter
public class RecipeNotFoundException extends RuntimeException {

    private final UUID recipeId;

    public RecipeNotFoundException(UUID recipeId) {
        super("Recipe with ID " + recipeId + " not found");
        this.recipeId = recipeId;
    }
}
