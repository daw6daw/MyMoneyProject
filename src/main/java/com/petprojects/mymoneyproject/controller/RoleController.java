package com.petprojects.mymoneyproject.controller;

import com.petprojects.mymoneyproject.DTO.RoleDTO;
import com.petprojects.mymoneyproject.model.Role;
import com.petprojects.mymoneyproject.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
@Tag(name = "Роли", description = "Контроллер для работы с ролями пользователей сервиса")
public class RoleController extends GenericController<Role, RoleDTO> {

    public RoleController(RoleService roleService) {
        super(roleService);
    }
}
