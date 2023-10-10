package com.cydeo.service.impl;

import com.cydeo.Repository.UserRepository;
import com.cydeo.entity.User;
import com.cydeo.entity.common.UserPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class SecurityService implements com.cydeo.service.SecurityService {
    private final UserRepository userRepository;

    public SecurityService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUserNameAndIsDeleted(username,false);
        if (user == null){
            throw new UsernameNotFoundException(username);
        }
        return new UserPrincipal(user);
    }
}
