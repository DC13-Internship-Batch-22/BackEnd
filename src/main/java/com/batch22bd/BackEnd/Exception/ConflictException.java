package com.batch22bd.BackEnd.Exception;

public class ConflictException extends RuntimeException {

    public ConflictException(String resource, String fieldName) {
        super(resource + " with " + fieldName + " already exists");
    }

}
