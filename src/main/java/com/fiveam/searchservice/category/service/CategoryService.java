package com.fiveam.searchservice.category.service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.fiveam.searchservice.category.entity.Category;
import com.fiveam.searchservice.category.repository.CategoryRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category createCategory(Category category) {
        return categoryRepository.save(category);
    }


    public void verifyExistCategory(String categoryName) {
        Optional<Category> byCategoryName =
                categoryRepository.findByCategoryName(categoryName);

        if (byCategoryName.isEmpty()) { // 카테고리 이름이 존재하지 않는다면
            throw new RuntimeException();
        }
    }

    public Long findCategoryId(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName).get().getCategoryId();
    }



}
