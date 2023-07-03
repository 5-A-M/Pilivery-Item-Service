package com.fiveam.itemservice.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.fiveam.itemservice.category.entity.Category;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCategoryName(String categoryName);

}
