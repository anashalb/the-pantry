package com.thepantry.recipeservice.controllers;

import com.thepantry.recipeservice.application.recipes.getRecipesCreatedByUser.GetRecipesCreatedByUserDto;
import com.thepantry.recipeservice.application.recipes.getRecipesCreatedByUser.GetRecipesCreatedByUserHandler;
import com.thepantry.recipeservice.application.recipes.getRecipesCreatedByUser.PaginatedResult;
import com.thepantry.recipeservice.application.recipes.getRecipesCreatedByUser.UserRecipeDto;
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

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class UserControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GetRecipesCreatedByUserHandler getRecipesCreatedByUserHandler;

    @InjectMocks
    private UserController userController;

    private UUID userId;
    private PaginatedResult<UserRecipeDto> mockRecipes;

    @BeforeEach
    void setUp() {
//        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
        userId = UUID.randomUUID();

        UserRecipeDto recipe1 = new UserRecipeDto(UUID.randomUUID(), "Recipe 1", "Description 1", 30L, 10L, 40L, (short) 2);
        UserRecipeDto recipe2 = new UserRecipeDto(UUID.randomUUID(), "Recipe 2", "Description 2", 45L, 15L, 60L, (short) 4);

        mockRecipes = new PaginatedResult<>(0, 2, List.of(recipe1, recipe2));
    }

    @Test
    void getRecipesCreatedByUser_ShouldReturnSuccessResponse_WhenRecipesExist() throws Exception {
        // Arrange
        when(getRecipesCreatedByUserHandler.handle(any(GetRecipesCreatedByUserDto.class))).thenReturn(mockRecipes);

        // Act & Assert
        mockMvc.perform(get("/users/{userId}/recipes", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items").isArray())
                .andExpect(jsonPath("$.data.items.length()").value(2))
                .andExpect(jsonPath("$.data.items[0].recipe_id").value(mockRecipes.getItems().get(0).getRecipeId().toString()))
                .andExpect(jsonPath("$.data.items[0].name").value(mockRecipes.getItems().get(0).getName()))
                .andExpect(jsonPath("$.data.items[1].recipe_id").value(mockRecipes.getItems().get(1).getRecipeId().toString()))
                .andExpect(jsonPath("$.data.items[1].name").value(mockRecipes.getItems().get(1).getName()))
                .andExpect(jsonPath("$.data.count").value(mockRecipes.getCount()))
                .andExpect(jsonPath("$.data.total").value(mockRecipes.getTotal()));
    }

    @Test
    void getRecipesCreatedByUser_ShouldReturnEmptyList_WhenNoRecipesExist() throws Exception {
        // Arrange
        PaginatedResult<UserRecipeDto> result = new PaginatedResult<>(0, 0, List.of());
        when(getRecipesCreatedByUserHandler.handle(any(GetRecipesCreatedByUserDto.class))).thenReturn(result);

        // Act & Assert
        mockMvc.perform(get("/users/{userId}/recipes", userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.items").isArray())
                .andExpect(jsonPath("$.data.items.length()").value(0))
                .andExpect(jsonPath("$.data.count").value(result.getCount()))
                .andExpect(jsonPath("$.data.total").value(result.getTotal()));

    }
}