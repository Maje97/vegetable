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
        ArgumentCaptor<Category> captor = ArgumentCaptor.forClass(Category.class);
        Category mockedResult = new Category();
        mockedResult.setName("Kitty veggies");
        List<Product> products = List.of(new Product(), new Product());
        mockedResult.setProducts(products);

        when(categoryRepository.findByName("Kitty veggies")).thenReturn(Optional.of(mockedResult));

        List<Product> result = productService.getAllProductsInCategory("Kitty veggies");

        assertEquals(2, result.size());
    }

    @Test
    void testCreateProduct() {

    }
}
