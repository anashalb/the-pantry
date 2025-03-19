package com.thepantry.recipeservice.application.recipes.upsertRecipe;

import com.thepantry.recipeservice.application.recipes.RecipeRequest;
import com.thepantry.recipeservice.application.recipes.IRecipeRepository;
import com.thepantry.recipeservice.application.recipes.RecipeIngredientRequest;
import com.thepantry.recipeservice.application.recipes.RecipeStepRequest;
import com.thepantry.recipeservice.domains.IUnitConfiguration;
import com.thepantry.recipeservice.domains.common.BusinessRuleException;
import com.thepantry.recipeservice.infrastructure.persistence.UnitConfiguration;
import com.thepantry.recipeservice.infrastructure.persistence.entities.RecipeEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpsertRecipeHandlerTests {

    private final IUnitConfiguration unitConfiguration = new UnitConfiguration();
    private UpsertRecipeHandler upsertRecipeHandler;
    @Mock
    private IRecipeRepository recipeRepository;
    private RecipeRequest recipeRequest;
    private RecipeEntity sampleRecipeEntity;

    @BeforeEach
    void setUp() {

        this.upsertRecipeHandler = new UpsertRecipeHandler(recipeRepository, unitConfiguration);

        UUID recipeId = UUID.randomUUID();
        recipeRequest = new RecipeRequest(
                "Test Recipe",
                "This is a test description",
                30L,
                15L,
                45L,
                (short) 4,
                List.of(new RecipeIngredientRequest(
                        UUID.randomUUID(),
                        10,
                        "kg",
                        "Grated"
                )),
                List.of(new RecipeStepRequest(
                        "Boil for new minutes"
                ))
        );

        sampleRecipeEntity = new RecipeEntity();
        sampleRecipeEntity.setRecipeId(recipeId);
        sampleRecipeEntity.setName(recipeRequest.name());
        sampleRecipeEntity.setDescription(recipeRequest.description());
        sampleRecipeEntity.setCookingTimeMinutes(recipeRequest.cookingTimeMinutes());
        sampleRecipeEntity.setPreparationTimeMinutes(recipeRequest.preparationTimeMinutes());
        sampleRecipeEntity.setReadyInTimeMinutes(recipeRequest.readyInTimeMinutes());
        sampleRecipeEntity.setServings(recipeRequest.servings());
    }

    @Test
    void testHandle_ShouldInvokeCreate_WhenRecipeIdIsNull() throws BusinessRuleException {
        when(recipeRepository.createRecipe(any(RecipeEntity.class))).thenReturn(sampleRecipeEntity);

        UpsertRecipeDto result = upsertRecipeHandler.handle(recipeRequest, null);

        assertNotNull(result);
        assertNotNull(result.recipeId());

        verify(recipeRepository, times(1)).createRecipe(any(RecipeEntity.class));
    }

    @Test
    void testHandle_ShouldThrowException_WhenBusinessRuleFails() {
        RecipeRequest invalidRequest = new RecipeRequest(
                "",
                "Test description",
                30L,
                15L,
                45L,
                (short) 4,
                List.of(new RecipeIngredientRequest(
                        UUID.randomUUID(),
                        10,
                        "kg",
                        "Grated"
                )),
                List.of(new RecipeStepRequest(
                        "Boil for new minutes"
                ))
        );

        assertThrows(BusinessRuleException.class, () -> upsertRecipeHandler.handle(invalidRequest, null));

        verify(recipeRepository, never()).createRecipe(any(RecipeEntity.class));
    }

    @Test
    void testHandle_ShouldUpdateRecipe_WhenRecipeIdIsNotNull() throws BusinessRuleException {
        when(recipeRepository.updateRecipe(any(RecipeEntity.class))).thenReturn(Optional.of(sampleRecipeEntity));

        UpsertRecipeDto result = upsertRecipeHandler.handle(recipeRequest, UUID.randomUUID());

        assertNotNull(result);
        assertNotNull(result.recipeId());

        verify(recipeRepository, times(1)).updateRecipe(any(RecipeEntity.class));
    }

    @Test
    void testHandle_ShouldThrowException_WhenRecipeIdIsNotFound() throws BusinessRuleException {
        when(recipeRepository.updateRecipe(any(RecipeEntity.class))).thenReturn(Optional.empty());

        assertThrows(
                RecipeCannotBeUpdatedException.class,
                () -> upsertRecipeHandler.handle(recipeRequest, UUID.randomUUID()));

        verify(recipeRepository, times(1)).updateRecipe(any(RecipeEntity.class));
    }
}