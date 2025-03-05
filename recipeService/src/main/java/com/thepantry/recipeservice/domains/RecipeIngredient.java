package com.thepantry.recipeservice.domains;

import lombok.ToString;
import lombok.Value;

@Value(staticConstructor = "of")
@ToString
public class RecipeIngredient {

    Ingredient ingredient;
    String preparationMethod;
    double quantity;
    MeasurementUnit measurementUnit;
//
//  public static RecipeIngredient of(Ingredient ingredient, MeasurementUnit measurementUnit, double quantity) {
//
//    if (quantity < 1) {
//      throw new IllegalArgumentException("Quantity cannot be less than 1");
//    }
//
//    if (ingredient.getMeasurementType() != measurementUnit.getMeasurementType()) {
//      throw new IllegalArgumentException("Ingredient must be measured by the same measurement unit type");
//    }
//
//    return new RecipeIngredient(ingredient, measurementUnit, quantity);
//  }
}
