package com.thepantry.recipeservice.application.recipes.deleteRecipe;

import com.thepantry.recipeservice.application.recipes.IRecipeRepository;
import org.springframework.stereotype.Component;

@Component
public class DeleteRecipeHandler {

    private final IRecipeRepository recipeRepository;

    public DeleteRecipeHandler(IRecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public boolean handle(DeleteRecipeRequest deleteRecipeRequest) {
        return recipeRepository.deleteRecipe(deleteRecipeRequest.recipeId());
    }
}
