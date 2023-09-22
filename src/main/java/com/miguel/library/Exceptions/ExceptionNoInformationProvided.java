package com.miguel.library.Exceptions;

public class ExceptionNoInformationProvided extends RuntimeException {
    public ExceptionNoInformationProvided(String message) {
        super(message);
    }

    public ExceptionNoInformationProvided(String message, Throwable cause) {
        super(message, cause);
    }

}
