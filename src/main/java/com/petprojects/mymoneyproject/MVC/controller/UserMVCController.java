package com.petprojects.mymoneyproject.MVC.controller;

import com.petprojects.mymoneyproject.DTO.UserDTO;
import com.petprojects.mymoneyproject.service.UserService;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Hidden
@RequestMapping("user")
public class UserMVCController {

    private final UserService userService;

    public UserMVCController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/allUsers")
    public String allUsers(@RequestParam(value = "page", defaultValue = "1") int page,
                           @RequestParam(value = "size", defaultValue = "15") int pageSize,
                           Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<UserDTO> result = userService.getAll(pageRequest);

        //List<UserDTO> userDTOList = userService.getAll();
        model.addAttribute("users", result);
        return "user/allUsers";
    }

//    @GetMapping("/logout")
//    public String logout() {
//        return "redirect:/";
//    }

    //переадресует на страницу регистрации
    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new UserDTO());
        return "user/registration";
    }

    //примет данные, создаст запись в бд и перенаправит на главную страницу
    @PostMapping("/registration")
    public String registration(@ModelAttribute("userForm") UserDTO userDTO,
                               BindingResult bindingResult) {
        if (userDTO.getLogin().equalsIgnoreCase("admin") || userService.getUserByLogin(userDTO.getLogin()) != null) {
            bindingResult.rejectValue("login", "error.login", "Этот логин уже занят");
            return "user/registration";
        }

        if (userService.getUserByEmail(userDTO.getEmail()) != null) {
            bindingResult.rejectValue("email", "error.email", "Эта электронная почта уже занята");
            return "user/registration";
        }

        userService.create(userDTO);
        return "redirect:/";
    }

    //Login here
    @GetMapping("/login")
    public String login() {
        if (
                SecurityContextHolder.getContext().getAuthentication() != null &&
                        SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                        !(SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken)
        ) {
            return "redirect:user/registration";
        }
        return "user/login";
    }
}
