package com.example.exercise.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.regex.Pattern;

@Configuration
public class RegexConfig {

    @Value("${user.password.regex.regexp}")
    private String passwordRegex;

    @Value("${user.email.regex.regexp}")
    private String emailRegex;

    @Bean
    public Pattern passwordPattern() {
        return Pattern.compile(passwordRegex);
    }

    @Bean
    public Pattern emailPattern() {
        return Pattern.compile(emailRegex);
    }
}
