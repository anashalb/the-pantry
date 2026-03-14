package com.thepantry.recipeservice.controllers;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.thepantry.recipeservice.controllers.dtos.responses.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> messageNotReadableException(HttpMessageNotReadableException ex) {
        if (ex.getCause() instanceof MismatchedInputException cause) {
            String fieldName = extractFieldName(cause);
            String expectedType = cause.getTargetType().getSimpleName().toLowerCase();

            String message = String.format(
                    "Invalid value for key '%s'. Expected %s.",
                    fieldName,
                    expectedType
            );
            return ResponseEntity.badRequest().body(new ErrorResponse("BAD_REQUEST", message));
        }

        return ResponseEntity.badRequest().body(
                new ErrorResponse("BAD_REQUEST", "The request appears to be invalid."));
    }

    private String extractFieldName(MismatchedInputException ex) {
        return ex.getPath().stream()
                .map(ref -> ref.getFieldName() != null
                        ? ref.getFieldName()
                        : "[" + ref.getIndex() + "]")
                .reduce((a, b) -> a + "." + b)
                .orElse("unknown");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex) {
        String error = String.format(
                "Invalid value for path '%s'%s.",
                ex.getName(),
                ex.getRequiredType() != null
                        ? String.format(". Expected %s", ex.getRequiredType().getSimpleName().toLowerCase())
                        : ""
        );

        return ResponseEntity.badRequest().body(new ErrorResponse("BAD_REQUEST", error));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParams(MissingServletRequestParameterException ex) {
        String message = String.format("Missing required parameter '%s'.", ex.getParameterName());
        return ResponseEntity.badRequest().body(new ErrorResponse("BAD_REQUEST", message));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFoundException(NoResourceFoundException ex) {
        ErrorResponse error = new ErrorResponse(
                "NOT_FOUND",
                String.format("Cannot %s %s", ex.getHttpMethod(), ex.getResourcePath()));
        return ResponseEntity.status(404).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        logger.error("Unhandled exception occurred", ex);
        ErrorResponse error = new ErrorResponse("INTERNAL_SERVER_ERROR", "An unexpected error occurred.");
        return ResponseEntity.internalServerError().body(error);
    }
}