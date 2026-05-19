package com.project.exception;

public class QuotaUnavailableException extends RuntimeException {
    public QuotaUnavailableException(String message) {
        super(message);
    }
}
