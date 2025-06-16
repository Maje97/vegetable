package com.project.vegetable.exception;

import org.springframework.http.HttpStatus;

public class OrderItemNotFoundException extends DomainException {
    public OrderItemNotFoundException(Long id) {
      super("Order item with id " + id + "not found",
              HttpStatus.NOT_FOUND);
    }
}
