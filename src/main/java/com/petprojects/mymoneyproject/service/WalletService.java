package com.petprojects.mymoneyproject.service;

import com.petprojects.mymoneyproject.DTO.WalletDTO;
import com.petprojects.mymoneyproject.mapper.WalletMapper;
import com.petprojects.mymoneyproject.model.Wallet;
import com.petprojects.mymoneyproject.repository.WalletRepository;
import org.springframework.stereotype.Service;

@Service
public class WalletService extends GenericService <Wallet, WalletDTO> {

    protected WalletRepository walletRepository;

    public WalletService (WalletRepository walletRepository, WalletMapper walletMapper) {
        super(walletRepository, walletMapper);
        this.walletRepository = walletRepository;
    }
}
