package com.petprojects.mymoneyproject.MVC.controller;

import com.petprojects.mymoneyproject.DTO.CategoryDTO;
import com.petprojects.mymoneyproject.DTO.TransactionDTO;
import com.petprojects.mymoneyproject.DTO.UserDTO;
import com.petprojects.mymoneyproject.DTO.WalletDTO;
import com.petprojects.mymoneyproject.service.CategoryService;
import com.petprojects.mymoneyproject.service.userdetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    @GetMapping("/allCategories")
    public String getAllCategories(@RequestParam(value = "page", defaultValue = "1") int page,
                                   @RequestParam(value = "size", defaultValue = "10") int pageSize,
                                   Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<CategoryDTO> result = categoryService.getAllCategories(pageRequest);

        model.addAttribute("categories", result);
        return "category/allCategories";
    }

    @PostMapping("/editCategory/restore")
    public String postRestoreByAdmin(@RequestParam("deleteId") Long id) {

        categoryService.restore(id);
        return "redirect:/category/allCategories";
    }

    @PostMapping("/editCategory/delete")
    public String postDeleteCategoryByAdmin(@RequestParam("deleteId") Long id,
                                 Authentication authentication) {

        categoryService.delete(id, authentication);
        return "redirect:/category/allCategories";
    }

    @GetMapping("/search")
    public String getCategorySearch(Model model) {
        model.addAttribute("categoryFormForSearch", new CategoryDTO());
        return "category/categorySearch";
    }

    @PostMapping("/search")
    public String postCategorySearch(@ModelAttribute("categoryFormForSearch") CategoryDTO categoryDTO,
                                   Model model) {
        model.addAttribute("categories", categoryService.findCategories(categoryDTO));
        return "category/categorySearch";
    }
}
