package com.thepantry.recipeservice.application.recipes.getRecipeDetails;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
public class GetRecipeDto {

    private UUID recipeId;
}
