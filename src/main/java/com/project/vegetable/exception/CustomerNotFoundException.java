package com.project.vegetable.exception;

import org.springframework.http.HttpStatus;

public class CustomerNotFoundException extends DomainException {
    public CustomerNotFoundException(Long id) {
        super("Customer with id " + id + "not found", HttpStatus.NOT_FOUND);
    }
}