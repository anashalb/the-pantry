package com.thepantry.recipeservice.application.recipes.getRecipesCreatedByUser;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class PaginatedResult<T> {

    private final long total;
    private final long count;
    private List<T> items;
}
