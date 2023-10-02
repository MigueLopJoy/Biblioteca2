package com.miguel.library.Exceptions;

public class ExceptionNoSearchResultsFound extends RuntimeException {

    public ExceptionNoSearchResultsFound(String message) {
        super(message);
    }

    public ExceptionNoSearchResultsFound(String message, Throwable cause) {
        super(message, cause);
    }
}
