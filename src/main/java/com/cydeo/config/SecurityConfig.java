package com.cydeo.config;

import com.cydeo.service.SecurityService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final SecurityService securityService;
    private final AuthSuccessHandler authSuccessHandler;

    public SecurityConfig(SecurityService securityService, AuthSuccessHandler authSuccessHandler) {
        this.securityService = securityService;
        this.authSuccessHandler = authSuccessHandler;
    }


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
//                .antMatchers("/project/**").hasAuthority("Manager")
//                .antMatchers("/user/**").hasAuthority("Admin")
//                .antMatchers("/project/**").hasAuthority("Manager")
//                .antMatchers("/project/create").hasAnyAuthority("Manager","Admin")

                .antMatchers("/project/employee/**").hasAuthority("Employee")
//                .antMatchers("/task/**").hasAuthority("Manager")
                .antMatchers("/task/pending-tasks",
                        "/task/task-update", "/task/task-update",
                        "/task/archive-tasks").hasAuthority("Employee")
//                .antMatchers("/project/**","/task/create").hasRole("MANAGER")
//                .antMatchers("/task/pending-tasks","/task/archive-tasks").hasRole("EMPLOYEE")
//                .antMatchers("/task/**").hasAnyRole("EMPLOYEE","MANAGER")
//                .antMatchers("/task/**").hasAuthority("ROLE_MANAGER")

                .antMatchers("/",
                        "/login",
                        "/fragments/**",
                        "/assets/**",
                        "/images/**"
                ).permitAll()
                .anyRequest().authenticated()
                .and()
//                .httpBasic()// popup box
                    .formLogin()
                    .loginPage("/login")
//                    .defaultSuccessUrl("/welcome")
                    .successHandler(authSuccessHandler)
                    .failureUrl("/login?error=true")
                .and()
                .logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutSuccessUrl("/login")
                .and()
                .rememberMe()
                    .tokenValiditySeconds(120)
                    .key("cydeo")
                    .userDetailsService(securityService)
                .and()
                .build();
    }

}
