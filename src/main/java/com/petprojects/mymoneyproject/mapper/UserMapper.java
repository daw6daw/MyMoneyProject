package com.petprojects.mymoneyproject.mapper;

import com.petprojects.mymoneyproject.DTO.UserDTO;
import com.petprojects.mymoneyproject.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapper extends GenericMapper<User, UserDTO> {

    private final ModelMapper modelMapper;

    protected UserMapper (ModelMapper modelMapper) {
        super(modelMapper, User.class, UserDTO.class);
        this.modelMapper = modelMapper;
    }
}
