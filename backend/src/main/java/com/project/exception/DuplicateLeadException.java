package com.project.exception;

public class DuplicateLeadException extends RuntimeException {
    public DuplicateLeadException() {
        super("A lead already exists for this phone number and service type");
    }
}
