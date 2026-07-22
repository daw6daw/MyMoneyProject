package com.petprojects.mymoneyproject.MVC.controller;

import com.petprojects.mymoneyproject.DTO.UserDTO;
import com.petprojects.mymoneyproject.DTO.WalletDTO;
import com.petprojects.mymoneyproject.model.Wallet;
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
                            @RequestParam(value = "size", defaultValue = "10") int pageSize,
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
    public String delete(@RequestParam("deleteId") Long id,
                         Authentication authentication) {

        walletService.delete(id, authentication);
        return "redirect:/wallet/myWallets";
    }

    @PostMapping("/editWallet/delete")
    public String deleteByAdmin(@RequestParam("deleteId") Long id,
                                Authentication authentication) {

        walletService.delete(id, authentication);
        return "redirect:/wallet/allWallets";
    }

    @PostMapping("/editWallet/restore")
    public String restoreByAdmin(@RequestParam("deleteId") Long id,
                                Authentication authentication) {

        walletService.restore(id, authentication);
        return "redirect:/wallet/allWallets";
    }



    //TODO: сделать гет и пост маппинг для mywallets/editwallet, доделать editwallet.html, мб переименовать? хотя для пет сойдёт и так

    @GetMapping("/editWallet/{editId}")
    public String getEditWallet(@PathVariable("editId") Long id,
                                Model model) {

        WalletDTO walletDTO = walletService.getOne(id);
        model.addAttribute("editWalletForm", walletDTO);
        return "wallet/editWallet";
    }

    @PostMapping("/editWallet/edit")
    public String postEditWallet(@ModelAttribute("editWalletForm") WalletDTO walletDTO,
                                 @RequestParam("stringBalance") String stringBalance,
                                 Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        walletService.editWallet(walletDTO, stringBalance, authentication);

        // 4. Динамически выбираем, куда перенаправить пользователя
        if (isAdmin) {
            return "redirect:/wallet/allWallets"; // Админ возвращается к полному списку
        } else {
            return "redirect:/wallet/myWallets";  // Обычный юзер возвращается к своим кошелькам
        }
    }

}

