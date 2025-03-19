package com.thepantry.recipeservice.application.recipes;

import com.thepantry.recipeservice.infrastructure.persistence.entities.RecipeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

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

    /**
     * Updates a recipe
     * @param recipeEntity The details of the updated recipe
     * @return The updated recipe
     */
    Optional<RecipeEntity> updateRecipe(RecipeEntity recipeEntity);

    /**
     * Deletes an existing recipe
     *
     * @param recipeId The ID of the recipe to be deleted
     * @return True if the recipe was successfully deleted, false otherwise
     */
    boolean deleteRecipe(UUID recipeId);

}