package com.thepantry.recipeservice.controllers;

import com.thepantry.recipeservice.application.recipes.deleteRecipe.DeleteRecipeHandler;
import com.thepantry.recipeservice.application.recipes.getRecipeDetails.GetRecipeHandler;
import com.thepantry.recipeservice.application.recipes.upsertRecipe.UpsertRecipeHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecipeController.class)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class GlobalExceptionHandlerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GetRecipeHandler getRecipeHandler;

    @MockitoBean
    private UpsertRecipeHandler upsertRecipeHandler;

    @MockitoBean
    private DeleteRecipeHandler deleteRecipeHandler;

    @Test
    void shouldReturn400_WhenJsonFieldHasWrongType() throws Exception {
        String body = """
                {
                  "name": "Test",
                  "description": "Desc",
                  "cooking_time_minutes": "not-a-number",
                  "preparation_time_minutes": 10,
                  "ready_in_time_minutes": 20,
                  "servings": 2,
                  "ingredients": [],
                  "steps": []
                }
                """;

        mockMvc.perform(post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Invalid value for key 'cooking_time_minutes'. Expected long."));
    }

    @Test
    void shouldReturn400_WhenJsonIsMalformed() throws Exception {
        mockMvc.perform(post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{invalid json}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("The request appears to be invalid."));
    }

    @Test
    void shouldReturn400_WhenPathVariableHasWrongType() throws Exception {
        mockMvc.perform(get("/recipes/not-a-uuid")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error_code").value("BAD_REQUEST"))
                .andExpect(jsonPath("$.message").value("Invalid value for path 'recipeId'. Expected uuid."));
    }

    @Test
    void shouldReturn404_WhenRouteDoesNotExist() throws Exception {
        mockMvc.perform(get("/unknown/route")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error_code").value("NOT_FOUND"));
    }

    @Test
    void shouldReturn500_WhenUnhandledExceptionIsThrown() throws Exception {
        when(getRecipeHandler.handle(any())).thenThrow(new RuntimeException("Unexpected failure"));

        mockMvc.perform(get("/recipes/{recipeId}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error_code").value("INTERNAL_SERVER_ERROR"))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred."));
    }
}
