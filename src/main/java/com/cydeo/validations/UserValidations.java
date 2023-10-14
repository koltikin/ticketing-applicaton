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
    public BindingResult addCustomValidations(UserDTO user, BindingResult bindingResult){

        if (userService.isUserExist(user)) {
            bindingResult.addError(new FieldError("user", "userName", "User Already Exist"));
        }
        return bindingResult;
    }
}
