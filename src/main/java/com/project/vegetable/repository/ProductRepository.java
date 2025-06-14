package com.project.vegetable.repository;

import com.project.vegetable.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

interface ProductRepository extends JpaRepository<Product, Long> {
}
