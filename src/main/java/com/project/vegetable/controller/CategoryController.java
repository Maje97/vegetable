package com.project.vegetable.controller;

import com.project.vegetable.model.Category;
import com.project.vegetable.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category", description = "Category management APIs")
public class CategoryController {

    private final ProductService productService;

    @Autowired
    public CategoryController(ProductService productService) {
        this.productService = productService;
    }

    //POST
    @PostMapping
    @Operation(summary = "Create new category")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category savedCategory = productService.createCategory(category.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCategory);
    }

    //DELETE
    @DeleteMapping("/{name}")
    @Operation(summary = "Delete category by name")
    public ResponseEntity<Void> deleteCategory(@PathVariable String name) {
        productService.deleteCategory(name);
        return ResponseEntity.noContent().build();
    }
}
