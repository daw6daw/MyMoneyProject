package com.petprojects.mymoneyproject.mapper;

import com.petprojects.mymoneyproject.DTO.CategoryDTO;
import com.petprojects.mymoneyproject.model.Category;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper extends GenericMapper<Category, CategoryDTO> {

    private final ModelMapper modelMapper;

    protected CategoryMapper(ModelMapper modelMapper) {
        super(modelMapper, Category.class, CategoryDTO.class);
        this.modelMapper = modelMapper;
    }
}
