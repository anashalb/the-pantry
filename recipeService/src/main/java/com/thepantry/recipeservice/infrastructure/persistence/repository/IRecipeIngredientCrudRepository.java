package com.thepantry.recipeservice.infrastructure.persistence.repository;

import com.thepantry.recipeservice.infrastructure.persistence.entities.RecipeIngredientEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IRecipeIngredientCrudRepository extends CrudRepository<RecipeIngredientEntity, Long> {

    List<RecipeIngredientEntity> findAllByRecipeIdOrderByIdAsc(Long recipeId);

    void deleteAllByRecipeId(Long recipeId);
}
