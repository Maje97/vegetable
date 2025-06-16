package com.project.vegetable.exception;

import org.springframework.http.HttpStatus;

public class OrderNotFoundException extends DomainException {
    public OrderNotFoundException(Long id) {
      super("Order with id " + id + "not found",
              HttpStatus.NOT_FOUND);
    }
}
