package com.miguel.library.Exceptions;

public class ExceptionHasRelatedObjects extends RuntimeException {

    public ExceptionHasRelatedObjects(String message) { super(message); }

    public ExceptionHasRelatedObjects(String message, Throwable cause) { super(message, cause); }
}
