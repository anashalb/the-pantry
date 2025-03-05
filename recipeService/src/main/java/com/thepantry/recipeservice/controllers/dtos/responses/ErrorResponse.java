package com.thepantry.recipeservice.controllers.dtos.responses;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ErrorResponse implements Response {
    private final String message;
    private final String errorCode;
    private final LocalDateTime timestamp;

    public ErrorResponse(String errorCode, String message) {
        this.message = message;
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now();
    }
}