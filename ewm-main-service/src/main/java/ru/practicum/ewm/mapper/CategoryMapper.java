package ru.practicum.ewm.mapper;

import ru.practicum.ewm.dto.response.CategoryDto;
import ru.practicum.ewm.dto.request.NewCategoryDto;
import ru.practicum.ewm.model.Category;

public class CategoryMapper {

    public static CategoryDto toDto(Category category) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setId(category.getId());
        categoryDto.setName(category.getName());
        return categoryDto;
    }

    public static Category toCategory(NewCategoryDto request) {
        Category category = new Category();
        category.setName(request.getName());
        return category;
    }