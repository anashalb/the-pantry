package com.thepantry.recipeservice.controllers;

import com.thepantry.recipeservice.application.recipes.createRecipe.CreateRecipeDto;
import com.thepantry.recipeservice.application.recipes.createRecipe.CreateRecipeHandler;
import com.thepantry.recipeservice.application.recipes.createRecipe.CreateRecipeRequest;
import com.thepantry.recipeservice.application.recipes.getRecipeDetails.GetRecipeDto;
import com.thepantry.recipeservice.application.recipes.getRecipeDetails.GetRecipeHandler;
import com.thepantry.recipeservice.application.recipes.getRecipeDetails.RecipeDetailsDto;
import com.thepantry.recipeservice.application.recipes.getRecipeDetails.RecipeNotFoundException;
import com.thepantry.recipeservice.controllers.dtos.responses.ErrorResponse;
import com.thepantry.recipeservice.controllers.dtos.responses.SuccessResponse;
import com.thepantry.recipeservice.domains.common.BusinessRuleException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/recipes")
public class RecipeController {

    private final GetRecipeHandler getRecipeHandler;
    private final CreateRecipeHandler createRecipeHandler;

    public RecipeController(GetRecipeHandler getRecipeHandler, CreateRecipeHandler createRecipeHandler) {
        this.getRecipeHandler = getRecipeHandler;
        this.createRecipeHandler = createRecipeHandler;
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<?> getRecipeById(@PathVariable UUID recipeId) {

        try {
            RecipeDetailsDto recipeDetails = getRecipeHandler.handle(new GetRecipeDto(recipeId));
            return ResponseEntity.ok(new SuccessResponse<>(recipeDetails));
        } catch (RecipeNotFoundException e) {
            return ResponseEntity.status(404).body(new ErrorResponse("RECIPE_NOT_FOUND", e.getMessage()));
        }
    }

    @PostMapping("")
    public ResponseEntity<?> createRecipe(@RequestBody CreateRecipeRequest createRecipeRequest) {

        //TODO: Map user to the recipe

        SuccessResponse<RecipeDetailsDto> response;

        try {
            CreateRecipeDto recipe = this.createRecipeHandler.handle(createRecipeRequest);
            return ResponseEntity.ok(new SuccessResponse<>(recipe));
        } catch (BusinessRuleException e) {
            return ResponseEntity.unprocessableEntity().body(
                    new ErrorResponse("RECIPE_NOT_CREATED", e.getMessage()));
        }
    }
}