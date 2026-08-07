package com.petprojects.mymoneyproject.MVC.controller;

import com.petprojects.mymoneyproject.DTO.UserDTO;
import com.petprojects.mymoneyproject.DTO.WalletDTO;
import com.petprojects.mymoneyproject.service.UserService;
import com.petprojects.mymoneyproject.service.userdetails.CustomUserDetails;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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

    }

    @GetMapping("/allUsers/{Id}")
    public String getAllUsersId(@PathVariable("Id") Long id,
                                Model model) {

        UserDTO userDTO = userService.getOne(id);
        model.addAttribute("allUsersId", userDTO);
        return "user/userForAdmin";
    }

    @PostMapping("/allUsers/{Id}")
    public String postAllUsersId(@PathVariable("Id") Long id,
                                 @ModelAttribute("userForm") UserDTO userDTO,
                                 BindingResult bindingResult,
                                 Authentication authentication) {

        //Сначала проверяем запрещенные системные слова (их нельзя никому)
        if (userDTO.getLogin().equalsIgnoreCase("admin") ||
                userDTO.getLogin().equalsIgnoreCase("login")) {
            bindingResult.rejectValue("login", "error.login", "Этот логин нельзя использовать");
            return "user/allUsers/" + id;
        }

        //Проверка на не изменился ли логин. Если изменился, то если этот логин ЗАНЯТ, то выдай ошибку
        if (!userService.getOne(id).getLogin().equals(userDTO.getLogin())) {
            if (userService.getUserByLogin(userDTO.getLogin()) != null) {
                bindingResult.rejectValue("login", "error.login", "Этот логин уже занят");
                return "user/allUsers/" + id;
            }
        }

        if (!userService.getOne(id).getEmail().equals(userDTO.getEmail())) {
            if (userService.getUserByEmail(userDTO.getEmail()) != null) {
                bindingResult.rejectValue("email", "error.email", "Эта электронная почта уже занята");
                return "user/allUsers/" + id;
            }
        }

        userService.editUser(userDTO, authentication);

        return "redirect:/user/allUsers/" + id;
    }

    @PostMapping("/allUsers/delete")
    public String deleteByAdmin(@RequestParam("id") Long id,
                                Authentication authentication) {

        userService.delete(id, authentication);
        return "redirect:/user/allUsers";
    }

    @PostMapping("/allUsers/restore")
    public String restoreByAdmin(@RequestParam("id") Long id) {

        userService.restore(id);
        return "redirect:/user/allUsers";
    }


    @GetMapping("/checkPassword")
    public String getCheckPassword() {

        return "user/checkPassword";
    }

    @PostMapping("/checkPassword")
    public String postCheckPassword(@RequestParam String oldPassword,
                                    Authentication authentication,
                                    Model model) {

        if (userService.checkPassword(oldPassword, authentication) == false) {
            model.addAttribute("error", "Неверный пароль");
            return "user/checkPassword";
        } else {
            return "/user/setNewPassword";
        }
    }

    @PostMapping("/setNewPassword")
    public String postSetNewPassword(@RequestParam("newPassword1") String newPassword1,
                                     @RequestParam("newPassword2") String newPassword2,
                                     Authentication authentication,
                                     Model model) {

        if (!newPassword1.equals(newPassword2)) {
            model.addAttribute("errorSetNewPassword", "Разные пароли");
            return "user/setNewPassword";
        } else {
            userService.setNewPassword(newPassword1, authentication);
            return "redirect:/user/myProfile";
        }
    }

    @PostMapping("/setNewPasswordByAdmin")
    public String postSetNewPasswordByAdmin(@RequestParam("newPassword") String newPassword,
                                            @RequestParam("userId") Long userId,
                                            Authentication authentication) {

        userService.setNewPasswordByAdmin(newPassword, userId, authentication);
        return "redirect:/user/allUsers";
    }

    @PostMapping("/delete")
    public String deleteByUser(@RequestParam("id") Long id,
                               HttpServletRequest request,
                               HttpServletResponse response,
                               Authentication authentication) {

        userService.delete(id, authentication);
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(request, response, authentication);
        return "redirect:/";
    }

    @GetMapping("/search")
    public String getUserSearch(Model model) {
        model.addAttribute("userFormForSearch", new UserDTO());
        return "user/userSearch";
    }

    @PostMapping("/search")
    public String postUserSearch(@ModelAttribute("userFormForSearch") UserDTO userDTO,
                                 @RequestParam(value = "searchAllDeleted", required = false) Boolean searchAllDeleted,
                                 @RequestParam(value = "isDeletedParam", required = false) Boolean isDeletedParam,
                                 Model model) {

        // Если чекбокс нажат (true), то нам плевать на селект — передаем null в сервис
        Boolean finalDeletedStatus = (searchAllDeleted != null && searchAllDeleted) ? null : isDeletedParam;

        // Если и чекбокс не нажат, и из селекта ничего не пришло (дефолт) — ставим false
        if (finalDeletedStatus == null && (searchAllDeleted == null || !searchAllDeleted)) {
            finalDeletedStatus = false;
        }

        model.addAttribute("users", userService.findUsers(userDTO, finalDeletedStatus));
        return "user/userSearch";
    }

    @PostMapping("/allUsers/changeRole")
    public String postChangeRole(@RequestParam("id") Long id) {

        userService.changeRole(id);
        return "redirect:/user/allUsers";
    }
}
