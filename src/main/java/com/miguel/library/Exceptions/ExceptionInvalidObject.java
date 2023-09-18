package com.miguel.library.Exceptions;

public class ExceptionInvalidObject extends RuntimeException{

    public ExceptionInvalidObject(String message) {
        super(message);
    }
    public ExceptionInvalidObject(String message, Throwable cause) {
        super(message, cause);
    }
}
