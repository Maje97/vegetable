package com.project.vegetable.repository;

import com.project.vegetable.model.Product;
import com.project.vegetable.model.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Product createCarrotProduct() {
        Category category = categoryRepository.findByName("Root vegetables")
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = new Product();
        product.setName("Carrot");
        product.setPrice(BigDecimal.valueOf(2.49f));
        product.setStockQuantity(50);
        product.setCategory(category);
        return productRepository.saveAndFlush(product);
    }

    @Test
    public void testSaveAndFindProduct() {
        Product product = createCarrotProduct();

        Optional<Product> found = productRepository.findById(product.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Carrot");
        assertThat(found.get().getCategory().getName()).isEqualTo("Root vegetables");
    }

    @Test
    public void testDeleteProduct() {
        Product product = createCarrotProduct();
        productRepository.deleteById(product.getId());
        productRepository.flush();
        assertThat(productRepository.findById(product.getId())).isNotPresent();
    }
}
