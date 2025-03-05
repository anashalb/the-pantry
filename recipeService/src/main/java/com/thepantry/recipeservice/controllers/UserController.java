package com.thepantry.recipeservice.controllers;

import com.thepantry.recipeservice.application.recipes.getRecipesCreatedByUser.GetRecipesCreatedByUserDto;
import com.thepantry.recipeservice.application.recipes.getRecipesCreatedByUser.GetRecipesCreatedByUserHandler;
import com.thepantry.recipeservice.application.recipes.getRecipesCreatedByUser.PaginatedResult;
import com.thepantry.recipeservice.application.recipes.getRecipesCreatedByUser.UserRecipeDto;
import com.thepantry.recipeservice.controllers.dtos.responses.SuccessResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final GetRecipesCreatedByUserHandler getRecipesCreatedByUserHandler;

    public UserController(GetRecipesCreatedByUserHandler getRecipesCreatedByUserHandler) {
        this.getRecipesCreatedByUserHandler = getRecipesCreatedByUserHandler;
    }

    @GetMapping("/{userId}/recipes")
    public ResponseEntity<?> getRecipesCreatedByUser(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort
    ) {

        //TODO: Check if the user exists first using authentication

        PaginatedResult<UserRecipeDto> recipes = this.getRecipesCreatedByUserHandler.handle(
                new GetRecipesCreatedByUserDto(userId, page, size, sort));

        return ResponseEntity.ok(new SuccessResponse<>(recipes));
    }
}
