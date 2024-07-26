package com.midterm.group4.exception;

public class ObjectNotFoundException extends RuntimeException{
    
    public ObjectNotFoundException(){}
    
    public ObjectNotFoundException(String message){
        super(message);
    }
}
