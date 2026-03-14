package com.thepantry.recipeservice.application.recipes.getRecipeDetails;

import com.thepantry.recipeservice.application.recipes.IRecipeRepository;
import org.springframework.stereotype.Component;

@Component
public class GetRecipeHandler {

    private final IRecipeRepository recipeRepository;

    public GetRecipeHandler(IRecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public RecipeDetailsDto handle(GetRecipeDto getRecipeDto) throws RecipeNotFoundException {
        return recipeRepository.getRecipeDetailsByRecipeId(getRecipeDto.getRecipeId())
                .orElseThrow(() -> new RecipeNotFoundException(getRecipeDto.getRecipeId()));
    }
}
