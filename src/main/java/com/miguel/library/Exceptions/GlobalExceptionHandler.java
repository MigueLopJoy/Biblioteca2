package com.miguel.library.Exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZonedDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @Override
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request
    ) {
        HttpStatus httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;

        ErrorResponse errorResponse = new ErrorResponse(
                "Validation error. Check 'errors' field for details.",
                HttpStatus.UNPROCESSABLE_ENTITY
            );

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = ExceptionObjectNotFound.class)
    public ResponseEntity<Object> handleExceptionObjectNotFound(ExceptionObjectNotFound ex) {

        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        ErrorResponse errorResponse = new ErrorResponse(
            ex.getMessage(),
                httpStatus
        );

        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = ExceptionNoSearchResultsFound.class)
    public ResponseEntity<Object> handleExceptionNoSearchResultsFound(ExceptionNoSearchResultsFound ex) {

        HttpStatus httpStatus = HttpStatus.NOT_FOUND;

        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                httpStatus
        );

        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    @ExceptionHandler(ExceptionObjectAlreadyExists.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<Object> handleExceptionObjectAlreadyExists(ExceptionObjectAlreadyExists ex) {

        HttpStatus httpStatus = HttpStatus.CONFLICT;

        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                httpStatus
        );

        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    @ExceptionHandler(ExceptionNullObject.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleExceptionNullObject(ExceptionNullObject ex) {

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                httpStatus
        );

        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    @ExceptionHandler(ExceptionNoInformationProvided.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public  ResponseEntity<Object> handleExceptionNoInformationProvided(ExceptionNoInformationProvided ex) {

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                httpStatus
        );

        return new ResponseEntity<>(errorResponse, httpStatus);

    }

    @ExceptionHandler(ExceptionInvalidObject.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> handleInvalidObject(ExceptionInvalidObject ex) {

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                httpStatus
        );

        return new ResponseEntity<>(errorResponse, httpStatus);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleExceptionAllUncaught(Exception ex) {

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(),
                httpStatus
        );

        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}
