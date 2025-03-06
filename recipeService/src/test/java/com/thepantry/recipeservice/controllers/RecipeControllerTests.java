package com.thepantry.recipeservice.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thepantry.recipeservice.application.recipes.createRecipe.CreateRecipeDto;
import com.thepantry.recipeservice.application.recipes.createRecipe.CreateRecipeHandler;
import com.thepantry.recipeservice.application.recipes.createRecipe.CreateRecipeRequest;
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

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @MockitoBean
    private GetRecipeHandler getRecipeHandler;

    @MockitoBean
    private CreateRecipeHandler createRecipeHandler;

    @InjectMocks
    private RecipeController recipeController;

    private UUID recipeId;
    private RecipeDetailsDto recipeDetails;

    @BeforeEach
    void setUp() {
        recipeId = UUID.randomUUID();
        recipeDetails = new RecipeDetailsDto(recipeId, "Test Recipe", "Description", 30L, 15L, 45L, (short) 4, null, null);
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
        // Given
        CreateRecipeRequest createRecipeRequest = new CreateRecipeRequest("New Recipe", "A delicious meal", 20L, 10L, 30L, (short) 2);
        CreateRecipeDto createdRecipe = new CreateRecipeDto(recipeId);

        when(createRecipeHandler.handle(any(CreateRecipeRequest.class))).thenReturn(createdRecipe);

        // When & Then
        mockMvc.perform(post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRecipeRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.recipe_id").value(recipeId.toString()));
    }

    @Test
    void createRecipe_ShouldReturnErrorResponse_WhenBusinessRuleExceptionIsThrown() throws Exception {
        // Given
        CreateRecipeRequest createRecipeRequest = new CreateRecipeRequest("Invalid Recipe", "Should fail", 10L, 5L, 15L, (short) 1);
        IBusinessRule mockRule = mock(IBusinessRule.class);
        when(mockRule.getMessage()).thenReturn("Recipe creation rule violated");

        BusinessRuleException exception = new BusinessRuleException(mockRule);

        when(createRecipeHandler.handle(any(CreateRecipeRequest.class))).thenThrow(exception);

        // When & Then
        mockMvc.perform(post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRecipeRequest)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.error_code").value("RECIPE_NOT_CREATED"))
                .andExpect(jsonPath("$.message").value(exception.getMessage()));
    }
}