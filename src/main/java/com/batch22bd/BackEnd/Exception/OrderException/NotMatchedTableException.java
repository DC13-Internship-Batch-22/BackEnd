package com.batch22bd.BackEnd.Exception.OrderException;

public class NotMatchedTableException extends RuntimeException {
    public NotMatchedTableException(String message) {
        super(message);
    }
}
