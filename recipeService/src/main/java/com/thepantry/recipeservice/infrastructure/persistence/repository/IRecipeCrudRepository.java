package com.thepantry.recipeservice.infrastructure.persistence.repository;

import com.thepantry.recipeservice.infrastructure.persistence.entities.RecipeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface IRecipeCrudRepository extends CrudRepository<RecipeEntity, Long>, PagingAndSortingRepository<RecipeEntity, Long> {

    Optional<RecipeEntity> findByRecipeId(UUID recipeId);

    Page<RecipeEntity> findAllByCreatedBy(UUID createdBy, Pageable pageable);

    @Transactional
    Integer deleteByRecipeId(UUID recipeId);
}
