package com.thepantry.recipeservice.application.recipes.createRecipe;

import com.thepantry.recipeservice.application.recipes.IRecipeRepository;
import com.thepantry.recipeservice.domains.common.BusinessRuleException;
import com.thepantry.recipeservice.infrastructure.persistence.entities.RecipeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateRecipeHandlerTests {

    @Mock
    private IRecipeRepository recipeRepository;

    @InjectMocks
    private CreateRecipeHandler createRecipeHandler;

    private CreateRecipeRequest createRecipeRequest;
    private RecipeEntity sampleRecipeEntity;

    @BeforeEach
    void setUp() {
        UUID recipeId = UUID.randomUUID();

        createRecipeRequest = new CreateRecipeRequest(
                "Test Recipe",
                "This is a test description",
                30L,
                15L,
                45L,
                (short) 4
        );

        sampleRecipeEntity = new RecipeEntity();
        sampleRecipeEntity.setRecipeId(recipeId);
        sampleRecipeEntity.setName(createRecipeRequest.name());
        sampleRecipeEntity.setDescription(createRecipeRequest.description());
        sampleRecipeEntity.setCookingTimeMinutes(createRecipeRequest.cookingTimeMinutes());
        sampleRecipeEntity.setPreparationTimeMinutes(createRecipeRequest.preparationTimeMinutes());
        sampleRecipeEntity.setReadyInTimeMinutes(createRecipeRequest.readyInTimeMinutes());
        sampleRecipeEntity.setServings(createRecipeRequest.servings());
    }

    @Test
    void testHandle_Success() throws BusinessRuleException {
        // Arrange: Mock repository behavior
        when(recipeRepository.createRecipe(any(RecipeEntity.class))).thenReturn(sampleRecipeEntity);

        // Act
        CreateRecipeDto result = createRecipeHandler.handle(createRecipeRequest);

        // Assert
        assertNotNull(result);
        assertNotNull(result.recipeId());

        // Verify repository interaction
        verify(recipeRepository, times(1)).createRecipe(any(RecipeEntity.class));
    }

    @Test
    void testHandle_BusinessRuleException() {
        // Arrange: Simulate rule violation
        CreateRecipeRequest invalidRequest = new CreateRecipeRequest(
                "", // Invalid name (empty)
                "Test description",
                30L,
                15L,
                45L,
                (short) 4
        );

        // Act & Assert: Expect BusinessRuleException to be thrown
        assertThrows(BusinessRuleException.class, () -> createRecipeHandler.handle(invalidRequest));

        // Verify repository was never called
        verify(recipeRepository, never()).createRecipe(any(RecipeEntity.class));
    }
}