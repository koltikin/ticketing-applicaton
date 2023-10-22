package com.cydeo.controller;

import com.cydeo.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;
    @PostMapping("/user/email-sent")
    public String EmailSent(@RequestParam("email") String email, Model model) {
        if (!email.contains("@")){
            return "redirect:/user/reset-password?error=true&email="+email;
        }

        emailService.sendEmail(email,"Reset Pass Word", "click the link and reset your pass word");

        model.addAttribute("email",email);
        return "/email-sent";
    }
}
