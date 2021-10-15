package com.troia.libraryproject.exception;

import org.springframework.http.HttpStatus;

import java.io.Serializable;

public class CustomException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 260809891125711064L;

    private final String message;
    private final HttpStatus httpStatus;

    public CustomException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}