package com.thepantry.recipeservice.application.recipes.deleteRecipe;

import com.thepantry.recipeservice.application.recipes.IRecipeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteRecipeHandlerTests {

    @InjectMocks
    private DeleteRecipeHandler deleteRecipeHandler;

    @Mock
    private IRecipeRepository recipeRepository;

    @Test
    void testHandle_shouldReturnTrue_WhenRecipeExists() {

        UUID recipeId = UUID.randomUUID();
        when(recipeRepository.deleteRecipe(recipeId)).thenReturn(true);

        DeleteRecipeRequest request = new DeleteRecipeRequest(recipeId);
        assertTrue(deleteRecipeHandler.handle(request));
    }

    @Test
    void testHandle_shouldReturnFalse_WhenRecipeDoesNotExist() {

        UUID recipeId = UUID.randomUUID();
        when(recipeRepository.deleteRecipe(recipeId)).thenReturn(false);

        DeleteRecipeRequest request = new DeleteRecipeRequest(recipeId);
        assertFalse(deleteRecipeHandler.handle(request));
    }
}