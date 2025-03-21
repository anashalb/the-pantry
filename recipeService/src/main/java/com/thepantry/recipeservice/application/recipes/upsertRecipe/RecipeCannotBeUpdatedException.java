package com.thepantry.recipeservice.application.recipes.upsertRecipe;

import lombok.Getter;

import java.util.UUID;

@Getter
public class RecipeCannotBeUpdatedException extends RuntimeException {

    private final UUID recipeId;

    public RecipeCannotBeUpdatedException(UUID recipeId) {
        super("Recipe with ID " + recipeId + " cannot be updated");
        this.recipeId = recipeId;
    }
}