package com.scheduler.Base.Exception;

public class ErrorException extends RuntimeException{
    
    private String message;
    public ErrorException(String message){
        super(message);
        this.message=message;
    }
    public String getMessage() {
        return message;
    }
}
