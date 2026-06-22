package com.petprojects.mymoneyproject.controller;

import com.petprojects.mymoneyproject.model.Wallet;
import com.petprojects.mymoneyproject.repository.WalletRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/wallet")
@Tag(name = "Кошельки", description = "Контроллер для работы с кошельками пользователей")
public class WalletController extends GenericController<Wallet> {

    private final WalletRepository walletRepository;

    public WalletController(WalletRepository walletRepository) {
        super(walletRepository);
        this.walletRepository = walletRepository;
    }
}
