package com.thepantry.recipeservice.application.recipes.createRecipe;

import com.thepantry.recipeservice.application.recipes.IRecipeRepository;
import com.thepantry.recipeservice.domains.Recipe;
import com.thepantry.recipeservice.domains.common.BusinessRuleException;
import com.thepantry.recipeservice.infrastructure.persistence.entities.RecipeEntity;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class CreateRecipeHandler {

    private final IRecipeRepository recipeRepository;

    public CreateRecipeHandler(
            IRecipeRepository recipeRepository
    ) {
        this.recipeRepository = recipeRepository;
    }

    private static RecipeEntity getRecipeEntity(Recipe recipe) {
        RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setRecipeId(recipe.getRecipeId().getValue());
        recipeEntity.setName(recipe.getName());
        recipeEntity.setDescription(recipe.getDescription());
        recipeEntity.setCookingTimeMinutes(recipe.getCookingTimeMinutes().toMinutes());
        recipeEntity.setPreparationTimeMinutes(recipe.getPreparationTimeMinutes().toMinutes());
        recipeEntity.setReadyInTimeMinutes(recipe.getReadyInTimeMinutes().toMinutes());
        recipeEntity.setServings(recipe.getServings());
        return recipeEntity;
    }

    public CreateRecipeDto handle(CreateRecipeRequest createRecipeRequest) throws BusinessRuleException {

        Recipe recipe = Recipe.create(
                createRecipeRequest.name(),
                createRecipeRequest.description(),
                Duration.ofMinutes(createRecipeRequest.cookingTimeMinutes()),
                Duration.ofMinutes(createRecipeRequest.preparationTimeMinutes()),
                Duration.ofMinutes(createRecipeRequest.readyInTimeMinutes()),
                createRecipeRequest.servings()
        );

        RecipeEntity recipeEntity = getRecipeEntity(recipe);
        this.recipeRepository.createRecipe(recipeEntity);
        return new CreateRecipeDto(recipeEntity.getRecipeId());
    }
}