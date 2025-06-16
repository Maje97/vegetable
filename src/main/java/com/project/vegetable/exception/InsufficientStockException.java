package com.project.vegetable.exception;

import org.springframework.http.HttpStatus;

public class InsufficientStockException extends DomainException {
    public InsufficientStockException(Long productId, int requested, int available) {
        super("Product with ID " + productId + " has insufficient stock. Requested: " +
                requested + ", Available: " + available, HttpStatus.CONFLICT);
    }
}
