package com.cydeo.controller;

import com.cydeo.service.EmailService;
import com.cydeo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;
    private final UserService userService;
    @PostMapping("/user/email-sent")
    public String EmailSent(@RequestParam("email") String email, Model model) {
        if (!email.contains("@")){
            return "redirect:/user/reset-password?error=true&email="+email;

        } else if (!userService.isUserExist(email)) {
            return "redirect:/user/reset-password?exist=false&email="+email;
        }

        emailService.sendEmail(email);

        model.addAttribute("email",email);
        return "/email-sent";
    }
}
