package com.project.vegetable.exception;

import org.springframework.http.HttpStatus;

public class CategoryNotFoundException extends DomainException {
    public CategoryNotFoundException(String name) {
        super("Category " + name + "not found", HttpStatus.NOT_FOUND);
    }
}
