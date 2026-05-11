package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.request.NewCategoryDto;
import ru.practicum.ewm.dto.response.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(NewCategoryDto newCategoryDto);

    CategoryDto updateCategory(Long catId, CategoryDto categoryDto);

    void deleteCategory(Long catId);

    List<CategoryDto> getCategories(int from, int size);

    CategoryDto getCategory(Long catId);
}