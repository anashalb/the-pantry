package com.thepantry.recipeservice.application.recipes.deleteRecipe;

import com.thepantry.recipeservice.application.recipes.IRecipeRepository;
import com.thepantry.recipeservice.infrastructure.persistence.repository.RecipeRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteRecipeHandlerTests {

    @Mock
    private IRecipeRepository recipeRepository;

    @InjectMocks
    private DeleteRecipeHandler deleteRecipeHandler;

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