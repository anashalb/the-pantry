package com.thepantry.recipeservice.controllers;

import com.thepantry.recipeservice.application.recipes.upsertRecipe.UpsertRecipeDto;
import com.thepantry.recipeservice.application.recipes.upsertRecipe.UpsertRecipeHandler;
import com.thepantry.recipeservice.application.recipes.RecipeRequest;
import com.thepantry.recipeservice.application.recipes.upsertRecipe.RecipeCannotBeUpdatedException;
import com.thepantry.recipeservice.application.recipes.deleteRecipe.DeleteRecipeHandler;
import com.thepantry.recipeservice.application.recipes.deleteRecipe.DeleteRecipeRequest;
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
    private final UpsertRecipeHandler upsertRecipeHandler;
    private final DeleteRecipeHandler deleteRecipeHandler;

    public RecipeController(
            GetRecipeHandler getRecipeHandler,
            UpsertRecipeHandler upsertRecipeHandler,
            DeleteRecipeHandler deleteRecipeHandler) {
        this.getRecipeHandler = getRecipeHandler;
        this.upsertRecipeHandler = upsertRecipeHandler;
        this.deleteRecipeHandler = deleteRecipeHandler;
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

    @PostMapping(value = "")
    public ResponseEntity<?> createRecipe(@RequestBody RecipeRequest recipeRequest) {

        //TODO: Map user to the recipe
        try {
            UpsertRecipeDto recipe = this.upsertRecipeHandler.handle(recipeRequest, null);
            return ResponseEntity.ok(new SuccessResponse<>(recipe));
        } catch (BusinessRuleException e) {
            return ResponseEntity.unprocessableEntity().body(
                    new ErrorResponse("RECIPE_NOT_CREATED", e.getMessage()));
        }
    }

    @PutMapping(value = "/{recipeId}")
    public ResponseEntity<?> updateRecipe(
            @RequestBody RecipeRequest recipeRequest,
            @PathVariable UUID recipeId) {

        //TODO: Map user to the recipe
        try {
            UpsertRecipeDto recipe = this.upsertRecipeHandler.handle(recipeRequest, recipeId);
            return ResponseEntity.ok(new SuccessResponse<>(recipe));
        } catch (BusinessRuleException | RecipeCannotBeUpdatedException e) {
            return ResponseEntity.unprocessableEntity().body(
                    new ErrorResponse("RECIPE_NOT_UPDATED", e.getMessage()));
        }
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<?> deleteRecipe(@PathVariable UUID recipeId) {

        //TODO: Check that user can delete the recipe
        DeleteRecipeRequest deleteRecipeRequest = new DeleteRecipeRequest(recipeId);
        boolean isDeleted = this.deleteRecipeHandler.handle(deleteRecipeRequest);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(404).body(new ErrorResponse(
                    "RECIPE_NOT_DELETED",
                    "Recipe does not exist or could not be deleted"));
        }
    }
}