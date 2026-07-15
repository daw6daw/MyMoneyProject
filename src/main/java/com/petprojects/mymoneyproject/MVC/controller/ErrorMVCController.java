package com.petprojects.mymoneyproject.MVC.controller;

import com.petprojects.mymoneyproject.DTO.UserDTO;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@Hidden
@RequestMapping("error")
public class ErrorMVCController {

    @GetMapping("/permit-denied")
    public String permitDenied() {
        return "errors/permit-denied";
    }
}
