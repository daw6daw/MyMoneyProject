package com.petprojects.mymoneyproject.mapper;

import com.petprojects.mymoneyproject.DTO.RoleDTO;
import com.petprojects.mymoneyproject.model.Role;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class RoleMapper extends GenericMapper<Role, RoleDTO> {

    private final ModelMapper modelMapper;

    protected RoleMapper (ModelMapper modelMapper) {
        super(modelMapper, Role.class, RoleDTO.class);
        this.modelMapper = modelMapper;
    }
}
