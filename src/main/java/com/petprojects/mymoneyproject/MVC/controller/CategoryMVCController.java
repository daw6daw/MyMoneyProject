package com.petprojects.mymoneyproject.MVC.controller;

import com.petprojects.mymoneyproject.DTO.CategoryDTO;
import com.petprojects.mymoneyproject.DTO.UserDTO;
import com.petprojects.mymoneyproject.DTO.WalletDTO;
import com.petprojects.mymoneyproject.service.CategoryService;
import com.petprojects.mymoneyproject.service.userdetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Hidden
@RequestMapping("category")
public class CategoryMVCController {

    private final CategoryService categoryService;

    public CategoryMVCController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/editCategory/{editId}")
    public String getEditCategory(@PathVariable("editId") Long id,
                                  Model model) {

        CategoryDTO categoryDTO = categoryService.getOne(id);
        model.addAttribute("editCategoryForm", categoryDTO);
        return "category/editCategory";
    }

    @PostMapping("/editCategory/edit")
    public String postEditWallet(@ModelAttribute("editCategoryForm") CategoryDTO categoryDTO,
                                 Authentication authentication) {

        categoryService.editCategory(categoryDTO, authentication);
        return "redirect:/";
    }
}
