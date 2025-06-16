package com.project.vegetable.repository;

import java.util.Optional;
import com.project.vegetable.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByName(String name);
    void deleteByName(String name);
    boolean existsByName(String name);
}
