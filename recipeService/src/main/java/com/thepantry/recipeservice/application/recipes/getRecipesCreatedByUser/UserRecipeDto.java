package com.thepantry.recipeservice.application.recipes.getRecipesCreatedByUser;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class UserRecipeDto {

    private UUID recipeId;
    private String name;
    private String description;
    private Long cookingTimeMinutes;
    private Long preparationTimeMinutes;
    private Long ReadyInTimeMinutes;
    private Short servings;
}
