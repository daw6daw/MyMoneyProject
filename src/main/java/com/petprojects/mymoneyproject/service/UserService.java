package com.petprojects.mymoneyproject.service;

import com.petprojects.mymoneyproject.DTO.UserDTO;
import com.petprojects.mymoneyproject.config.BCryptPasswordConfig;
import com.petprojects.mymoneyproject.mapper.UserMapper;
import com.petprojects.mymoneyproject.model.Role;
import com.petprojects.mymoneyproject.model.User;
import com.petprojects.mymoneyproject.model.Wallet;
import com.petprojects.mymoneyproject.repository.RoleRepository;
import com.petprojects.mymoneyproject.repository.UserRepository;
import com.petprojects.mymoneyproject.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService extends GenericService <User, UserDTO> {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final WalletRepository walletRepository;



    public UserService (UserRepository userRepository,
                        UserMapper userMapper,
                        BCryptPasswordEncoder bCryptPasswordEncoder,
                        WalletRepository walletRepository) {
        super(userRepository, userMapper);
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.walletRepository = walletRepository;
    }

    @Override
    @Transactional
    public UserDTO create(UserDTO object) {
        User user = mapper.toEntity(object);
        Role role = new Role();
        role.setId(2L);
        //user.setCreatedWhen(LocalDateTime.now());
        user.setRole(role);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        User savedUser = repository.save(user);

        //walletService.createFirstWallet(savedUser);

        Wallet wallet = new Wallet(savedUser, "Внешний мир", 99999999999999L);
        walletRepository.save(wallet);

        return mapper.toDTO(savedUser);
    }

    public UserDTO getUserByLogin(final String login) {
        return mapper.toDTO(((UserRepository) repository).findUserByLogin(login));
    }

    public UserDTO getUserByEmail(final String email) {
        return mapper.toDTO(((UserRepository) repository).findUserByEmail(email));
    }

    public Page<UserDTO> getAll (Pageable pageable) {
        Page<User> usersPaginated = repository.findAll(pageable);
        List<UserDTO> result = mapper.toDTOs(usersPaginated.getContent());
        return new PageImpl<>(result, pageable, usersPaginated.getTotalElements());
    }
}
