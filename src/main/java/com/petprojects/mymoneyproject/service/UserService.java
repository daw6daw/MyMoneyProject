package com.petprojects.mymoneyproject.service;

import com.petprojects.mymoneyproject.DTO.UserDTO;
import com.petprojects.mymoneyproject.config.BCryptPasswordConfig;
import com.petprojects.mymoneyproject.mapper.UserMapper;
import com.petprojects.mymoneyproject.model.Role;
import com.petprojects.mymoneyproject.model.User;
import com.petprojects.mymoneyproject.repository.RoleRepository;
import com.petprojects.mymoneyproject.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService extends GenericService <User, UserDTO> {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService (UserRepository userRepository, UserMapper userMapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        super(userRepository, userMapper);
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UserDTO create(UserDTO object) {
        User user = mapper.toEntity(object);
        Role role = new Role();
        role.setId(2L);
        //user.setCreatedWhen(LocalDateTime.now());

        user.setRole(role);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return mapper.toDTO(repository.save(user));
    }

    public UserDTO getUserByLogin(final String login) {
        return mapper.toDTO(((UserRepository) repository).findUserByLogin(login));
    }

    public UserDTO getUserByEmail(final String email) {
        return mapper.toDTO(((UserRepository) repository).findUserByEmail(email));
    }
}
