package com.miguel.library.Exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private String message;
    private Throwable throwable;
    private HttpStatus status;
    private ZonedDateTime timestamp;
    private List<ValidationError> errors;

    public ErrorResponse(String message, Throwable throwable, HttpStatus status, ZonedDateTime timestamp) {
        this.message = message;
        this.throwable = throwable;
        this.status = status;
        this.timestamp = timestamp;
    }

    @Getter @Setter
    @AllArgsConstructor
    private static class ValidationError {
        private final String field;
        private final String message;
    }

    public void addValidationError(String field, String message){
        if (Objects.isNull(this.errors)){
            errors = new ArrayList<>();
        }
        errors.add(new ValidationError(field, message));
    }
}
