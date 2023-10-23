package com.cydeo.service;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;

import java.util.List;


public interface UserService extends CrudService<UserDTO,String>{

    List<UserDTO> findAllByRoleDetail();
    List<UserDTO> findAllByRole(String description);
    boolean isUserExist(UserDTO userDto);
    boolean isUserExist(String username);
    Boolean isPasswordNotConfirmed(UserDTO user);

    Boolean isPasswordNotMatch(UserDTO userDTO);

    Boolean isRoleChanged(UserDTO userDto);
}
