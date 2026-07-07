package com.petprojects.mymoneyproject.controller;

import com.petprojects.mymoneyproject.DTO.UserDTO;
import com.petprojects.mymoneyproject.model.User;
import com.petprojects.mymoneyproject.repository.UserRepository;
import com.petprojects.mymoneyproject.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@Tag(name = "Пользователи", description = "Контроллер для работы с пользователями сервиса")
public class UserController extends GenericController<User, UserDTO> {

    public UserController(UserService userService) {
        super(userService);
    }
}
