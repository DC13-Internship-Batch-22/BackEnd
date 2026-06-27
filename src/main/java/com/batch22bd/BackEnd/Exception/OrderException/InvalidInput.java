package com.batch22bd.BackEnd.Exception.OrderException;

public class InvalidInput extends RuntimeException {
  public InvalidInput(String message) {
    super(message);
  }
}
