package com.cydeo.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class MessagePropertyConfig {
    @Bean
    public MessageSource messageSource(){
        ResourceBundleMessageSource resource = new ResourceBundleMessageSource();

        resource.setBasenames("messages/userCreateValidation", "messages/projectCreateValidation");

        return resource;
    }
}
