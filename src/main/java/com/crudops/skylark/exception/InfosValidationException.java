package com.crudops.skylark.exception;



public class InfosValidationException extends RuntimeException {

    public InfosValidationException(String message) {
        super(message);
    }

    public InfosValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
