package com.petprojects.mymoneyproject.service;

import com.petprojects.mymoneyproject.DTO.RoleDTO;
import com.petprojects.mymoneyproject.mapper.RoleMapper;
import com.petprojects.mymoneyproject.model.Role;
import com.petprojects.mymoneyproject.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends GenericService<Role, RoleDTO> {

    protected RoleRepository roleRepository;

    public RoleService (RoleRepository roleRepository, RoleMapper roleMapper) {
        super(roleRepository, roleMapper);
        this.roleRepository = roleRepository;
    }
}
