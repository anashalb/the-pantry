package com.thepantry.recipeservice.application.recipes.upsertRecipe;

import com.thepantry.recipeservice.application.recipes.RecipeRequest;
import com.thepantry.recipeservice.application.recipes.IRecipeRepository;
import com.thepantry.recipeservice.application.recipes.RecipeIngredientRequest;
import com.thepantry.recipeservice.application.recipes.RecipeStepRequest;
import com.thepantry.recipeservice.domains.*;
import com.thepantry.recipeservice.domains.common.BusinessRuleException;
import com.thepantry.recipeservice.infrastructure.persistence.entities.RecipeEntity;
import com.thepantry.recipeservice.infrastructure.persistence.entities.RecipeIngredientEntity;
import com.thepantry.recipeservice.infrastructure.persistence.entities.RecipeStepEntity;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

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

    private static RecipeEntity getRecipeEntity(Recipe recipe) {

        RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setRecipeId(recipe.getRecipeId().getValue());
        recipeEntity.setName(recipe.getName());
        recipeEntity.setDescription(recipe.getDescription());
        recipeEntity.setCookingTimeMinutes(recipe.getCookingTimeMinutes().toMinutes());
        recipeEntity.setPreparationTimeMinutes(recipe.getPreparationTimeMinutes().toMinutes());
        recipeEntity.setReadyInTimeMinutes(recipe.getReadyInTimeMinutes().toMinutes());
        recipeEntity.setServings(recipe.getServings());

        List<RecipeIngredientEntity> recipeIngredientEntities = recipe.getIngredients().stream().map(
                recipeIngredient -> {
                    RecipeIngredientEntity entity = new RecipeIngredientEntity();
                    entity.setIngredientId(recipeIngredient.getIngredient().getId().getValue());
                    entity.setQuantity(recipeIngredient.getQuantity());
                    entity.setPreparationMethod(recipeIngredient.getPreparationMethod());
                    entity.setMeasurementUnit(recipeIngredient.getMeasurementUnit().getName());
                    entity.setRecipe(recipeEntity);
                    return entity;
                }
        ).toList();

        recipeEntity.setRecipeIngredients(recipeIngredientEntities);

        List<RecipeStepEntity> recipeStepEntities = IntStream
                .range(0, recipe.getSteps().size())
                .mapToObj(i -> {
                    RecipeStep recipeStep = recipe.getSteps().get(i);
                    RecipeStepEntity entity = new RecipeStepEntity();
                    entity.setInstructions(recipeStep.getInstructions());
                    entity.setStepNumber(i);
                    entity.setRecipe(recipeEntity);
                    return entity;
                }).toList();

        recipeEntity.setRecipeSteps(recipeStepEntities);
        return recipeEntity;
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

            RecipeEntity recipeEntity = getRecipeEntity(recipe);
            this.recipeRepository.createRecipe(recipeEntity);
            return new UpsertRecipeDto(recipeEntity.getRecipeId());

        }
        else {
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

            RecipeEntity recipeEntity = getRecipeEntity(recipe);
            this.recipeRepository.updateRecipe(recipeEntity)
                    .orElseThrow(() -> new RecipeCannotBeUpdatedException(recipeId));
            return new UpsertRecipeDto(recipeEntity.getRecipeId());
        }
    }
}