package com.petprojects.mymoneyproject.MVC.controller;

import com.petprojects.mymoneyproject.DTO.CategoryDTO;
import com.petprojects.mymoneyproject.DTO.WalletDTO;
import com.petprojects.mymoneyproject.model.Category;
import com.petprojects.mymoneyproject.service.CategoryService;
import com.petprojects.mymoneyproject.service.WalletService;
import com.petprojects.mymoneyproject.service.userdetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Hidden
public class MainMVCController {

    private final CategoryService categoryService;
    private final WalletService walletService;

    public MainMVCController(CategoryService categoryService,
                             WalletService walletService) {
        this.categoryService = categoryService;
        this.walletService = walletService;
    }

//    @GetMapping("/")
//    public String index() {
//        return "index";
//    }

    @PostMapping("/createExpense")
    public String createExpense(@ModelAttribute("expenseForm") CategoryDTO categoryDTO,
                                Authentication authentication) {
        categoryService.createExpense(categoryDTO, authentication);
        return "redirect:/";
    }

    @PostMapping("/createIncome")
    public String createIncome(@ModelAttribute("incomeForm") CategoryDTO categoryDTO,
                               Authentication authentication) {
        categoryService.createIncome(categoryDTO, authentication);
        return "redirect:/";
    }

    @GetMapping("/")
    public String index(Model model,
                        Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        //List<UserDTO> userDTOList = userService.getAll();

        List<CategoryDTO> categoryDTOListExpense = categoryService.getAllExpense(authentication);
        List<CategoryDTO> categoryDTOListIncome = categoryService.getAllIncome(authentication);
        List<WalletDTO> walletDTOList = walletService.getAllWalletsById(userDetails.getUserId().longValue());

        model.addAttribute("wallets", walletDTOList);
        model.addAttribute("categoriesExpense", categoryDTOListExpense);
        model.addAttribute("categoriesIncome", categoryDTOListIncome);
        return "index";
    }

    @PostMapping("/deleteCategory")
    public String delete(@RequestParam("deleteId") Long id,
                         Authentication authentication) {

        categoryService.delete(id, authentication);
        return "redirect:/";
    }


}
