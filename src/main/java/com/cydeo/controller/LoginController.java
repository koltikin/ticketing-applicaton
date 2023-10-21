package com.cydeo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/login")
public class LoginController {
    @PostMapping("")
    public String processForm(@RequestParam("error") String error) {
        System.out.println(error);
        return "redirect:/login";
    }
    @GetMapping("")
    public String loginForm() {


        return "/login";
    }
}
