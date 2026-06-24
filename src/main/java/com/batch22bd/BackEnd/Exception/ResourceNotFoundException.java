package com.batch22bd.BackEnd.Exception;

public class ResourceNotFoundException extends RuntimeException{

    public ResourceNotFoundException(String resourceName, String fieldName, String value) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, value));
    }

}
