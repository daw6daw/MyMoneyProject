package com.petprojects.mymoneyproject.MVC.controller;

import com.petprojects.mymoneyproject.DTO.UserDTO;
import com.petprojects.mymoneyproject.DTO.WalletDTO;
import com.petprojects.mymoneyproject.service.UserService;
import com.petprojects.mymoneyproject.service.userdetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
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
        return "redirect:/user/login";
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

    @GetMapping("/myProfile")
    public String getMyProfile(Authentication authentication,
                               Model model) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        UserDTO userDTO = userService.getOne(userDetails.getUserId().longValue());
        model.addAttribute("userForm", userDTO);
        return "user/myProfile";
    }

    @PostMapping("/myProfile/edit")
    public String postEditProfile(@ModelAttribute("userForm") UserDTO userDTO,
                                  BindingResult bindingResult,
                                  Authentication authentication) {

        String currentUsername = authentication.getName();

        // 1. Сначала проверяем запрещенные системные слова (их нельзя никому)
        if (userDTO.getLogin().equalsIgnoreCase("admin") || userDTO.getLogin().equalsIgnoreCase("login")) {
            bindingResult.rejectValue("login", "error.login", "Этот логин нельзя использовать");
            return "user/myProfile";
        }

        // 2. Теперь проверяем занятость логина другими пользователями

        if (userService.getUserByLogin(userDTO.getLogin()) != null &&
                !userService.getUserByLogin(userDTO.getLogin()).getId().equals(userService.getUserByLogin(currentUsername).getId())) {
            bindingResult.rejectValue("login", "error.login", "Этот логин уже занят");
            return "user/myProfile";
        }

        if (userService.getUserByEmail(userDTO.getEmail()) != null &&
                !userService.getUserByEmail(userDTO.getEmail()).getId().equals(userService.getUserByLogin(currentUsername).getId())) {
            bindingResult.rejectValue("email", "error.email", "Эта электронная почта уже занята");
            return "user/myProfile";
        }

        userService.editUser(userDTO, authentication);
        return "redirect:/user/myProfile";

        //todo сделать под админа управление аккаунтами, возможно добавить удаление акка и пользователю
    }
}
