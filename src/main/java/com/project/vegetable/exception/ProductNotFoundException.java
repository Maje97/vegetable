package com.project.vegetable.exception;

public class ProductNotFoundException extends DomainException {
    public ProductNotFoundException(Long id) {
        super("Product with id " + id + "not found");
    }
}
