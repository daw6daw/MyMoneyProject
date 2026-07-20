package com.petprojects.mymoneyproject.service;

import com.petprojects.mymoneyproject.DTO.UserDTO;
import com.petprojects.mymoneyproject.DTO.WalletDTO;
import com.petprojects.mymoneyproject.mapper.UserMapper;
import com.petprojects.mymoneyproject.mapper.WalletMapper;
import com.petprojects.mymoneyproject.model.User;
import com.petprojects.mymoneyproject.model.Wallet;
import com.petprojects.mymoneyproject.repository.UserRepository;
import com.petprojects.mymoneyproject.repository.WalletRepository;
import com.petprojects.mymoneyproject.service.userdetails.CustomUserDetails;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
public class WalletService extends GenericService<Wallet, WalletDTO> {

    protected WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public WalletService(WalletRepository walletRepository,
                         WalletMapper walletMapper,
                         UserRepository userRepository,
                         UserMapper userMapper) {
        super(walletRepository, walletMapper);
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

//    public void createFirstWallet(User user) {
//        Wallet wallet = new Wallet(user, "Внешний мир", 999999999999L);
//        walletRepository.save(wallet);
//    }

    public Page<WalletDTO> getAll(Pageable pageable) {
        Page<Wallet> walletsPaginated = repository.findAll(pageable);
        List<WalletDTO> result = mapper.toDTOs(walletsPaginated.getContent());
        return new PageImpl<>(result, pageable, walletsPaginated.getTotalElements());
    }

    public Page<WalletDTO> getWalletsById(Pageable pageable, Long id) {
        Page<Wallet> walletsPaginated = walletRepository.findAllByUserId(id, pageable);
        List<WalletDTO> result = mapper.toDTOs(walletsPaginated.getContent());
        return new PageImpl<>(result, pageable, walletsPaginated.getTotalElements());
    }

    public void createWallet(WalletDTO walletDTO,
                             Authentication authentication,
                             String stringBalance) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        walletDTO.setUser(userMapper.toDTO(userRepository.findById(userDetails.getUserId().longValue()).orElseThrow(() -> new jakarta.persistence.EntityNotFoundException("User not found"))));

        // Конвертируем строку ("100,50") в копейки (Long) с помощью BigDecimal
        String normalized = stringBalance.replace(",", ".");
        java.math.BigDecimal decimalAmount = new java.math.BigDecimal(normalized);
        java.math.BigDecimal centsAmount = decimalAmount.multiply(new java.math.BigDecimal("100"));
        Long longBalance = centsAmount.setScale(0, java.math.RoundingMode.HALF_UP).longValue();

        // Записываем готовый Long обратно в DTO
        walletDTO.setBalance(longBalance);
        repository.save(mapper.toEntity(walletDTO));

    }
}
