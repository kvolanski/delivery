package com.kvolanski.delivery.delivery.tracking.domain.exception;

public class DomainException extends RuntimeException{

    public DomainException() {
        super();
    }

    public DomainException(String message) {
        super(message);
    }

    public DomainException(String message, Throwable cause) {
        super(message, cause);
    }

    public DomainException(String s, Object o, String s1) {
    }
}
