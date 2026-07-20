package com.petprojects.mymoneyproject.MVC.controller;

import com.petprojects.mymoneyproject.DTO.UserDTO;
import com.petprojects.mymoneyproject.DTO.WalletDTO;
import com.petprojects.mymoneyproject.service.WalletService;
import com.petprojects.mymoneyproject.service.userdetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@Hidden
@RequestMapping("/wallet")
public class WalletMVCController {

    private final WalletService walletService;

    public WalletMVCController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/allWallets")
    public String allUsersWallets(@RequestParam(value = "page", defaultValue = "1") int page,
                                  @RequestParam(value = "size", defaultValue = "15") int pageSize,
                                  Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<WalletDTO> result = walletService.getAll(pageRequest);

        //List<UserDTO> userDTOList = userService.getAll();
        model.addAttribute("wallets", result);
        return "wallet/allWallets";
    }

    @GetMapping("/myWallets")
    public String myWallets(@RequestParam(value = "page", defaultValue = "1") int page,
                            @RequestParam(value = "size", defaultValue = "15") int pageSize,
                            Model model,
                            Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long id = userDetails.getUserId().longValue();

        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<WalletDTO> result = walletService.getWalletsById(pageRequest, id);

        model.addAttribute("wallets", result);
        return "wallet/myWallets";
    }

    @PostMapping("/myWallets/add")
    public String create(@ModelAttribute("walletForm") WalletDTO walletDTO,
                         Authentication authentication,
                         @RequestParam("stringBalance") String stringBalance) {

        walletService.createWallet(walletDTO, authentication, stringBalance);
        return "redirect:/wallet/myWallets";
    }

    @PostMapping("/myWallets/delete")
    public String delete(@RequestParam("deleteId") Long id) {

        walletService.delete(id);
        return "redirect:/wallet/myWallets";
    }

}

