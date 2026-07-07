package com.petprojects.mymoneyproject.controller;

import com.petprojects.mymoneyproject.DTO.CategoryDTO;
import com.petprojects.mymoneyproject.model.Category;
import com.petprojects.mymoneyproject.repository.CategoryRepository;
import com.petprojects.mymoneyproject.service.CategoryService;
import com.petprojects.mymoneyproject.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
@Tag(name = "Категории", description = "Контроллер для работы с категориями пользователей")
public class CategoryController extends GenericController<Category, CategoryDTO> {

    public CategoryController (CategoryService categoryService) {
        super(categoryService);
    }
}
