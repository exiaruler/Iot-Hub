package com.scheduler.Base.Exception;

import java.util.Map;

public class ValidationException extends RuntimeException {
    private final Map<String, String> errors;
    private final Map<String, String> warnings;
    public ValidationException(Map<String,String> errors, Map<String, String> warnings){
        super("Validation failed for 1 or more fields");
        this.errors = errors;
        this.warnings = warnings;
    }
    public Map<String, String> getErrors() {
        return errors;
    }
    public Map<String, String> getWarnings() {
        return warnings;
    }
}
