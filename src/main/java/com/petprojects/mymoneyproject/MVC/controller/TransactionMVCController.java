package com.petprojects.mymoneyproject.MVC.controller;

import com.petprojects.mymoneyproject.DTO.TransactionDTO;
import com.petprojects.mymoneyproject.DTO.WalletDTO;
import com.petprojects.mymoneyproject.service.TransactionService;
import com.petprojects.mymoneyproject.service.WalletService;
import com.petprojects.mymoneyproject.service.userdetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@Hidden
@RequestMapping("transaction")
public class TransactionMVCController {
    private final TransactionService transactionService;
    private final WalletService walletService;

    public TransactionMVCController(TransactionService transactionService,
                                    WalletService walletService) {
        this.transactionService = transactionService;
        this.walletService = walletService;
    }

    @GetMapping("/history")
    public String getMyTransactions(@RequestParam(value = "page", defaultValue = "1") int page,
                                    @RequestParam(value = "size", defaultValue = "15") int pageSize,
                                    Model model,
                                    Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long id = userDetails.getUserId().longValue();

        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<TransactionDTO> result = transactionService.getPageMyTransactionsById(pageRequest, id);

        model.addAttribute("transactions", result);
        return "transaction/history";
    }


    @GetMapping("/transfer")
    public String getTransfer(Model model,
                              Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        List<WalletDTO> walletDTOList = walletService.getAllWalletsById(userDetails.getUserId().longValue());


        model.addAttribute("wallets", walletDTOList);
        return "transaction/transfer";
    }
    //todo: тут что-то не так, надо сделать рабочим, доделать варианты update и delete для транзакций, по возможности, сделать для админа управление категориями и транзакциями

    @PostMapping("/makeTransfer")
    public String postMakeTransferTransaction(@RequestParam("fromWallet") Long fromWalletId,
                                              @RequestParam("amount") String amount,
                                              @RequestParam("toWallet") Long toWalletId,
                                              RedirectAttributes redirectAttributes,
                                              Authentication authentication) {

        if (fromWalletId.equals(toWalletId)) {
            // Записываем ошибку во Flash-атрибуты. Название должно быть точь-в-точь как в HTML ("errorMessage")
            redirectAttributes.addFlashAttribute("errorMessage", "Нельзя сделать перевод на один и тот же кошелек!");
            return "redirect:/transaction/transfer"; // Убедитесь, что этот путь точно ведет на ваш GET-метод ниже
        }

        transactionService.makeTransferTransaction(fromWalletId, amount, toWalletId, authentication);
        return "redirect:/wallet/myWallets";
    }

    @GetMapping("/allTransactions")
    public String getAllTransactions(@RequestParam(value = "page", defaultValue = "1") int page,
                                     @RequestParam(value = "size", defaultValue = "10") int pageSize,
                                     Model model) {

        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<TransactionDTO> result = transactionService.getAllTransactions(pageRequest);

        model.addAttribute("transactions", result);
        return "transaction/allTransactions";
    }

}
