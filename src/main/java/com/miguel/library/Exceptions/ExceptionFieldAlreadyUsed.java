package com.miguel.library.Exceptions;

public class ExceptionFieldAlreadyUsed extends RuntimeException {

    public ExceptionFieldAlreadyUsed(String message) {
        super(message);
    };

    public ExceptionFieldAlreadyUsed(String message, Throwable cause) {
        super(message, cause);
    }

}
