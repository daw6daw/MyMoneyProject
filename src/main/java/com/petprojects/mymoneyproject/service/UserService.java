package com.petprojects.mymoneyproject.service;

import com.petprojects.mymoneyproject.DTO.UserDTO;
import com.petprojects.mymoneyproject.mapper.UserMapper;
import com.petprojects.mymoneyproject.model.User;
import com.petprojects.mymoneyproject.repository.RoleRepository;
import com.petprojects.mymoneyproject.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService extends GenericService <User, UserDTO> {

    protected UserRepository userRepository;

    protected RoleRepository roleRepository;

    protected UserMapper userMapper;

    public UserService (UserRepository userRepository, UserMapper userMapper, RoleRepository roleRepository) {
        super(userRepository, userMapper);
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDTO create(UserDTO dto) {
        User user = userMapper.toEntity(dto);

        //user.setCreatedWhen(LocalDateTime.now());

        user.setRole(roleRepository.getById(2L));

        return userMapper.toDTO(userRepository.save(user));
    }
}
