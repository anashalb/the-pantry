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

    @InjectMocks
    private GetRecipeHandler getRecipeHandler;

    @Mock
    private IRecipeRepository recipeRepository;

    private UUID recipeId;
    private GetRecipeDto getRecipeDto;
    private RecipeEntity recipeEntity;

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
        ingredient.setMeasurementUnit(measurementUnit.getName());

        List<RecipeIngredientEntity> recipeIngredients = List.of(ingredient);
        recipeEntity.setRecipeIngredients(recipeIngredients);

        // Mock Steps
        RecipeStepEntity step = new RecipeStepEntity();
        step.setStepNumber(1);
        step.setInstructions("Mix all ingredients together.");

        List<RecipeStepEntity> recipeSteps = List.of(step);
        recipeEntity.setRecipeSteps(recipeSteps);
    }

    @Test
    void handle_ShouldReturnRecipeDetailsDto_WhenRecipeExists() throws RecipeNotFoundException {
        when(recipeRepository.getRecipeDetailsByRecipeId(recipeId)).thenReturn(Optional.of(recipeEntity));

        RecipeDetailsDto result = getRecipeHandler.handle(getRecipeDto);

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

        verify(recipeRepository, times(1)).getRecipeDetailsByRecipeId(recipeId);
    }

    @Test
    void handle_ShouldThrowRecipeNotFoundException_WhenRecipeDoesNotExist() {
        when(recipeRepository.getRecipeDetailsByRecipeId(recipeId)).thenReturn(Optional.empty());

        RecipeNotFoundException thrown = assertThrows(RecipeNotFoundException.class, () -> {
            getRecipeHandler.handle(getRecipeDto);
        });

        assertEquals(recipeId, thrown.getRecipeId());

        verify(recipeRepository, times(1)).getRecipeDetailsByRecipeId(recipeId);
    }
}