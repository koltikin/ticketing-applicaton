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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
        dto.setEnabled(true);
        dto.setPassWord(passwordEncoder.encode(dto.getPassWord()));
        repository.save(mapper.convertToEntity(dto));

    }

    @Override
    public void update(UserDTO dto) {
        Long old_user_id = repository.findByUserNameAndIsDeleted(dto.getUserName(),false).getId();
        User new_user = mapper.convertToEntity(dto);
        new_user.setId(old_user_id);
        repository.save(new_user);

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
    public List<UserDTO> findAllByRole(String description) {
        return repository.findByRole_DescriptionIgnoreCaseAndIsDeleted(description,false)
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
    public Boolean isPasswordNotConfirmed(UserDTO userDto) {
            return !userDto.getPassWord().equals(userDto.getPassWordConfirm());
    }

    @Override
    public Boolean isPasswordNotMatch(UserDTO userDto) {
        var user = repository.findByUserNameAndIsDeleted(userDto.getUserName(),false);
        boolean passwordsMatch = passwordEncoder.matches(userDto.getPassWord(), user.getPassWord());

        return !passwordsMatch;
    }

}
