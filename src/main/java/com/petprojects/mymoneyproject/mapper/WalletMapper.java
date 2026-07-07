package com.petprojects.mymoneyproject.mapper;

import com.petprojects.mymoneyproject.DTO.WalletDTO;
import com.petprojects.mymoneyproject.model.Wallet;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class WalletMapper extends GenericMapper <Wallet, WalletDTO> {
    private final ModelMapper modelMapper;

    protected WalletMapper (ModelMapper modelMapper) {
        super(modelMapper, Wallet.class, WalletDTO.class);
        this.modelMapper = modelMapper;
    }
}
