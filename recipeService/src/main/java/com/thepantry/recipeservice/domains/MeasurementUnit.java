package com.thepantry.recipeservice.domains;

import lombok.NonNull;
import lombok.ToString;
import lombok.Value;

@Value(staticConstructor = "of")
@ToString
public class MeasurementUnit {

    @NonNull
    String name;
}
