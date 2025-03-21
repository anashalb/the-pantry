package com.thepantry.recipeservice.infrastructure.persistence.repository;

import com.thepantry.recipeservice.application.recipes.IRecipeRepository;
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

    @Override
    public Optional<RecipeEntity> getRecipeDetailsByRecipeId(UUID recipeId) {

        Optional<RecipeEntity> recipeResult = recipeRepository.findByRecipeId(recipeId);

        if (recipeResult.isEmpty()) {
            return Optional.empty();
        }

        RecipeEntity recipe = recipeResult.get();

        List<RecipeIngredientEntity> recipeIngredientEntities =
                recipeIngredientRepository.findAllByRecipeIdOrderByIdAsc(recipe.getId());

        recipe.setRecipeIngredients(recipeIngredientEntities);

        List<RecipeStepEntity> recipeSteps =
                recipeStepRepository.findAllByRecipeIdOrderByStepNumberAsc(recipe.getId());
        recipe.setRecipeSteps(recipeSteps);

        return Optional.of(recipe);
    }

    @Override
    public Page<RecipeEntity> getRecipesCreatedByUserId(
            UUID createdBy, Pageable pageRequest) {

        return this.recipeRepository.findAllByCreatedBy(createdBy, pageRequest);
    }

    @Override
    public RecipeEntity createRecipe(RecipeEntity recipeEntity) {

        RecipeEntity createdRecipe = this.recipeRepository.save(recipeEntity);
        this.recipeIngredientRepository.saveAll(createdRecipe.getRecipeIngredients());
        this.recipeStepRepository.saveAll(createdRecipe.getRecipeSteps());
        return createdRecipe;
    }

    @Override
    @Transactional
    public Optional<RecipeEntity> updateRecipe(RecipeEntity recipeEntity) {

        Optional<RecipeEntity> result = this.recipeRepository.findByRecipeId(recipeEntity.getRecipeId());

        if (result.isEmpty()) {
            return Optional.empty();
        }

        RecipeEntity existingRecipe = result.get();
        Long recipeId = existingRecipe.getId();
        recipeEntity.setId(recipeId);

        RecipeEntity recipe = this.recipeRepository.save(recipeEntity);

        this.recipeIngredientRepository.deleteAllByRecipeId(recipeId);
        this.recipeStepRepository.deleteAllByRecipeId(recipeId);

        this.recipeIngredientRepository.saveAll(recipeEntity.getRecipeIngredients());
        this.recipeStepRepository.saveAll(recipeEntity.getRecipeSteps());

        return Optional.of(recipe);
    }

    @Override
    @Transactional
    public boolean deleteRecipe(UUID recipeId) {
        return this.recipeRepository.deleteByRecipeId(recipeId) == 1;
    }
}
