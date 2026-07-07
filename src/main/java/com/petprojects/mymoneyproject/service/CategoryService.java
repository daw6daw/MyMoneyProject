package com.petprojects.mymoneyproject.service;

import com.petprojects.mymoneyproject.DTO.CategoryDTO;
import com.petprojects.mymoneyproject.mapper.CategoryMapper;
import com.petprojects.mymoneyproject.model.Category;
import com.petprojects.mymoneyproject.repository.CategoryRepository;
import org.springframework.stereotype.Service;

@Service
public class CategoryService extends GenericService<Category, CategoryDTO> {

    protected CategoryRepository categoryRepository;

    public CategoryService (CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        super(categoryRepository, categoryMapper);
        this.categoryRepository = categoryRepository;
    }
}
