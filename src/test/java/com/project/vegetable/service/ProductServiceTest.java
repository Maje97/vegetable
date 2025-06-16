package com.project.vegetable.service;

import com.project.vegetable.exception.CategoryNotFoundException;
import com.project.vegetable.exception.ProductNotFoundException;
import com.project.vegetable.model.Category;
import com.project.vegetable.model.Product;
import com.project.vegetable.repository.CategoryRepository;
import com.project.vegetable.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        categoryRepository = mock(CategoryRepository.class);
        productRepository = mock(ProductRepository.class);
        productService = new ProductService(productRepository, categoryRepository);
    }

    @Test
    void testCreateCategory() {
        ArgumentCaptor<Category> captor = ArgumentCaptor.forClass(Category.class);
        Category mockedResult = new Category();
        mockedResult.setName("Kitty veggies");

        when(categoryRepository.save(any(Category.class))).thenReturn(mockedResult);

        Category saved = productService.createCategory("Kitty veggies");

        assertEquals("Kitty veggies", saved.getName());
        verify(categoryRepository, times(1)).save(captor.capture());
        assertEquals("Kitty veggies", captor.getValue().getName());
    }

    @Test
    void testGetAllProductsInCategory() {
        Category mockedResult = new Category();
        mockedResult.setName("Kitty veggies");
        List<Product> products = List.of(new Product(), new Product());
        mockedResult.setProducts(products);

        when(categoryRepository.findByName("Kitty veggies")).thenReturn(Optional.of(mockedResult));

        List<Product> result = productService.getAllProductsInCategory("Kitty veggies");

        assertEquals(2, result.size());
    }

    @Test
    void testGetAllProductsInCategory_CategoryNotFound() {
        when(categoryRepository.findByName("Nonexistent")).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> productService.getAllProductsInCategory("Nonexistent"));
    }

    @Test
    void testCreateProduct() {
        Product product = new Product();
        product.setName("Paw beans");

        when(productRepository.save(product)).thenReturn(product);

        Product saved = productService.createProduct(product);

        assertEquals("Paw beans", saved.getName());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testUpdateProduct_Success() {
        Long productId = 1L;

        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setName("Old Name");
        existingProduct.setPrice(BigDecimal.valueOf(10.0));
        existingProduct.setDescription("Old description");
        existingProduct.setStockQuantity(5);

        Product updatedProduct = new Product();
        updatedProduct.setName("New Name");
        updatedProduct.setPrice(BigDecimal.valueOf(15.0));
        updatedProduct.setDescription("New description");
        updatedProduct.setStockQuantity(10);

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(i -> i.getArgument(0));

        Product result = productService.updateProduct(productId, updatedProduct);

        assertEquals("New Name", result.getName());
        assertEquals(BigDecimal.valueOf(15.0), result.getPrice());
        assertEquals("New description", result.getDescription());
        assertEquals(10, result.getStockQuantity());

        verify(productRepository).findById(productId);
        verify(productRepository).save(existingProduct);
    }

    @Test
    void testUpdateProduct_ProductNotFound() {
        Long productId = 1L;
        Product updatedProduct = new Product();

        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> {
            productService.updateProduct(productId, updatedProduct);
        });

        verify(productRepository).findById(productId);
        verify(productRepository, never()).save(any());
    }

    @Test
    void testDeleteProduct() {
        Long productId = 1L;
        productService.deleteProduct(productId);
        verify(productRepository, times(1)).deleteById(productId);
    }

    @Test
    void testGetProductById_Success() {
        Product product = new Product();
        product.setId(1L);
        product.setName("Paw beans");

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(1L);

        assertEquals("Paw beans", result.getName());
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(99L));
    }
}
