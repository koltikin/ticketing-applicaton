package com.cydeo.service;
import com.cydeo.dto.UserDTO;

import java.util.List;


public interface UserService extends CrudService<UserDTO,String>{

    List<UserDTO> findAllByRoleDetail();
    List<UserDTO> findAllByRole(String description);
    boolean isUserExist(UserDTO userDto);
    boolean isUserExist(String username);
    Boolean isPasswordNotConfirmed(UserDTO user);

    Boolean isPasswordNotMatch(UserDTO userDTO);

    Boolean isRoleChanged(UserDTO userDto);

    Boolean isUserExistByEmail(String userName);

    void saveUserConfirmation(String userName);

    Boolean verifyUserAccount(String token);
    void sendUserVerificationEmail(String userName);
    void sendUserPassWordResetLink(String email);

    Boolean isMetRequirement(String password);
    void resetPassWord(String token, String new_password);

}
