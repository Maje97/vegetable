package com.project.vegetable.exception;

import org.springframework.http.HttpStatus;

public class ProductNotFoundException extends DomainException {
    public ProductNotFoundException(Long id) {
        super("Product with id " + id + "not found",
                HttpStatus.NOT_FOUND);
    }
}
