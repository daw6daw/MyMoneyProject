package com.petprojects.mymoneyproject.controller;

import com.petprojects.mymoneyproject.model.Category;
import com.petprojects.mymoneyproject.repository.CategoryRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/category")
@Tag(name = "Категории", description = "Контроллер для работы с категориями пользователей")
public class CategoryController extends GenericController<Category> {

    private final CategoryRepository categoryRepository;

    public CategoryController (CategoryRepository categoryRepository) {

        super(categoryRepository);
        this.categoryRepository = categoryRepository;
    }
}
