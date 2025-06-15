package com.project.vegetable.repository;

import com.project.vegetable.model.Category;
import com.project.vegetable.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    private Category createVoidCategory() {
        Category category = new Category();
        category.setName("Void vegetable");
        return categoryRepository.saveAndFlush(category);
    }

    @Test
    public void testSaveAndFindCategory() {
        Category category = createVoidCategory();
        Optional<Category> found = categoryRepository.findById(category.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Void vegetable");
    }

    @Test
    public void testDeleteCategory() {
        Category category = createVoidCategory();
        categoryRepository.deleteById(category.getId());
        categoryRepository.flush();
        assertThat(categoryRepository.findById(category.getId())).isNotPresent();
    }

    @Test
    public void testOrphanRemoval() {
        Category category = createVoidCategory();

        Product product = new Product();
        product.setName("Void plant");
        product.setPrice(1.2f);
        product.setStockQuantity(40);
        product.setCategory(category);
        productRepository.save(product);

        category.addProduct(product);
        categoryRepository.saveAndFlush(category);

        category.removeProduct(product);
        categoryRepository.saveAndFlush(category);  // triggers orphanRemoval

        assertThat(categoryRepository.findById(category.getId())).isPresent();
        assertThat(productRepository.findById(product.getId())).isNotPresent();
    }

    @Test
    public void testCascadeRemoval() {
        Category category = createVoidCategory();

        Product product1 = new Product();
        product1.setName("Void plant");
        product1.setPrice(1.2f);
        product1.setStockQuantity(40);
        product1.setCategory(category);

        Product product2 = new Product();
        product2.setName("Void leaves");
        product2.setPrice(1.0f);
        product2.setStockQuantity(30);
        product2.setCategory(category);

        productRepository.save(product1);
        productRepository.save(product2);

        category.addProduct(product1);
        category.addProduct(product2);
        categoryRepository.saveAndFlush(category);

        categoryRepository.delete(category);
        categoryRepository.flush();

        assertThat(categoryRepository.findById(category.getId())).isNotPresent();
        assertThat(productRepository.findById(product1.getId())).isNotPresent();
        assertThat(productRepository.findById(product2.getId())).isNotPresent();
    }
}
