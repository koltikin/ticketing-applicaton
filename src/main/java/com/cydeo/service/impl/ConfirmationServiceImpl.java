//package com.cydeo.service.impl;
//
//import com.cydeo.Repository.AccountConfirmationRepository;
//import com.cydeo.Repository.UserRepository;
//import com.cydeo.entity.AccountConfirmation;
//import com.cydeo.entity.User;
//import com.cydeo.mapper.UserMapper;
//import com.cydeo.service.ConfirmationService;
//import com.cydeo.service.UserService;
//import org.springframework.context.annotation.Lazy;
//import org.springframework.stereotype.Service;
//
//@Service
//public class ConfirmationServiceImpl implements ConfirmationService {
//    private final UserService userService;
//    private final UserMapper userMapper;
//    private final AccountConfirmationRepository repository;
//    private final UserRepository userRepository;
//
//    public ConfirmationServiceImpl(@Lazy UserService userService, UserMapper userMapper, AccountConfirmationRepository repository, UserRepository userRepository) {
//        this.userService = userService;
//        this.userMapper = userMapper;
//        this.repository = repository;
//        this.userRepository = userRepository;
//    }
//
//
//    @Override
//    public void saveConfirmation(String userName) {
//        User user = userMapper.convertToEntity(userService.findById(userName));
//        AccountConfirmation confirmation = new AccountConfirmation(user);
//        repository.save(confirmation);
//    }
//
//    @Override
//    public void save(AccountConfirmation confirmation) {
//        repository.save(confirmation);
//    }
//
//    @Override
//    public Boolean verifyAccount(String token) {
//
//
//        if (isTokenExist(token)) {
//            AccountConfirmation confirmation = findByToken(token);
//            User user = userRepository.findByUserNameAndIsDeleted(confirmation.getUser().getUserName(),false);
//            user.setEnabled(true);
//            userRepository.save(user);
//
//            confirmation.setDeleted(true);
//            repository.save(confirmation);
//
//            return true;
//        }
//
//        return false;
//    }
//
//    @Override
//    public String getTokenByUser(String userName) {
//        return repository.findTokenByUserName(userName);
//    }
//
//    @Override
//    public Boolean isTokenExist(String token) {
//        return repository.existsByToken(token);
//    }
//
//    @Override
//    public AccountConfirmation findByToken(String token) {
//        return repository.findByToken(token);
//    }
//
//}
