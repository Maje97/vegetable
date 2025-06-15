package com.project.vegetable.exception;

public class CategoryNotFoundException extends DomainException {
    public CategoryNotFoundException(String name) {
        super("Category " + name + "not found");
    }
}
