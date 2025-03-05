package com.thepantry.recipeservice.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "recipe", schema = "recipes")
public class RecipeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column(name = "recipe_id", nullable = false, unique = true)
    private UUID recipeId;

    @Column(name = "created_by")
    private UUID createdBy;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "servings")
    private Short servings;

    @Column(name = "cooking_time_minutes")
    private Long cookingTimeMinutes;

    @Column(name = "ready_in_time_minutes")
    private Long readyInTimeMinutes;

    @Column(name = "preparation_time_minutes")
    private Long preparationTimeMinutes;

    @OneToMany(mappedBy = "recipe")
    private List<RecipeIngredientEntity> recipeIngredients = new ArrayList<>();

    @OneToMany(mappedBy = "recipe")
    private List<RecipeStepEntity> recipeSteps = new ArrayList<>();

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private Instant createdAt;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private Instant updatedAt;

}