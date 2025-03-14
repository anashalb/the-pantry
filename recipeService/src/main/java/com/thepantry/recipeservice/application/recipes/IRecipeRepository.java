package com.thepantry.recipeservice.application.recipes;

import com.thepantry.recipeservice.infrastructure.persistence.entities.RecipeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IRecipeRepository {

    /**
     * Retrieves the complete details of a recipe by its unique identifier.
     *
     * @param recipeId The unique identifier of the recipe to retrieve.
     * @return An {@code Optional} containing the recipe details if found, otherwise {@code Optional.empty()}.
     */
    Optional<RecipeEntity> getRecipeDetailsByRecipeId(UUID recipeId);

    /**
     * Retrieves a list of recipes created by the specified user.
     *
     * @param userId The unique identifier of the user whose recipes are to be retrieved.
     * @return A list of {@code RecipeEntity} instances created by the user, or an empty list if none are found.
     */
    Page<RecipeEntity> getRecipesCreatedByUserId(UUID userId, Pageable pageRequest);

    /**
     * Creates a new recipe
     *
     * @param recipeEntity The details of the recipe to be created
     * @return The newly created recipe
     */
    RecipeEntity createRecipe(RecipeEntity recipeEntity);
}