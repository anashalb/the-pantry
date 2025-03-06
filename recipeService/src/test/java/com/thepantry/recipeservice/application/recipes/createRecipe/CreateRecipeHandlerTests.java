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
        when(recipeRepository.createRecipe(any(RecipeEntity.class))).thenReturn(sampleRecipeEntity);

        CreateRecipeDto result = createRecipeHandler.handle(createRecipeRequest);

        assertNotNull(result);
        assertNotNull(result.recipeId());

        verify(recipeRepository, times(1)).createRecipe(any(RecipeEntity.class));
    }

    @Test
    void testHandle_BusinessRuleException() {
        CreateRecipeRequest invalidRequest = new CreateRecipeRequest(
                "", // Invalid name (empty)
                "Test description",
                30L,
                15L,
                45L,
                (short) 4
        );

        assertThrows(BusinessRuleException.class, () -> createRecipeHandler.handle(invalidRequest));

        verify(recipeRepository, never()).createRecipe(any(RecipeEntity.class));
    }
}