package com.thepantry.recipeservice.application.recipes.getRecipesCreatedByUser;

import com.thepantry.recipeservice.application.recipes.IRecipeRepository;
import com.thepantry.recipeservice.infrastructure.persistence.entities.RecipeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

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

        Page<RecipeEntity> userRecipes = recipeRepository.getRecipesCreatedByUserId(
                recipesCreatedByUserDto.getUserId(),
                pageRequest
        );

        List<UserRecipeDto> userRecipeDtos = userRecipes.stream().map(recipeEntity -> {
            return new UserRecipeDto(
                    recipeEntity.getRecipeId(),
                    recipeEntity.getName(),
                    recipeEntity.getDescription(),
                    recipeEntity.getCookingTimeMinutes(),
                    recipeEntity.getPreparationTimeMinutes(),
                    recipeEntity.getReadyInTimeMinutes(),
                    recipeEntity.getServings()
            );
        }).toList();

        return new PaginatedResult<>(
                userRecipes.getTotalElements(), userRecipeDtos.size(), userRecipeDtos
        );
    }
}