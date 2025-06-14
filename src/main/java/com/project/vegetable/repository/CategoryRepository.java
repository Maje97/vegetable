package com.project.vegetable.repository;

import com.project.vegetable.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

interface CategoryRepository extends JpaRepository<Category, Long> {
}
