package com.thepantry.recipeservice.domains;

import lombok.NonNull;
import lombok.Value;

@Value(staticConstructor = "of")
public class Category {

    @NonNull
    String name;

    @NonNull
    String description;
}
