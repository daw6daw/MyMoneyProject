package com.petprojects.mymoneyproject.MVC.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Hidden
public class MainMVCController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

}
