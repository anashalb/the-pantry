package com.thepantry.recipeservice.infrastructure.persistence.repository;

import com.thepantry.recipeservice.application.recipes.getRecipeDetails.RecipeDetailsDto;
import com.thepantry.recipeservice.application.recipes.getRecipesCreatedByUser.UserRecipeDto;
import com.thepantry.recipeservice.domains.Recipe;
import com.thepantry.recipeservice.domains.common.BusinessRuleException;
import com.thepantry.recipeservice.infrastructure.persistence.entities.RecipeEntity;
import com.thepantry.recipeservice.infrastructure.persistence.entities.RecipeIngredientEntity;
import com.thepantry.recipeservice.infrastructure.persistence.entities.RecipeStepEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class RecipeRepositoryImplTests {

    @InjectMocks
    private RecipeRepositoryImpl recipeRepositoryImpl;

    @Mock
    private IRecipeCrudRepository recipeRepository;

    @Mock
    private IRecipeIngredientCrudRepository recipeIngredientRepository;

    @Mock
    private IRecipeStepCrudRepository recipeStepRepository;

    private Long entityId;
    private UUID recipeId;
    private UUID userId;
    private RecipeEntity recipeEntity;
    private RecipeIngredientEntity ingredientEntity;
    private RecipeStepEntity stepEntity;

    @BeforeEach
    void setUp() {
        entityId = new Random().nextLong();
        recipeId = UUID.randomUUID();
        userId = UUID.randomUUID();

        recipeEntity = new RecipeEntity();
        recipeEntity.setId(entityId);
        recipeEntity.setRecipeId(recipeId);
        recipeEntity.setName("Test Recipe");
        recipeEntity.setDescription("A test description");
        recipeEntity.setCookingTimeMinutes(30L);
        recipeEntity.setPreparationTimeMinutes(15L);
        recipeEntity.setReadyInTimeMinutes(45L);
        recipeEntity.setServings((short) 4);
        recipeEntity.setCreatedBy(userId);

        ingredientEntity = new RecipeIngredientEntity();
        ingredientEntity.setId(new Random().nextLong());
        ingredientEntity.setQuantity(2.0);
        ingredientEntity.setPreparationMethod("Diced");
        ingredientEntity.setMeasurementUnit("kg");

        stepEntity = new RecipeStepEntity();
        stepEntity.setStepNumber(1);
        stepEntity.setInstructions("Test instructions");

        when(recipeRepository.findByRecipeId(recipeId)).thenReturn(Optional.of(recipeEntity));
        when(recipeIngredientRepository.findAllByRecipeIdOrderByIdAsc(entityId)).thenReturn(List.of(ingredientEntity));
        when(recipeStepRepository.findAllByRecipeIdOrderByStepNumberAsc(entityId)).thenReturn(List.of(stepEntity));
    }

    @Test
    void testGetRecipeDetailsByRecipeId_ShouldReturnRecipe_WhenRecipeExists() {
        Optional<RecipeDetailsDto> result = recipeRepositoryImpl.getRecipeDetailsByRecipeId(recipeId);

        assertTrue(result.isPresent(), "Recipe should be found");
        assertEquals(recipeId, result.get().getRecipeId(), "Recipe ID should match");
        assertEquals(recipeEntity.getName(), result.get().getName(), "Recipe name should match");
        assertEquals(1, result.get().getIngredients().size(), "Ingredients count should match");
        assertEquals(1, result.get().getSteps().size(), "Steps count should match");

        verify(recipeRepository, times(1)).findByRecipeId(recipeId);
        verify(recipeIngredientRepository, times(1)).findAllByRecipeIdOrderByIdAsc(entityId);
        verify(recipeStepRepository, times(1)).findAllByRecipeIdOrderByStepNumberAsc(entityId);
    }

    @Test
    void testGetRecipeDetailsByRecipeId_ShouldReturnEmpty_WhenRecipeDoesNotExist() {
        UUID nonExistentRecipeId = UUID.randomUUID();
        when(recipeRepository.findByRecipeId(nonExistentRecipeId)).thenReturn(Optional.empty());

        Optional<RecipeDetailsDto> result = recipeRepositoryImpl.getRecipeDetailsByRecipeId(nonExistentRecipeId);

        assertTrue(result.isEmpty(), "Should return empty when recipe is not found");

        verify(recipeRepository, times(1)).findByRecipeId(nonExistentRecipeId);
        verifyNoInteractions(recipeIngredientRepository, recipeStepRepository);
    }

    @Test
    void testGetRecipesCreatedByUserId_ShouldReturnRecipes_WhenRecipesExist() {
        UUID testUserId = UUID.randomUUID();
        RecipeEntity recipe1 = new RecipeEntity();
        recipe1.setRecipeId(UUID.randomUUID());
        recipe1.setName("Recipe 1");
        recipe1.setDescription("A description");
        recipe1.setCookingTimeMinutes(30L);
        recipe1.setPreparationTimeMinutes(15L);
        recipe1.setReadyInTimeMinutes(45L);
        recipe1.setServings((short) 4);
        recipe1.setCreatedBy(userId);

        Pageable pageable = PageRequest.of(0, 10);
        Page<RecipeEntity> mockPage = new PageImpl<>(List.of(recipe1));

        when(recipeRepository.findAllByCreatedBy(testUserId, pageable)).thenReturn(mockPage);

        Page<UserRecipeDto> result = recipeRepositoryImpl.getRecipesCreatedByUserId(testUserId, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(recipe1.getName(), result.getContent().getFirst().getName());

        verify(recipeRepository, times(1)).findAllByCreatedBy(testUserId, pageable);
    }

    @Test
    void testCreateRecipe_ShouldCreateRecipe() throws BusinessRuleException {
        Recipe recipe = Recipe.update(
                UUID.randomUUID(),
                "Chocolate Cake",
                "A delicious cake",
                Duration.ofMinutes(30),
                Duration.ofMinutes(15),
                Duration.ofMinutes(45),
                (short) 4,
                List.of(),
                List.of()
        );

        RecipeEntity savedEntity = new RecipeEntity();
        savedEntity.setRecipeId(recipe.getRecipeId().getValue());
        when(recipeRepository.save(any(RecipeEntity.class))).thenReturn(savedEntity);

        UUID result = recipeRepositoryImpl.createRecipe(recipe);

        assertNotNull(result);
        assertEquals(recipe.getRecipeId().getValue(), result);

        verify(recipeRepository, times(1)).save(any(RecipeEntity.class));
    }

    @Test
    void testDeleteRecipe_ShouldDeleteRecipe_WhenRecipeExists() {
        UUID recipeId = UUID.randomUUID();
        when(recipeRepository.deleteByRecipeId(recipeId)).thenReturn(1);

        assertTrue(recipeRepositoryImpl.deleteRecipe(recipeId));
    }

    @Test
    void testDeleteRecipe_ShouldNotDeleteRecipe_WhenRecipeDoesNotExists() {
        UUID recipeId = UUID.randomUUID();
        when(recipeRepository.deleteByRecipeId(recipeId)).thenReturn(0);

        assertFalse(recipeRepositoryImpl.deleteRecipe(recipeId));
    }

    @Test
    void testUpdateRecipe_ShouldUpdateRecipe_WhenRecipeExists() throws BusinessRuleException {
        Recipe recipe = Recipe.update(
                recipeId,
                "Test Recipe",
                "A test description",
                Duration.ofMinutes(30),
                Duration.ofMinutes(15),
                Duration.ofMinutes(45),
                (short) 4,
                List.of(),
                List.of()
        );

        when(recipeRepository.findByRecipeId(recipeId)).thenReturn(Optional.of(recipeEntity));
        when(recipeRepository.save(any(RecipeEntity.class))).thenReturn(recipeEntity);

        Optional<UUID> result = recipeRepositoryImpl.updateRecipe(recipe);

        verify(recipeRepository, times(1)).save(any(RecipeEntity.class));
        verify(recipeIngredientRepository, times(1)).deleteAllByRecipeId(entityId);
        verify(recipeStepRepository, times(1)).deleteAllByRecipeId(entityId);
        verify(recipeIngredientRepository, times(1)).saveAll(anyList());
        verify(recipeStepRepository, times(1)).saveAll(anyList());

        assertTrue(result.isPresent());
        assertEquals(recipeId, result.get(), "Recipe ID should match");
    }

    @Test
    void testUpdateRecipe_ShouldReturnEmpty_WhenRecipeDoesNotExist() throws BusinessRuleException {
        UUID nonExistentId = UUID.randomUUID();
        Recipe recipe = Recipe.update(
                nonExistentId,
                "Test Recipe",
                "A test description",
                Duration.ofMinutes(30),
                Duration.ofMinutes(15),
                Duration.ofMinutes(45),
                (short) 4,
                List.of(),
                List.of()
        );

        when(recipeRepository.findByRecipeId(nonExistentId)).thenReturn(Optional.empty());

        Optional<UUID> result = recipeRepositoryImpl.updateRecipe(recipe);

        assertTrue(result.isEmpty(), "Should return empty when recipe is not found");
    }
}
