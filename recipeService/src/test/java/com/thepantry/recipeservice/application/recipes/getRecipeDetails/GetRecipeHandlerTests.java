package com.thepantry.recipeservice.application.recipes.getRecipeDetails;

import com.thepantry.recipeservice.application.recipes.IRecipeRepository;
import com.thepantry.recipeservice.infrastructure.persistence.entities.MeasurementUnitEntity;
import com.thepantry.recipeservice.infrastructure.persistence.entities.RecipeEntity;
import com.thepantry.recipeservice.infrastructure.persistence.entities.RecipeIngredientEntity;
import com.thepantry.recipeservice.infrastructure.persistence.entities.RecipeStepEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetRecipeHandlerTests {

    @Mock
    private IRecipeRepository recipeRepository;

    @InjectMocks
    private GetRecipeHandler getRecipeHandler;

    private UUID recipeId;
    private GetRecipeDto getRecipeDto;
    private RecipeEntity recipeEntity;
    private List<RecipeIngredientEntity> recipeIngredients;
    private List<RecipeStepEntity> recipeSteps;

    @BeforeEach
    void setUp() {
        recipeId = UUID.randomUUID();
        getRecipeDto = new GetRecipeDto(recipeId);

        // Mock RecipeEntity
        recipeEntity = new RecipeEntity();
        recipeEntity.setRecipeId(recipeId);
        recipeEntity.setName("Test Recipe");
        recipeEntity.setDescription("Delicious test recipe");
        recipeEntity.setCookingTimeMinutes(30L);
        recipeEntity.setPreparationTimeMinutes(15L);
        recipeEntity.setReadyInTimeMinutes(45L);
        recipeEntity.setServings((short) 4);

        // Mock Measurement Unit
        MeasurementUnitEntity measurementUnit = new MeasurementUnitEntity();
        measurementUnit.setName("cups");

        // Mock Ingredients
        RecipeIngredientEntity ingredient = new RecipeIngredientEntity();
        ingredient.setId(new Random().nextLong());
        ingredient.setQuantity(2.0);
        ingredient.setMeasurementUnit(measurementUnit);

        recipeIngredients = List.of(ingredient);
        recipeEntity.setRecipeIngredients(recipeIngredients);

        // Mock Steps
        RecipeStepEntity step = new RecipeStepEntity();
        step.setStepNumber(1);
        step.setInstructions("Mix all ingredients together.");

        recipeSteps = List.of(step);
        recipeEntity.setRecipeSteps(recipeSteps);
    }

    @Test
    void handle_ShouldReturnRecipeDetailsDto_WhenRecipeExists() throws RecipeNotFoundException {
        // Arrange
        when(recipeRepository.getRecipeDetailsByRecipeId(recipeId)).thenReturn(Optional.of(recipeEntity));

        // Act
        RecipeDetailsDto result = getRecipeHandler.handle(getRecipeDto);

        // Assert
        assertNotNull(result);
        assertEquals(recipeId, result.getRecipeId());
        assertEquals("Test Recipe", result.getName());
        assertEquals("Delicious test recipe", result.getDescription());
        assertEquals(30, result.getCookingTimeMinutes());
        assertEquals(15, result.getPreparationTimeMinutes());
        assertEquals(45, result.getReadyInTimeMinutes());
        assertEquals((short) 4, result.getServings());
        assertEquals(1, result.getIngredients().size());
        assertEquals(2.0, result.getIngredients().getFirst().getQuantity());
        assertEquals("cups", result.getIngredients().getFirst().getMeasurementUnit());
        assertEquals(1, result.getSteps().size());
        assertEquals("Mix all ingredients together.", result.getSteps().getFirst().getInstructions());

        // Verify interactions
        verify(recipeRepository, times(1)).getRecipeDetailsByRecipeId(recipeId);
    }

    @Test
    void handle_ShouldThrowRecipeNotFoundException_WhenRecipeDoesNotExist() {
        // Arrange
        when(recipeRepository.getRecipeDetailsByRecipeId(recipeId)).thenReturn(Optional.empty());

        // Act & Assert
        RecipeNotFoundException thrown = assertThrows(RecipeNotFoundException.class, () -> {
            getRecipeHandler.handle(getRecipeDto);
        });

        assertEquals(recipeId, thrown.getRecipeId());

        // Verify interactions
        verify(recipeRepository, times(1)).getRecipeDetailsByRecipeId(recipeId);
    }
}