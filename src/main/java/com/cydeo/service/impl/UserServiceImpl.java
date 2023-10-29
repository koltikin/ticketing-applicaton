package com.cydeo.service.impl;

import com.cydeo.Repository.AccountConfirmationRepository;
import com.cydeo.Repository.UserPassWordResetRepository;
import com.cydeo.Repository.UserRepository;
import com.cydeo.dto.ProjectDTO;
import com.cydeo.dto.TaskDTO;
import com.cydeo.dto.UserDTO;
import com.cydeo.entity.AccountConfirmation;
import com.cydeo.entity.User;
import com.cydeo.entity.UserResetPassWord;
import com.cydeo.mapper.UserMapper;
import com.cydeo.service.EmailService;
import com.cydeo.service.ProjectService;
import com.cydeo.service.TaskService;
import com.cydeo.service.UserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
//@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper mapper;
    private final UserRepository repository;
    private final ProjectService projectService;
    private final TaskService taskService;
    private final PasswordEncoder passwordEncoder;
    private final AccountConfirmationRepository confirmationRepository;
    private final EmailService emailService;
    private final UserPassWordResetRepository passWordResetRepository;

    public UserServiceImpl(UserMapper mapper, UserRepository repository, @Lazy ProjectService projectService, @Lazy TaskService taskService, PasswordEncoder passwordEncoder,
                           AccountConfirmationRepository confirmationRepository, EmailServiceImpl emailService, UserPassWordResetRepository passWordResetRepository) {
        this.mapper = mapper;
        this.repository = repository;
        this.projectService = projectService;
        this.taskService = taskService;
        this.passwordEncoder = passwordEncoder;
        this.confirmationRepository = confirmationRepository;
        this.emailService = emailService;
        this.passWordResetRepository = passWordResetRepository;
    }


    @Override
    public List<UserDTO> findAll() {
        return repository.findAllByIsDeletedOrderByFirstName(false).stream()
                .map(mapper::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO findById(String userName) {
        return mapper.convertToDto(repository.findByUserNameAndIsDeleted(userName, false));
    }

    @Override
    public void save(UserDTO dto) {
//        dto.setEnabled(true);
        dto.setPassWord(passwordEncoder.encode(dto.getPassWord()));
        repository.save(mapper.convertToEntity(dto));

    }

    @Override
    public void update(UserDTO dto) {
        User old_user = repository.findByUserNameAndIsDeleted(dto.getUserName(), false);
        User updatedUser = mapper.convertToEntity(dto);
        updatedUser.setId(old_user.getId());

        if (dto.getPassWord() == null) {
            updatedUser.setPassWord(old_user.getPassWord());
        } else {
            updatedUser.setPassWord(passwordEncoder.encode(dto.getPassWord()));
            updatedUser.setEnabled(true);
        }

        repository.save(updatedUser);

    }

    @Override
    public void delete(String username) {
        User user = repository.findByUserNameAndIsDeleted(username, false);
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
        User user = repository.findByUserNameAndIsDeleted(username, false);
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

    private boolean checkIfUserCanBeDeleted(User user) {
        switch (user.getRole().getDescription()) {
            case "Manager":
                List<ProjectDTO> notCompletedProjects = projectService.listAllNotCompletedPrjByManager(user);
                return notCompletedProjects.size() == 0;
            case "Employee":
                List<TaskDTO> notCompletedTasks = taskService.listAllNotCompletedTaskByEmployee(user);
                return notCompletedTasks.size() == 0;

            default:
                return true;
        }
    }

    @Override
    public boolean isUserExist(UserDTO userDto) {
        var user = repository.findByUserNameAndIsDeleted(userDto.getUserName(), false);
        if (user != null && user.getId() != null) {
            return repository.existsByUserNameAndIsDeleted(userDto.getUserName(), false);
        }
        return false;
    }

    @Override
    public boolean isUserExist(String username) {
        return repository.existsByUserNameAndIsDeleted(username, false);
    }

    @Override
    public Boolean isPasswordNotConfirmed(UserDTO userDto) {
        return !userDto.getPassWord().equals(userDto.getPassWordConfirm());
    }

    @Override
    public Boolean isPasswordNotMatch(UserDTO userDto) {
        var user = repository.findByUserNameAndIsDeleted(userDto.getUserName(), false);
        boolean passwordsMatch = passwordEncoder.matches(userDto.getOldPassWord(), user.getPassWord());

        return !passwordsMatch;
    }

    @Override
    public Boolean isRoleChanged(UserDTO userDto) {
        var user = repository.findByUserNameAndIsDeleted(userDto.getUserName(), false);

        return !user.getRole().getDescription().equals(userDto.getRole().getDescription());
    }

    @Override
    public Boolean isUserExistByEmail(String userName) {
        return repository.existsByUserNameAndIsDeleted(userName, false);

    }

    @Override
    public Boolean verifyUserAccount(String token) {

        if (confirmationRepository.existsByToken(token)) {
            AccountConfirmation confirmation = confirmationRepository.findByToken(token);
            User user = repository.findByUserNameAndIsDeleted(confirmation.getUser().getUserName(), false);

            user.setEnabled(true);
            repository.save(user);

            confirmation.setDeleted(true);
            confirmationRepository.save(confirmation);

            return true;
        }

        return false;
    }

    @Override
    public void saveUserConfirmation(String userName) {
        User user = mapper.convertToEntity(findById(userName));
        AccountConfirmation confirmation = new AccountConfirmation(user);
        confirmationRepository.save(confirmation);
    }

    @Override
    public void sendUserVerificationEmail(String userName) {
        if (isUserExistByEmail(userName)) {
            User user = mapper.convertToEntity(findById(userName));
            if (!user.isEnabled()) {

                String token = confirmationRepository.findTokenByUserName(userName);
                String subject = "Cydeo ticketing Account Verify";

                String message = "Click the link blow to Verify Your Cydeo Ticketing Account.\n\n " +
                        "http://localhost:8080/user/verify?token=" + token + "\n\n" +
                        "from: cydeo.ticketing@gmail.com";

                emailService.sendSimpleMessage(userName,subject,message);
            }
        }
    }

    @Override
    public void sendUserPassWordResetLink(String email) {
        User user = repository.findByUserNameAndIsDeleted(email,false);

        UserResetPassWord resetPassWord = new UserResetPassWord(user);
        passWordResetRepository.save(resetPassWord);

        String token = resetPassWord.getToken();

        String subject = "Cydeo Ticketing Account Pass Word Rest";
        String message = "You recently requested to change your password.\n" +
                "\n" +
                "If it wasn't you, please disregard this email and make sure you can still login to your account." +
                " If it was you, then confirm the password change by clicking the button below.\n\n" +
                "http://localhost:8080/user/change-password?token="+token+"\n\n" +
                "If you are having any issues with your account, please don’t hesitate to contact us using\n\ncydeo.ticketing@gmail.com.\n" +
                "\n" +
                "\n" +
                "Thanks!\n" +
                "CYDEO Team\n";

        emailService.sendSimpleMessage(email,subject,message);
    }

    @Override
    public Boolean isMetRequirement(String password) {
            // Define regular expressions for each requirement
            String lengthRegex = ".{8,}";
            String lowercaseRegex = "[a-z]";
            String uppercaseRegex = "[A-Z]";
            String numberRegex = "[0-9]";
            String specialCharRegex = "[\\W_]";

            // Compile regular expressions
            Pattern lengthPattern = Pattern.compile(lengthRegex);
            Pattern lowercasePattern = Pattern.compile(lowercaseRegex);
            Pattern uppercasePattern = Pattern.compile(uppercaseRegex);
            Pattern numberPattern = Pattern.compile(numberRegex);
            Pattern specialCharPattern = Pattern.compile(specialCharRegex);

            // Match each requirement against the password
            Matcher lengthMatcher = lengthPattern.matcher(password);
            Matcher lowercaseMatcher = lowercasePattern.matcher(password);
            Matcher uppercaseMatcher = uppercasePattern.matcher(password);
            Matcher numberMatcher = numberPattern.matcher(password);
            Matcher specialCharMatcher = specialCharPattern.matcher(password);

            // Check if the password meets the requirements
            return lengthMatcher.find() &&
                    (lowercaseMatcher.find() || uppercaseMatcher.find() || numberMatcher.find() || specialCharMatcher.find()) &&
                    (lowercaseMatcher.find() ? 1 : 0) +
                            (uppercaseMatcher.find() ? 1 : 0) +
                            (numberMatcher.find() ? 1 : 0) +
                            (specialCharMatcher.find() ? 1 : 0) >= 3;

    }

    @Override
    public void resetPassWord(String token, String new_password) {
        UserResetPassWord userResetPassWord = passWordResetRepository.findByToken(token);
        String userName = userResetPassWord.getUser().getUserName();
        User user = repository.findByUserNameAndIsDeleted(userName,false);
        if (user!=null){
            user.setPassWord(passwordEncoder.encode(new_password));
            user.setLastUpdateUserId(user.getId());
            user.setLastUpdateDateTime(LocalDateTime.now());
            repository.save(user);
            userResetPassWord.setDeleted(true);
            passWordResetRepository.save(userResetPassWord);
        }
        userResetPassWord.setDeleted(true);
        passWordResetRepository.save(userResetPassWord);
    }

    @Override
    public Boolean isPasswordTokenValid(String token) {
        return passWordResetRepository.existsByToken(token);
    }

    @Override
    public Boolean isUserActive(String username) {
        return repository.findByUserNameAndIsDeleted(username,false).isEnabled();
    }
}
