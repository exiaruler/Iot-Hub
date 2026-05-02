package com.scheduler.Base.Exception;

import java.util.Map;

public class ValidationException extends RuntimeException {
    private final Map<String, String> errors;

    public ValidationException(Map<String,String> errors){
        super("Validation failed for 1 or more fields");
        this.errors = errors;
    }
    public Map<String, String> getErrors() {
        return errors;
    }
}
