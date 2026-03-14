package com.thepantry.recipeservice.application.recipes.getRecipesCreatedByUser;

import com.thepantry.recipeservice.application.recipes.IRecipeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class GetRecipesCreatedByUserHandler {

    private final IRecipeRepository recipeRepository;

    public GetRecipesCreatedByUserHandler(IRecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public PaginatedResult<UserRecipeDto> handle(GetRecipesCreatedByUserDto recipesCreatedByUserDto) {
        Sort sort = Sort.by(Sort.Direction.DESC, recipesCreatedByUserDto.getSortBy());
        Pageable pageRequest = PageRequest.of(
                recipesCreatedByUserDto.getPageNumber(),
                recipesCreatedByUserDto.getPageSize(),
                sort);

        Page<UserRecipeDto> userRecipes = recipeRepository.getRecipesCreatedByUserId(
                recipesCreatedByUserDto.getUserId(),
                pageRequest
        );

        return new PaginatedResult<>(
                userRecipes.getTotalElements(), userRecipes.getContent().size(), userRecipes.getContent()
        );
    }
}
