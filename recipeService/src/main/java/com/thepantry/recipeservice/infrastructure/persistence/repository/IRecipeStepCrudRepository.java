package com.thepantry.recipeservice.infrastructure.persistence.repository;

import com.thepantry.recipeservice.infrastructure.persistence.entities.RecipeStepEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IRecipeStepCrudRepository extends CrudRepository<RecipeStepEntity, Long> {

    List<RecipeStepEntity> findAllByRecipeIdOrderByStepNumberAsc(Long id);

    void deleteAllByRecipeId(Long recipeId);
}
