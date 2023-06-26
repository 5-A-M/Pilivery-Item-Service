package com.fiveam.searchservice.category.mapper;

import org.mapstruct.Mapper;
import com.fiveam.searchservice.category.dto.CategoryDto;
import com.fiveam.searchservice.category.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    default Category categoryPostDtoToCategory(CategoryDto.Post post) {
        Category category = new Category();
        category.setCategoryName(post.getCategoryName());
        return category;
    }


    default CategoryDto.Response categoryToCategoryResponseDto(Category category) {
        CategoryDto.Response categoryResponseDto = new CategoryDto.Response();
        categoryResponseDto.setCategoryName(category.getCategoryName());
        return categoryResponseDto;
    }

}
