package com.thepantry.recipeservice.application.recipes;

import com.thepantry.recipeservice.application.recipes.getRecipeDetails.RecipeDetailsDto;
import com.thepantry.recipeservice.application.recipes.getRecipesCreatedByUser.UserRecipeDto;
import com.thepantry.recipeservice.domains.Recipe;
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
    Optional<RecipeDetailsDto> getRecipeDetailsByRecipeId(UUID recipeId);

    /**
     * Retrieves a paginated list of recipes created by the specified user.
     *
     * @param userId      The unique identifier of the user whose recipes are to be retrieved.
     * @param pageRequest The pagination and sorting parameters.
     * @return A page of {@code UserRecipeDto} instances created by the user.
     */
    Page<UserRecipeDto> getRecipesCreatedByUserId(UUID userId, Pageable pageRequest);

    /**
     * Creates a new recipe.
     *
     * @param recipe The domain recipe to create.
     * @return The UUID of the newly created recipe.
     */
    UUID createRecipe(Recipe recipe);

    /**
     * Updates an existing recipe.
     *
     * @param recipe The domain recipe with updated values.
     * @return The UUID of the updated recipe, or {@code Optional.empty()} if not found.
     */
    Optional<UUID> updateRecipe(Recipe recipe);

    /**
     * Deletes an existing recipe.
     *
     * @param recipeId The ID of the recipe to be deleted.
     * @return True if the recipe was successfully deleted, false otherwise.
     */
    boolean deleteRecipe(UUID recipeId);
}
