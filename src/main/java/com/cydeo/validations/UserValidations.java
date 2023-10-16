package com.cydeo.validations;

import com.cydeo.dto.UserDTO;
import com.cydeo.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Component
@AllArgsConstructor
public class UserValidations {

    private final UserService userService;
    public BindingResult addCustomValidationsCreate(UserDTO user, BindingResult bindingResult){

        if (userService.isUserExist(user)) {
            bindingResult.addError(new FieldError("user", "userName", "User Already Exist"));
        }
        if (userService.isPasswordNotConfirmed(user)) {
            bindingResult.addError(new FieldError("user", "passWordConfirm", "Pass Word didn't match"));
        }
        return bindingResult;
    }

    public BindingResult addCustomValidationsEdit(UserDTO user, BindingResult bindingResult){

        if (userService.isPasswordNotMatch(user)) {
            bindingResult.addError(new FieldError("user", "oldPassWord", "Pass Word didn't match"));
        }else {
            if (userService.isPasswordNotConfirmed(user)) {
                bindingResult.addError(new FieldError("user", "passWordConfirm", "Pass Word didn't match"));
            }
        }
        return bindingResult;
    }
}
