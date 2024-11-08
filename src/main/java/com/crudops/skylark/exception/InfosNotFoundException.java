package com.crudops.skylark.exception;

public class InfosNotFoundException extends RuntimeException {

    public InfosNotFoundException(String message) {
        super(message);
    }

    public InfosNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}