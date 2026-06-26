package com.batch22bd.BackEnd.Exception.OrderException;

public class DeleteNoItemException extends RuntimeException {
    public DeleteNoItemException(String message) {
        super(message);
    }
}
