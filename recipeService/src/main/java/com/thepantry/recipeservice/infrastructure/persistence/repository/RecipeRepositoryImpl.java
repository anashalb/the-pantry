package com.thepantry.recipeservice.infrastructure.persistence.repository;

import com.thepantry.recipeservice.application.recipes.IRecipeRepository;
import com.thepantry.recipeservice.application.recipes.getRecipeDetails.RecipeDetailsDto;
import com.thepantry.recipeservice.application.recipes.getRecipeDetails.RecipeIngredientDto;
import com.thepantry.recipeservice.application.recipes.getRecipeDetails.RecipeStepDto;
import com.thepantry.recipeservice.application.recipes.getRecipesCreatedByUser.UserRecipeDto;
import com.thepantry.recipeservice.domains.Recipe;
import com.thepantry.recipeservice.domains.RecipeStep;
import com.thepantry.recipeservice.infrastructure.persistence.entities.RecipeEntity;
import com.thepantry.recipeservice.infrastructure.persistence.entities.RecipeIngredientEntity;
import com.thepantry.recipeservice.infrastructure.persistence.entities.RecipeStepEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.IntStream;

@Repository
public class RecipeRepositoryImpl implements IRecipeRepository {

    private final IRecipeIngredientCrudRepository recipeIngredientRepository;
    private final IRecipeStepCrudRepository recipeStepRepository;
    private final IRecipeCrudRepository recipeRepository;

    public RecipeRepositoryImpl(
            IRecipeCrudRepository recipeRepository,
            IRecipeIngredientCrudRepository recipeIngredientRepository,
            IRecipeStepCrudRepository recipeStepRepository) {
        this.recipeRepository = recipeRepository;
        this.recipeIngredientRepository = recipeIngredientRepository;
        this.recipeStepRepository = recipeStepRepository;
    }

    private RecipeEntity toEntity(Recipe recipe) {
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

    @Override
    public Optional<RecipeDetailsDto> getRecipeDetailsByRecipeId(UUID recipeId) {
        Optional<RecipeEntity> recipeResult = recipeRepository.findByRecipeId(recipeId);

        if (recipeResult.isEmpty()) {
            return Optional.empty();
        }

        RecipeEntity recipe = recipeResult.get();

        List<RecipeIngredientEntity> recipeIngredientEntities =
                recipeIngredientRepository.findAllByRecipeIdOrderByIdAsc(recipe.getId());

        List<RecipeStepEntity> recipeSteps =
                recipeStepRepository.findAllByRecipeIdOrderByStepNumberAsc(recipe.getId());

        List<RecipeIngredientDto> ingredientDtos = recipeIngredientEntities.stream()
                .map(i -> new RecipeIngredientDto(i.getId(), i.getPreparationMethod(), i.getQuantity(), i.getMeasurementUnit()))
                .toList();

        List<RecipeStepDto> stepDtos = recipeSteps.stream()
                .map(s -> new RecipeStepDto(s.getStepNumber(), s.getInstructions()))
                .toList();

        return Optional.of(new RecipeDetailsDto(
                recipe.getRecipeId(),
                recipe.getName(),
                recipe.getDescription(),
                recipe.getCookingTimeMinutes(),
                recipe.getPreparationTimeMinutes(),
                recipe.getReadyInTimeMinutes(),
                recipe.getServings(),
                ingredientDtos,
                stepDtos
        ));
    }

    @Override
    public Page<UserRecipeDto> getRecipesCreatedByUserId(UUID createdBy, Pageable pageRequest) {
        return this.recipeRepository.findAllByCreatedBy(createdBy, pageRequest)
                .map(entity -> new UserRecipeDto(
                        entity.getRecipeId(),
                        entity.getName(),
                        entity.getDescription(),
                        entity.getCookingTimeMinutes(),
                        entity.getPreparationTimeMinutes(),
                        entity.getReadyInTimeMinutes(),
                        entity.getServings()
                ));
    }

    @Override
    public UUID createRecipe(Recipe recipe) {
        RecipeEntity recipeEntity = toEntity(recipe);
        RecipeEntity createdRecipe = this.recipeRepository.save(recipeEntity);
        this.recipeIngredientRepository.saveAll(createdRecipe.getRecipeIngredients());
        this.recipeStepRepository.saveAll(createdRecipe.getRecipeSteps());
        return recipe.getRecipeId().getValue();
    }

    @Override
    @Transactional
    public Optional<UUID> updateRecipe(Recipe recipe) {
        RecipeEntity recipeEntity = toEntity(recipe);
        Optional<RecipeEntity> result = this.recipeRepository.findByRecipeId(recipeEntity.getRecipeId());

        if (result.isEmpty()) {
            return Optional.empty();
        }

        Long existingId = result.get().getId();
        recipeEntity.setId(existingId);

        this.recipeRepository.save(recipeEntity);

        this.recipeIngredientRepository.deleteAllByRecipeId(existingId);
        this.recipeStepRepository.deleteAllByRecipeId(existingId);

        this.recipeIngredientRepository.saveAll(recipeEntity.getRecipeIngredients());
        this.recipeStepRepository.saveAll(recipeEntity.getRecipeSteps());

        return Optional.of(recipe.getRecipeId().getValue());
    }

    @Override
    @Transactional
    public boolean deleteRecipe(UUID recipeId) {
        return this.recipeRepository.deleteByRecipeId(recipeId) == 1;
    }
}
