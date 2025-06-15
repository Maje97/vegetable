package com.project.vegetable.exception;

public class CustomerNotFoundException extends DomainException {
    public CustomerNotFoundException(Long id) {
        super("Customer with id " + id + "not found");
    }
}