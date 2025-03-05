package com.thepantry.recipeservice.controllers.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SuccessResponse<T> implements Response {
    private final T data;
}
