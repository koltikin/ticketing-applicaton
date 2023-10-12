package com.cydeo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SecurityConfig {
//    @Bean
//    public UserDetailsService userDetailsService(PasswordEncoder encoder){
//        List<UserDetails> userList = new ArrayList<>();
//        userList.add(
//                new User("ziya",encoder.encode("bismillah"),
//                        List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))));
//
//        userList.add(
//                new User("yusuf",encoder.encode("yusuf123"),
//                        List.of(new SimpleGrantedAuthority("ROLE_MANAGER"))));
//
//        userList.add(
//                new User("alim",encoder.encode("alim0908"),
//                        List.of(new SimpleGrantedAuthority("ROLE_EMPLOYEE"))));
//
//        return new InMemoryUserDetailsManager(userList);
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests()
//                .antMatchers("/user/**","/project/create").hasRole("ADMIN")
                .antMatchers("/user/**","/project/create").hasAuthority("Admin")
//                .antMatchers("/project/**","/task/create").hasRole("MANAGER")
//                .antMatchers("/task/pending-tasks","/task/archive-tasks").hasRole("EMPLOYEE")
//                .antMatchers("/task/**").hasAnyRole("EMPLOYEE","MANAGER")
//                .antMatchers("/task/**").hasAuthority("ROLE_MANAGER")

                .antMatchers("/",
                        "/login",
                        "/welcome",
                        "/fragments/**",
                        "/assets/**",
                        "/images/**"
                ).permitAll()
                .anyRequest().authenticated()
                .and()
//                .httpBasic()// popup box
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/user/create")
                .failureUrl("/login?error=true")
                .and().build();
    }

}
