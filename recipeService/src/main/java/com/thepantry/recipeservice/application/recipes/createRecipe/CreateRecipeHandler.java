package com.thepantry.recipeservice.application.recipes.createRecipe;

import com.thepantry.recipeservice.application.recipes.IRecipeRepository;
import com.thepantry.recipeservice.domains.*;
import com.thepantry.recipeservice.domains.common.BusinessRuleException;
import com.thepantry.recipeservice.infrastructure.persistence.entities.RecipeEntity;
import com.thepantry.recipeservice.infrastructure.persistence.entities.RecipeIngredientEntity;
import com.thepantry.recipeservice.infrastructure.persistence.entities.RecipeStepEntity;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class CreateRecipeHandler {

    private final IRecipeRepository recipeRepository;
    private final IUnitConfiguration unitConfiguration;

    public CreateRecipeHandler(
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

    public CreateRecipeDto handle(CreateRecipeRequest createRecipeRequest) throws BusinessRuleException {

        List<RecipeIngredient> recipeIngredients = new ArrayList<>();
        for (RecipeIngredientRequest ingredient : createRecipeRequest.ingredients()) {
            recipeIngredients.add(
                    RecipeIngredient.of(new Ingredient(new IngredientId(ingredient.ingredientId())),
                            ingredient.preparationMethod(),
                            ingredient.quantity(),
                            MeasurementUnit.of(ingredient.measurementUnit(), unitConfiguration)));
        }

        List<RecipeStep> recipeSteps = new ArrayList<>();
        for (RecipeStepRequest step : createRecipeRequest.steps()) {
            recipeSteps.add(RecipeStep.of(step.instructions()));
        }

        Recipe recipe = Recipe.create(
                createRecipeRequest.name(),
                createRecipeRequest.description(),
                Duration.ofMinutes(createRecipeRequest.cookingTimeMinutes()),
                Duration.ofMinutes(createRecipeRequest.preparationTimeMinutes()),
                Duration.ofMinutes(createRecipeRequest.readyInTimeMinutes()),
                createRecipeRequest.servings(),
                recipeIngredients,
                recipeSteps,
                unitConfiguration
        );

        RecipeEntity recipeEntity = getRecipeEntity(recipe);

        this.recipeRepository.createRecipe(recipeEntity);

        return new CreateRecipeDto(recipeEntity.getRecipeId());
    }
}