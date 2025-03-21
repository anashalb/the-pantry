package com.thepantry.recipeservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thepantry.recipeservice.application.recipes.RecipeRequest;
import com.thepantry.recipeservice.application.recipes.RecipeIngredientRequest;
import com.thepantry.recipeservice.application.recipes.RecipeStepRequest;
import com.thepantry.recipeservice.application.recipes.upsertRecipe.*;
import com.thepantry.recipeservice.application.recipes.deleteRecipe.DeleteRecipeHandler;
import com.thepantry.recipeservice.application.recipes.deleteRecipe.DeleteRecipeRequest;
import com.thepantry.recipeservice.application.recipes.getRecipeDetails.GetRecipeDto;
import com.thepantry.recipeservice.application.recipes.getRecipeDetails.GetRecipeHandler;
import com.thepantry.recipeservice.application.recipes.getRecipeDetails.RecipeDetailsDto;
import com.thepantry.recipeservice.application.recipes.getRecipeDetails.RecipeNotFoundException;
import com.thepantry.recipeservice.domains.common.BusinessRuleException;
import com.thepantry.recipeservice.domains.common.IBusinessRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecipeController.class)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class RecipeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private RecipeController recipeController;

    @MockitoBean
    private GetRecipeHandler getRecipeHandler;

    @MockitoBean
    private UpsertRecipeHandler upsertRecipeHandler;

    @MockitoBean
    private DeleteRecipeHandler deleteRecipeHandler;

    private UUID recipeId;
    private RecipeDetailsDto recipeDetails;
    private List<RecipeIngredientRequest> recipeIngredients;
    private List<RecipeStepRequest> recipeSteps;

    @BeforeEach
    void setUp() {
        recipeId = UUID.randomUUID();
        recipeDetails = new RecipeDetailsDto(recipeId, "Test Recipe", "Description", 30L, 15L, 45L, (short) 4, null, null);
        recipeIngredients = new ArrayList<>();
        recipeSteps = new ArrayList<>();
    }

    @Test
    void getRecipeById_ShouldReturnSuccessResponse_WhenRecipeExists() throws Exception {

        when(getRecipeHandler.handle(any(GetRecipeDto.class))).thenReturn(recipeDetails);

        mockMvc.perform(get("/recipes/{recipeId}", recipeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.recipe_id").value(recipeId.toString()))
                .andExpect(jsonPath("$.data.name").value("Test Recipe"))
                .andExpect(jsonPath("$.data.description").value("Description"))
                .andExpect(jsonPath("$.data.cooking_time_minutes").value(30))
                .andExpect(jsonPath("$.data.preparation_time_minutes").value(15))
                .andExpect(jsonPath("$.data.ready_in_time_minutes").value(45))
                .andExpect(jsonPath("$.data.servings").value(4));
    }

    @Test
    void getRecipeById_ShouldReturnErrorResponse_WhenRecipeNotFound() throws Exception {

        RecipeNotFoundException exception = new RecipeNotFoundException(recipeId);

        when(getRecipeHandler.handle(any(GetRecipeDto.class)))
                .thenThrow(exception);

        mockMvc.perform(get("/recipes/{recipeId}", recipeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error_code").value("RECIPE_NOT_FOUND"))
                .andExpect(jsonPath("$.message").value(exception.getMessage()));
    }

    @Test
    void createRecipe_ShouldReturnSuccessResponse_WhenRecipeIsCreated() throws Exception {

        RecipeRequest recipeRequest = new RecipeRequest(
                "New Recipe",
                "A delicious meal",
                20L,
                10L,
                30L,
                (short) 2,
                recipeIngredients,
                recipeSteps);

        UpsertRecipeDto createdRecipe = new UpsertRecipeDto(recipeId);

        when(upsertRecipeHandler.handle(any(RecipeRequest.class), eq(null))).thenReturn(createdRecipe);

        mockMvc.perform(post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.recipe_id").value(recipeId.toString()));
    }

    @Test
    void createRecipe_ShouldReturnErrorResponse_WhenBusinessRuleExceptionIsThrown() throws Exception {

        RecipeRequest recipeRequest = new RecipeRequest(
                "Invalid Recipe",
                "Should fail",
                10L,
                5L,
                15L, (short) 1,
                recipeIngredients,
                recipeSteps);

        IBusinessRule mockRule = mock(IBusinessRule.class);
        when(mockRule.getMessage()).thenReturn("Recipe creation rule violated");

        BusinessRuleException exception = new BusinessRuleException(mockRule);

        when(upsertRecipeHandler.handle(any(RecipeRequest.class), eq(null))).thenThrow(exception);

        mockMvc.perform(post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipeRequest)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error_code").value("RECIPE_NOT_CREATED"))
                .andExpect(jsonPath("$.message").value(exception.getMessage()));
    }

    @Test
    void updateRecipe_ShouldReturnSuccessResponse_WhenRecipeIsUpdated() throws Exception {

        UUID recipeId = UUID.randomUUID();
        RecipeRequest recipeRequest = new RecipeRequest(
                "Existing Recipe",
                "A delicious meal",
                20L,
                10L,
                30L,
                (short) 2,
                recipeIngredients,
                recipeSteps);

        UpsertRecipeDto updatedRecipe = new UpsertRecipeDto(recipeId);

        when(upsertRecipeHandler.handle(recipeRequest, recipeId)).thenReturn(updatedRecipe);

        mockMvc.perform(put("/recipes/{recipeId}", recipeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.recipe_id").value(recipeId.toString()));
    }

    @Test
    void updateRecipe_ShouldReturnErrorResponse_WhenRecipeNotFound() throws Exception {

        UUID recipeId = UUID.randomUUID();
        RecipeRequest recipeRequest = new RecipeRequest(
                "Non-existent Recipe",
                "Will not be found",
                20L,
                10L,
                30L,
                (short) 2,
                recipeIngredients,
                recipeSteps);

        RecipeCannotBeUpdatedException exception = new RecipeCannotBeUpdatedException(recipeId);

        when(upsertRecipeHandler.handle(any(RecipeRequest.class), eq(recipeId)))
                .thenThrow(exception);

        mockMvc.perform(put("/recipes/{recipeId}", recipeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipeRequest)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error_code").value("RECIPE_NOT_UPDATED"))
                .andExpect(jsonPath("$.message").value(exception.getMessage()));
    }

    @Test
    void updateRecipe_ShouldReturnErrorResponse_WhenBusinessRuleExceptionIsThrown() throws Exception {
        UUID recipeId = UUID.randomUUID();
        RecipeRequest recipeRequest = new RecipeRequest(
                "Invalid recipe",
                "Should fail",                20L,
                10L,
                30L,
                (short) 2,
                recipeIngredients,
                recipeSteps);

        IBusinessRule mockRule = mock(IBusinessRule.class);
        when(mockRule.getMessage()).thenReturn("Recipe update rule violated");

        BusinessRuleException exception = new BusinessRuleException(mockRule);

        when(upsertRecipeHandler.handle(any(RecipeRequest.class), eq(recipeId)))
                .thenThrow(exception);

        mockMvc.perform(put("/recipes/{recipeId}", recipeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(recipeRequest)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error_code").value("RECIPE_NOT_UPDATED"))
                .andExpect(jsonPath("$.message").value(exception.getMessage()));
    }

    @Test
    void deleteRecipe_ShouldReturnSuccessResponse_WhenRecipeIsDeleted() throws Exception {
        UUID recipeId = UUID.randomUUID();
        DeleteRecipeRequest request = new DeleteRecipeRequest(recipeId);
        when(deleteRecipeHandler.handle(request)).thenReturn(true);

        mockMvc.perform(delete("/recipes/{recipeId}", recipeId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteRecipe_ShouldReturnErrorResponse_WhenRecipeNotFound() throws Exception {
        UUID recipeId = UUID.randomUUID();
        DeleteRecipeRequest request = new DeleteRecipeRequest(recipeId);
        when(deleteRecipeHandler.handle(request)).thenReturn(false);

        mockMvc.perform(delete("/recipes/{recipeId}", recipeId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error_code").value("RECIPE_NOT_DELETED"))
                .andExpect(jsonPath("$.message").value("Recipe does not exist or could not be deleted"));
    }
}