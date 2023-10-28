package com.cydeo.controller;

import com.cydeo.Repository.AccountConfirmationRepository;
import com.cydeo.dto.UserDTO;
import com.cydeo.enums.UserStatus;
import com.cydeo.securit.AuthSuccessHandler;
import com.cydeo.service.RoleService;
import com.cydeo.service.UserService;
import com.cydeo.validations.UserValidations;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
     private final RoleService roleService;
     private final UserService userService;
     private final UserValidations userValidations;
     private final AuthSuccessHandler authSuccessHandler;
     private final AccountConfirmationRepository confirmationRepository;


    @PreAuthorize("hasAuthority('Admin')")
    @GetMapping("/create")
    public String userCreate(Model model){
        model.addAttribute("user",new UserDTO());
        model.addAttribute("roles",roleService.findAll());
        model.addAttribute("userList",userService.findAll());
        model.addAttribute("userStatuses", UserStatus.values());
        return "user/create";
    }
    @PreAuthorize("hasAuthority('Admin')")
    @PostMapping("/save")
    public String userSave(@Valid @ModelAttribute("user") UserDTO user, BindingResult bindingResult, Model model){

        bindingResult = userValidations.addCustomValidationsCreate(user,bindingResult);

        if(bindingResult.hasErrors()){

            model.addAttribute("roles",roleService.findAll());
            model.addAttribute("userList",userService.findAll());
            model.addAttribute("userStatuses", UserStatus.values());

            return "user/create";
        }
        userService.save(user);
        userService.saveUserConfirmation(user.getUserName());
        userService.sendUserVerificationEmail(user.getUserName());

        return "redirect:/user/create";
    }

    @GetMapping("/verify")
    public String userVerify(@RequestParam("token") String token, Model model){
        Boolean isVerifyied = userService.verifyUserAccount(token);
        model.addAttribute("isVerifyied", isVerifyied);
        return "/user/verify-user";
    }

    @PreAuthorize("hasAuthority('Admin')")
    @GetMapping("/delete/{userName}")
    public String userDelete(@PathVariable("userName") String username){
        userService.delete(username);
        return "redirect:/user/create";
    }
    @PreAuthorize("hasAuthority('Admin')")
    @GetMapping("/update/{userName}")
    public String userUpdate(@PathVariable("userName") String username, Model model){

        model.addAttribute("user",userService.findById(username));
        model.addAttribute("roles",roleService.findAll());
        model.addAttribute("userList",userService.findAll());
        model.addAttribute("userStatuses", UserStatus.values());

        return "/user/update";
    }

    @PreAuthorize("hasAuthority('Admin')")
    @PostMapping("/update-save")
    public String userUpdate(@Valid @ModelAttribute("user") UserDTO user, BindingResult bindingResult, Model model){

        if (bindingResult.hasFieldErrors("firstName") || bindingResult.hasFieldErrors("lastName")
                || bindingResult.hasFieldErrors("userName")|| bindingResult.hasFieldErrors("phone")
                || bindingResult.hasFieldErrors("role")|| bindingResult.hasFieldErrors("gender")
                || bindingResult.hasFieldErrors("enabled")){
            model.addAttribute("roles",roleService.findAll());
            model.addAttribute("userList",userService.findAll());
            model.addAttribute("userStatuses", UserStatus.values());
            return "/user/update";
        }

        userService.update(user);

        return "redirect:/user/create";
    }

    @PreAuthorize("hasAnyAuthority('Admin','Manager','Employee')")
    @GetMapping("/edit")
    public String userEdit(Model model){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        model.addAttribute("user",userService.findById(username));
        model.addAttribute("roles",roleService.findAll());

        return "/user/edit";
    }

    @PreAuthorize("hasAnyAuthority('Admin','Manager','Employee')")
    @PostMapping("/edit")
    public String userEditSave(@Valid @ModelAttribute("user") UserDTO user, BindingResult bindingResult,Model model){

       bindingResult = userValidations.addCustomValidationsEdit(user,bindingResult);

       if (bindingResult.hasErrors()){
           model.addAttribute("roles",roleService.findAll());
           return "/user/edit";
       }
       if (userService.isRoleChanged(user)){
           userService.update(user);
       }else {
           switch (user.getRole().getDescription()){
               case "Admin" :
                   userService.update(user);
                   return "redirect:/user/create";
               case "Manager" :
                   userService.update(user);
                   return "redirect:/task/create";
               case "Employee" :
                   userService.update(user);
                   return "redirect:/task/pending-tasks";
           }
       }
        return "redirect:/login";
    }
    @PostMapping("/email-sent")
    public String userPasswordResetEmail(@RequestParam("email") String email, Model model){
        if ((email.substring(1,email.length()-1)).contains("@")){
            Boolean isUserExist = userService.isUserExist(email);
            if (isUserExist) {
                userService.sendUserPassWordResetLink(email);
                model.addAttribute("email",email);
                return "/user/email-sent";
            }
            return "redirect:/user/reset-password?error=true&exist="+isUserExist+"&email="+email;
        }
        return "redirect:/user/reset-password?error=true&invalid=true&email="+email;
    }


    @PostMapping("/reset-password-confirmation")
    public String userPassWordResetConfirm(@RequestParam("new_password") String password,
                                           @RequestParam("re-password") String rePassword,
                                           @RequestParam("token")String token, Model model){

        Boolean isMetRequirements = userService.isMetRequirement(password);
        Boolean isMatch = password.equals(rePassword);
        if (isMetRequirements){
            if (isMatch){
                userService.resetPassWord(token, password);
                return "/user/pass-reset-confirm";
            }
            return "redirect:/user/change-password?error=true&isMatch=false&new_password="+password+"&token="+token;
        }
        return "redirect:/user/change-password?error=true&new_password="+password+"&re_password="+rePassword+"&token="+token;
    }


}
