package com.thepantry.recipeservice.application.recipes.getRecipeDetails;

import com.thepantry.recipeservice.application.recipes.IRecipeRepository;
import com.thepantry.recipeservice.domains.IUnitConfiguration;
import com.thepantry.recipeservice.infrastructure.persistence.entities.RecipeEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GetRecipeHandler {

    private final IRecipeRepository recipeRepository;

    public GetRecipeHandler(
            IRecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public RecipeDetailsDto handle(GetRecipeDto getRecipeDto) throws RecipeNotFoundException {

        RecipeEntity recipeEntity = recipeRepository.getRecipeDetailsByRecipeId(getRecipeDto.getRecipeId())
                .orElseThrow(() -> new RecipeNotFoundException(getRecipeDto.getRecipeId()));

        List<RecipeIngredientDto> recipeIngredients = recipeEntity.getRecipeIngredients().stream()
                .map(recipeIngredient ->
                        new RecipeIngredientDto(recipeIngredient.getId(),
                                recipeIngredient.getPreparationMethod(),
                                recipeIngredient.getQuantity(),
                                recipeIngredient.getMeasurementUnit()))
                .toList();

        List<RecipeStepDto> recipeSteps = recipeEntity.getRecipeSteps().stream()
                .map(recipeStep ->
                        new RecipeStepDto(recipeStep.getStepNumber(), recipeStep.getInstructions())
                ).toList();

        return new RecipeDetailsDto(
                recipeEntity.getRecipeId(),
                recipeEntity.getName(),
                recipeEntity.getDescription(),
                recipeEntity.getCookingTimeMinutes(),
                recipeEntity.getPreparationTimeMinutes(),
                recipeEntity.getReadyInTimeMinutes(),
                recipeEntity.getServings(),
                recipeIngredients,
                recipeSteps
        );
    }
}
