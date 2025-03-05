package com.thepantry.recipeservice.domains;

import com.thepantry.recipeservice.domains.businessRules.*;
import com.thepantry.recipeservice.domains.common.BusinessRuleException;
import com.thepantry.recipeservice.domains.common.DomainModel;
import lombok.Getter;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class Recipe extends DomainModel {

    RecipeId recipeId;
    String name;
    String description;
    Duration cookingTimeMinutes;
    Duration preparationTimeMinutes;
    Duration ReadyInTimeMinutes;
    Short servings;
    List<RecipeIngredient> ingredients;
    List<RecipeStep> steps;
    User creator;

    private Recipe() {
        this.recipeId = new RecipeId();
        this.ingredients = new ArrayList<>();
        this.steps = new ArrayList<>();
    }

    // Business Methods
    public static Recipe create(
            String name,
            String description,
            Duration cookingTimeMinutes,
            Duration preparationTimeMinutes,
            Duration readyInTimeMinutes,
            Short servings
    ) throws BusinessRuleException {

        Recipe recipe = new Recipe();
        recipe.setName(name);
        recipe.setDescription(description);
        recipe.setCookingTimeMinutes(cookingTimeMinutes);
        recipe.setPreparationTimeMinutes(preparationTimeMinutes);
        recipe.setReadyInTimeMinutes(readyInTimeMinutes);
        recipe.setServings(servings);

        return recipe;
    }

    private void setName(String name) throws BusinessRuleException {
        checkRule(new RecipeNameLengthShouldBeAtLeastThreeChars(name));
        this.name = name;
    }

    private void setDescription(String description) throws BusinessRuleException {
        checkRule(new RecipeDescriptionLengthShouldBeAtLeastThreeChars(description));
        this.description = description;
    }

    private void setServings(Short servings) throws BusinessRuleException {
        checkRule(new ServingsMustBeAtLeastOne(servings));
        this.servings = servings;
    }

    private void setPreparationTimeMinutes(Duration preparationTimeMinutes) throws BusinessRuleException {
        checkRule(new PreparationTimeMustBeAtLeastOneMinute(preparationTimeMinutes));
        this.preparationTimeMinutes = preparationTimeMinutes;
    }

    private void setCookingTimeMinutes(Duration cookingTimeMinutes) throws BusinessRuleException {
        checkRule(new CookingTimeMustBeAtLeastOneMinute(cookingTimeMinutes));
        this.cookingTimeMinutes = cookingTimeMinutes;
    }

    private void setReadyInTimeMinutes(Duration readyInTimeMinutes) throws BusinessRuleException {
        checkRule(new ReadyInTimeMustBeAtLeastOneMinute(readyInTimeMinutes));
        this.ReadyInTimeMinutes = readyInTimeMinutes;
    }

    public void addIngredient(RecipeIngredient recipeIngredient) throws BusinessRuleException {
        ingredients.add(recipeIngredient);
    }

    public void addStep(RecipeStep step) {
        this.steps.add(step);
    }

    public List<RecipeIngredient> getIngredients() {
        return Collections.unmodifiableList(ingredients);
    }
}
