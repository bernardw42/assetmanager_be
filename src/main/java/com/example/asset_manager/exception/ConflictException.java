package com.example.asset_manager.exception;

import java.util.Map;

public class ConflictException extends RuntimeException {
    private final Map<String, String> fieldErrors;

    public ConflictException(String message) {
        super(message);
        this.fieldErrors = null;
    }

    public ConflictException(String message, Map<String, String> fieldErrors) {
        super(message);
        this.fieldErrors = fieldErrors;
    }

    public Map<String, String> getFieldErrors() {
        return fieldErrors;
    }
}
