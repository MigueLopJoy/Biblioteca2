package com.miguel.library.Exceptions;


public class ExceptionNullObject extends RuntimeException{

    public ExceptionNullObject(String message) {
        super(message);
    }

    public ExceptionNullObject(String message, Throwable cause) {
        super(message, cause);
    }
}
