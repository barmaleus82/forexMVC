package com.spring.forex.exception;

public class ExceptionJDBC extends RuntimeException{
    public ExceptionJDBC() {
    }

    public ExceptionJDBC(String message) {
        super(message);
    }
}
