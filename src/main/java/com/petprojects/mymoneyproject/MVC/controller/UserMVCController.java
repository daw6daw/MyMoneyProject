package com.petprojects.mymoneyproject.MVC.controller;

import com.petprojects.mymoneyproject.DTO.UserDTO;
import com.petprojects.mymoneyproject.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("user")
public class UserMVCController {

    private final UserService userService;

    public UserMVCController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/allUsers")
    public String allUsers(Model model) {
        List<UserDTO> userDTOList = userService.getAll();
        model.addAttribute("users", userDTOList);
        return "user/allUsers";
    }
}
