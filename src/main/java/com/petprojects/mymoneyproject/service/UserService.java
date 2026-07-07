package com.petprojects.mymoneyproject.service;

import com.petprojects.mymoneyproject.DTO.UserDTO;
import com.petprojects.mymoneyproject.mapper.UserMapper;
import com.petprojects.mymoneyproject.model.User;
import com.petprojects.mymoneyproject.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService extends GenericService <User, UserDTO> {

    protected UserRepository userRepository;

    public UserService (UserRepository userRepository, UserMapper userMapper) {
        super(userRepository, userMapper);
        this.userRepository = userRepository;
    }


}
