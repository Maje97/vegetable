package com.project.vegetable.controller;

import com.project.vegetable.model.Product;
import com.project.vegetable.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product", description = "Product management APIs")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    //GET
    @GetMapping
    @Operation(summary = "Get all products")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a product by its ID")
    public Product getOneProduct(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping("/{name}")
    @Operation(summary = "Get products by category name")
    public List<Product> getProductsByCategory(@PathVariable String name) {
        return productService.getAllProductsInCategory(name);
    }

    //POST
    @PostMapping
    @Operation(summary = "Create new product")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product savedProduct = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct);
    }

    //PUT
    @PutMapping("/{id}")
    @Operation(summary = "Update product by id and new product in request body")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody Product product) {
        Product updated = productService.updateProduct(id, product);
        return ResponseEntity.ok(updated);
    }

    //DELETE
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete products by id")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
