package com.expertise.system.exception;

public class ApplicationProcessingException extends Exception {
    public ApplicationProcessingException(String message) {
        super(message);
    }

    public ApplicationProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}