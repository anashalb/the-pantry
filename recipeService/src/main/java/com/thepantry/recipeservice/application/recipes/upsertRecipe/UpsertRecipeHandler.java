package com.thepantry.recipeservice.application.recipes.upsertRecipe;

import com.thepantry.recipeservice.application.recipes.RecipeRequest;
import com.thepantry.recipeservice.application.recipes.IRecipeRepository;
import com.thepantry.recipeservice.application.recipes.RecipeIngredientRequest;
import com.thepantry.recipeservice.application.recipes.RecipeStepRequest;
import com.thepantry.recipeservice.domains.*;
import com.thepantry.recipeservice.domains.common.BusinessRuleException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class UpsertRecipeHandler {

    private final IRecipeRepository recipeRepository;
    private final IUnitConfiguration unitConfiguration;

    public UpsertRecipeHandler(
            IRecipeRepository recipeRepository,
            IUnitConfiguration unitConfiguration
    ) {
        this.recipeRepository = recipeRepository;
        this.unitConfiguration = unitConfiguration;
    }

    public UpsertRecipeDto handle(RecipeRequest recipeRequest, UUID recipeId)
            throws BusinessRuleException, RecipeCannotBeUpdatedException {

        List<RecipeIngredient> recipeIngredients = new ArrayList<>();
        for (RecipeIngredientRequest ingredient : recipeRequest.ingredients()) {
            recipeIngredients.add(
                    RecipeIngredient.of(new Ingredient(new IngredientId(ingredient.ingredientId())),
                            ingredient.preparationMethod(),
                            ingredient.quantity(),
                            MeasurementUnit.of(ingredient.measurementUnit(), unitConfiguration)));
        }

        List<RecipeStep> recipeSteps = new ArrayList<>();
        for (RecipeStepRequest step : recipeRequest.steps()) {
            recipeSteps.add(RecipeStep.of(step.instructions()));
        }

        if (recipeId == null) {
            Recipe recipe = Recipe.create(
                    recipeRequest.name(),
                    recipeRequest.description(),
                    Duration.ofMinutes(recipeRequest.cookingTimeMinutes()),
                    Duration.ofMinutes(recipeRequest.preparationTimeMinutes()),
                    Duration.ofMinutes(recipeRequest.readyInTimeMinutes()),
                    recipeRequest.servings(),
                    recipeIngredients,
                    recipeSteps,
                    unitConfiguration
            );

            UUID id = this.recipeRepository.createRecipe(recipe);
            return new UpsertRecipeDto(id);
        } else {
            Recipe recipe = Recipe.update(
                    recipeId,
                    recipeRequest.name(),
                    recipeRequest.description(),
                    Duration.ofMinutes(recipeRequest.cookingTimeMinutes()),
                    Duration.ofMinutes(recipeRequest.preparationTimeMinutes()),
                    Duration.ofMinutes(recipeRequest.readyInTimeMinutes()),
                    recipeRequest.servings(),
                    recipeIngredients,
                    recipeSteps
            );

            UUID id = this.recipeRepository.updateRecipe(recipe)
                    .orElseThrow(() -> new RecipeCannotBeUpdatedException(recipeId));
            return new UpsertRecipeDto(id);
        }
    }
}
