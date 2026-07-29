package com.petprojects.mymoneyproject.MVC.controller;

import com.petprojects.mymoneyproject.DTO.CategoryDTO;
import com.petprojects.mymoneyproject.DTO.WalletDTO;
import com.petprojects.mymoneyproject.service.CategoryService;
import com.petprojects.mymoneyproject.service.TransactionService;
import com.petprojects.mymoneyproject.service.WalletService;
import com.petprojects.mymoneyproject.service.userdetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Hidden;
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
     private final TransactionService transactionService;

    public MainMVCController(CategoryService categoryService,
                             WalletService walletService,
                             TransactionService transactionService) {
        this.categoryService = categoryService;
        this.walletService = walletService;
        this.transactionService = transactionService;
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
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return "redirect:/user/allUsers"; // Админ возвращается к полному списку
        }

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
    public String deleteCategory(@RequestParam("deleteId") Long id,
                                 Authentication authentication) {

        categoryService.delete(id, authentication);
        return "redirect:/";
    }

    @PostMapping("/makeExpenseTransaction")
    public String postMakeExpenseTransaction(@RequestParam("fromWalletId") Long selectedWalletId,
                                             @RequestParam("amount") String amount,
                                             @RequestParam("description") String description,
                                             @RequestParam("categoryId") Long categoryID,
                                         Authentication authentication) {

        transactionService.makeExpenseTransaction(selectedWalletId, amount, description, categoryID, authentication);
        return "redirect:/";
    }

    @PostMapping("/makeIncomeTransaction")
    public String postMakeIncomeTransaction(@RequestParam("toWalletId") Long selectedWalletId,
                                             @RequestParam("amount") String amount,
                                             @RequestParam("description") String description,
                                             @RequestParam("categoryId") Long categoryID,
                                             Authentication authentication) {

        transactionService.makeIncomeTransaction(selectedWalletId, amount, description, categoryID, authentication);
        return "redirect:/";
    }
}
