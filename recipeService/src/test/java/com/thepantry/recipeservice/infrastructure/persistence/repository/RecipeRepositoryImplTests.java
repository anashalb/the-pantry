package com.thepantry.recipeservice.infrastructure.persistence.repository;

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
    private List<RecipeIngredientEntity> recipeIngredients;
    private List<RecipeStepEntity> recipeSteps;

    @BeforeEach
    void setUp() {
        entityId = new Random().nextLong();
        recipeId = UUID.randomUUID();
        userId = UUID.randomUUID();

        // Mock RecipeEntity
        recipeEntity = new RecipeEntity();
        recipeEntity.setId(entityId);
        recipeEntity.setRecipeId(recipeId);
        recipeEntity.setName("Test Recipe");
        recipeEntity.setCreatedBy(userId);

        // Mock Ingredients
        recipeIngredients = List.of(new RecipeIngredientEntity());
        recipeSteps = List.of(new RecipeStepEntity());

        // Stubbing behavior
        when(recipeRepository.findByRecipeId(recipeId)).thenReturn(Optional.of(recipeEntity));
        when(recipeIngredientRepository.findAllByRecipeIdOrderByIdAsc(entityId)).thenReturn(recipeIngredients);
        when(recipeStepRepository.findAllByRecipeIdOrderByStepNumberAsc(entityId)).thenReturn(recipeSteps);
    }

    @Test
    void testGetRecipeDetailsByRecipeId_ShouldReturnRecipe_WhenRecipeExists() {
        Optional<RecipeEntity> result = recipeRepositoryImpl.getRecipeDetailsByRecipeId(recipeId);

        assertTrue(result.isPresent(), "Recipe should be found");
        assertEquals(recipeEntity.getId(), result.get().getId(), "Recipe ID should match");
        assertEquals(recipeIngredients, result.get().getRecipeIngredients(), "Ingredients should match");
        assertEquals(recipeSteps, result.get().getRecipeSteps(), "Steps should match");

        verify(recipeRepository, times(1)).findByRecipeId(recipeId);
        verify(recipeIngredientRepository, times(1)).findAllByRecipeIdOrderByIdAsc(entityId);
        verify(recipeStepRepository, times(1)).findAllByRecipeIdOrderByStepNumberAsc(entityId);
    }

    @Test
    void testGetRecipeDetailsByRecipeId_ShouldReturnEmpty_WhenRecipeDoesNotExist() {
        UUID nonExistentRecipeId = UUID.randomUUID();
        when(recipeRepository.findByRecipeId(nonExistentRecipeId)).thenReturn(Optional.empty());

        Optional<RecipeEntity> result = recipeRepositoryImpl.getRecipeDetailsByRecipeId(nonExistentRecipeId);

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
        recipe1.setCreatedBy(userId);

        Pageable pageable = PageRequest.of(0, 10);
        Page<RecipeEntity> mockPage = new PageImpl<>(List.of(recipe1));

        when(recipeRepository.findAllByCreatedBy(testUserId, pageable)).thenReturn(mockPage);
        Page<RecipeEntity> result = recipeRepositoryImpl.getRecipesCreatedByUserId(testUserId, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(recipe1, result.getContent().getFirst());

        verify(recipeRepository, times(1)).findAllByCreatedBy(testUserId, pageable);
    }

    @Test
    void testCreateRecipe_ShouldCreateRecipe() {

        RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setName("Chocolate Cake");

        when(recipeRepository.save(recipeEntity)).thenReturn(recipeEntity);
        RecipeEntity savedRecipe = recipeRepositoryImpl.createRecipe(recipeEntity);

        assertNotNull(savedRecipe);
        assertEquals("Chocolate Cake", savedRecipe.getName());

        verify(recipeRepository, times(1)).save(recipeEntity);
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
    void testUpdateRecipe_ShouldUpdateRecipe_WhenRecipeExists() {

        when(recipeRepository.findByRecipeId(recipeId)).thenReturn(Optional.of(recipeEntity));
        when(recipeRepository.save(recipeEntity)).thenReturn(recipeEntity);

        Optional<RecipeEntity> result = recipeRepositoryImpl.updateRecipe(recipeEntity);

        verify(recipeRepository, times(1)).save(recipeEntity);

        verify(recipeIngredientRepository, times(1))
                .deleteAllByRecipeId(recipeEntity.getId());

        verify(recipeStepRepository, times(1))
                .deleteAllByRecipeId(recipeEntity.getId());

        verify(recipeIngredientRepository, times(1))
                .saveAll(recipeEntity.getRecipeIngredients());

        verify(recipeStepRepository, times(1))
                .saveAll(recipeEntity.getRecipeSteps());

        assertEquals(
                recipeEntity.getRecipeId(),
                result.get().getRecipeId(),
                "Recipe ID should match");
    }

    @Test
    void testUpdateRecipe_ShouldReturnEmpty_WhenRecipeDoesNotExist() {

        when(recipeRepository.findByRecipeId(recipeId)).thenReturn(Optional.empty());
        when(recipeRepository.save(recipeEntity)).thenReturn(recipeEntity);
        Optional<RecipeEntity> result = recipeRepositoryImpl.updateRecipe(recipeEntity);
        assertTrue(result.isEmpty(), "Should return empty when recipe is not found");
    }
}