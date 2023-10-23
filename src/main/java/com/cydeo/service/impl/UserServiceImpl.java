package com.cydeo.service.impl;

import com.cydeo.Repository.UserRepository;
import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.User;
import com.cydeo.mapper.UserMapper;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
//@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper mapper;
    private final UserRepository repository;
    private final ProjectService projectService;
    private final TaskService taskService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserMapper mapper, UserRepository repository, @Lazy ProjectService projectService, @Lazy TaskService taskService, PasswordEncoder passwordEncoder) {
        this.mapper = mapper;
        this.repository = repository;
        this.projectService = projectService;
        this.taskService = taskService;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public List<UserDTO> findAll() {
        return repository.findAllByIsDeletedOrderByFirstName(false).stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO findById(String userName) {
       return mapper.convertToDto(repository.findByUserNameAndIsDeleted(userName,false));
    }
    @Override
    public void save(UserDTO dto) {
//        dto.setEnabled(true);
        dto.setPassWord(passwordEncoder.encode(dto.getPassWord()));
        repository.save(mapper.convertToEntity(dto));

    }

    @Override
    public void update(UserDTO dto) {
        User old_user = repository.findByUserNameAndIsDeleted(dto.getUserName(),false);
        User updatedUser = mapper.convertToEntity(dto);
        updatedUser.setId(old_user.getId());
        if (dto.getPassWord()==null){
            updatedUser.setPassWord(old_user.getPassWord());
        }else {
            updatedUser.setPassWord(passwordEncoder.encode(dto.getPassWord()));
        }
        repository.save(updatedUser);

    }

    @Override
    public void delete(String username) {
        User user = repository.findByUserNameAndIsDeleted(username,false);
        if (checkIfUserCanBeDeleted(user)) {
            user.setUserName(user.getUserName() + '-' + LocalDateTime.now());
//            if (user.getRole().getDescription().equals("Employee")){
//                taskService.listAllTasksByEmployee(user)
//                        .forEach(task -> task.setIsDeleted(true));
//            }
//            if (user.getRole().getDescription().equals("Manager")){
//                projectService.listAllProjectByManager(user)
//                        .forEach(prj -> prj.setIsDeleted(true));
//            }
            user.setIsDeleted(true);
            repository.save(user);
        }

    }

    @Override
    public List<UserDTO> findAllByRoleDetail() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = repository.findByUserNameAndIsDeleted(username,false);
        String description = user.getRole().getDescription();
        if (description.equals("Admin")) {
            return repository.findByRole_DescriptionIgnoreCaseAndIsDeleted("Manager", false)
                    .stream().map(mapper::convertToDto)
                    .collect(Collectors.toList());
        }
        return Collections.singletonList(mapper.convertToDto(user));
    }

    public List<UserDTO> findAllByRole(String description) {
            return repository.findByRole_DescriptionIgnoreCaseAndIsDeleted(description, false)
                    .stream().map(mapper::convertToDto)
                    .collect(Collectors.toList());
    }

    private boolean checkIfUserCanBeDeleted(User user){
        switch (user.getRole().getDescription()){
            case "Manager":
                List<ProjectDTO> notCompletedProjects = projectService.listAllNotCompletedPrjByManager(user);
                return notCompletedProjects.size() == 0;
            case "Employee":
                List<TaskDTO> notCompletedTasks = taskService.listAllNotCompletedTaskByEmployee(user);
                return notCompletedTasks.size() == 0;

            default: return true;
        }
    }

    @Override
    public boolean isUserExist(UserDTO userDto) {
        var user = repository.findByUserNameAndIsDeleted(userDto.getUserName(),false);
        if (user !=null && user.getId()!=null) {
            return repository.existsByUserNameAndIsDeleted(userDto.getUserName(), false);
        }
        return false;
    }

    @Override
    public boolean isUserExist(String username) {
        return repository.existsByUserNameAndIsDeleted(username,false);
    }

    @Override
    public Boolean isPasswordNotConfirmed(UserDTO userDto) {
            return !userDto.getPassWord().equals(userDto.getPassWordConfirm());
    }

    @Override
    public Boolean isPasswordNotMatch(UserDTO userDto) {
        var user = repository.findByUserNameAndIsDeleted(userDto.getUserName(),false);
        boolean passwordsMatch = passwordEncoder.matches(userDto.getOldPassWord(), user.getPassWord());

        return !passwordsMatch;
    }

    @Override
    public Boolean isRoleChanged(UserDTO userDto) {
        var user = repository.findByUserNameAndIsDeleted(userDto.getUserName(),false);

        return !user.getRole().getDescription().equals(userDto.getRole().getDescription());
    }
}
