package com.crudops.skylark.exception;

import org.springframework.http.HttpStatus;

public class InfosException {


    private final String message;
    private final Throwable throwable;
    private final HttpStatus httpStatus;


    public InfosException(String message, Throwable throwable, HttpStatus httpStatus) {
        this.message = message;
        this.throwable = throwable;
        this.httpStatus = httpStatus;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }




}
