package com.thepantry.recipeservice.application.recipes.getRecipeDetails;

import com.thepantry.recipeservice.application.recipes.IRecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
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
    private RecipeDetailsDto recipeDetailsDto;

    @BeforeEach
    void setUp() {
        recipeId = UUID.randomUUID();
        getRecipeDto = new GetRecipeDto(recipeId);

        recipeDetailsDto = new RecipeDetailsDto(
                recipeId,
                "Test Recipe",
                "Delicious test recipe",
                30L,
                15L,
                45L,
                (short) 4,
                List.of(new RecipeIngredientDto(1L, null, 2.0, "cups")),
                List.of(new RecipeStepDto(1, "Mix all ingredients together."))
        );
    }

    @Test
    void handle_ShouldReturnRecipeDetailsDto_WhenRecipeExists() throws RecipeNotFoundException {
        when(recipeRepository.getRecipeDetailsByRecipeId(recipeId)).thenReturn(Optional.of(recipeDetailsDto));

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
