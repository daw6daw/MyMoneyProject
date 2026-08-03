package com.petprojects.mymoneyproject.service;

import com.petprojects.mymoneyproject.DTO.UserDTO;
import com.petprojects.mymoneyproject.DTO.WalletDTO;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService extends GenericService<User, UserDTO> {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final WalletRepository walletRepository;

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository,
                       UserMapper userMapper,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       WalletRepository walletRepository) {
        super(userRepository, userMapper);
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
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
        user.setCreatedBy("System");
        User savedUser = repository.save(user);

        //walletService.createFirstWallet(savedUser);

        Wallet wallet = new Wallet(savedUser, "Внешний мир", 99999999999999L);
        wallet.setCreatedBy("System");
        wallet.setDeleted(false);
        walletRepository.save(wallet);

        return mapper.toDTO(savedUser);
    }

    public UserDTO getUserByLogin(final String login) {
        return mapper.toDTO(((UserRepository) repository).findUserByLogin(login));
    }

    public UserDTO getUserByEmail(final String email) {
        return mapper.toDTO(((UserRepository) repository).findUserByEmail(email));
    }

    public Page<UserDTO> getAll(Pageable pageable) {
        Page<User> usersPaginated = repository.findAll(pageable);
        List<UserDTO> result = mapper.toDTOs(usersPaginated.getContent());
        return new PageImpl<>(result, pageable, usersPaginated.getTotalElements());
    }

    public void editUser(UserDTO userDTO,
                         Authentication authentication) {
        User oldUser = repository.findById(userDTO.getId()).get();
        String currentUsername = authentication.getName();
        oldUser.setLastName(userDTO.getLastName());
        oldUser.setFirstName(userDTO.getFirstName());
        oldUser.setMiddleName(userDTO.getMiddleName());
        oldUser.setLogin(userDTO.getLogin());
        oldUser.setEmail(userDTO.getEmail());
        oldUser.setNumber(userDTO.getNumber());
        oldUser.setUpdatedBy(currentUsername);
        oldUser.setUpdatedWhen(LocalDateTime.now());

        repository.save(oldUser);
    }

    public void delete(Long id,
                       Authentication authentication) {
        String currentUsername = authentication.getName();

        User user = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("User не найден с ID: " + id));
        user.setDeleted(true);
        user.setUpdatedBy(currentUsername);
        user.setDeletedBy(currentUsername);
        user.setDeletedWhen(LocalDateTime.now());
        repository.save(user);
    }

    public void restore(Long id) {

        User user = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("User не найден с ID: " + id));

        user.setDeleted(false);
        user.setRestoredWhen(LocalDateTime.now());

        repository.save(user);
    }

    public boolean checkPassword(String oldPassword,
                                 Authentication authentication) {
        String currentUsername = authentication.getName();
        User user = mapper.toEntity(getUserByLogin(currentUsername));
        return bCryptPasswordEncoder.matches(oldPassword, user.getPassword());
    }

    public void setNewPassword(String newPassword,
                               Authentication authentication) {
        String currentUsername = authentication.getName();
        User user = mapper.toEntity(getUserByLogin(currentUsername));
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        user.setUpdatedBy(currentUsername);
        repository.save(user);
    }

    public void setNewPasswordByAdmin(String newPassword,
                                      Long userId,
                                      Authentication authentication) {
        String currentUsername = authentication.getName();
        User user = repository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User не найден с ID: " + userId));
        user.setPassword(bCryptPasswordEncoder.encode(newPassword));
        user.setUpdatedBy(currentUsername);
        repository.save(user);
    }

    public List<UserDTO> findUsers(UserDTO userDTO) {
        return mapper.toDTOs(userRepository.searchUsers(
                userDTO.getId(),
                userDTO.getEmail(),
                userDTO.getLogin(),
                userDTO.getLastName(),
                userDTO.getFirstName(),
                userDTO.getMiddleName(),
                userDTO.getRole().getId(),
                userDTO.isDeleted(),
                userDTO.getNumber(),
                userDTO.getCreatedWhen(),
                userDTO.getUpdatedBy(),
                userDTO.getUpdatedWhen(),
                userDTO.getDeletedBy(),
                userDTO.getDeletedWhen(),
                userDTO.getRestoredWhen()
        ));
    }

}
