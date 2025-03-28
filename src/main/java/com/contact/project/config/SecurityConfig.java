package com.contact.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.contact.project.services.implementation.*;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class SecurityConfig {

    private CustomUserDetailService customUserDetailService;

    @Autowired
    public SecurityConfig(CustomUserDetailService customUserDetailService) {
        this.customUserDetailService = customUserDetailService;
    }

    /*
     * This method creates a `DaoAuthenticationProvider` bean, which is a type of
     * authentication provider that uses a `UserDetailsService` to load user
     * details.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {

        log.info("Creating DaoAuthenticationProvider bean");

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        daoAuthenticationProvider.setUserDetailsService(customUserDetailService);

        log.info("Set UserDetailsService to CustomUserDetailService");

        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

        log.info("Set PasswordEncoder to BCryptPasswordEncoder");

        return daoAuthenticationProvider;
    }

    /*
     * This method creates a `BCryptPasswordEncoder` bean, which is a password
     * encoder that uses the BCrypt algorithm to hash and verify passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("Creating BCryptPasswordEncoder bean");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(auth -> {
            auth.requestMatchers("user/**").authenticated();
            auth.anyRequest().permitAll();
        });


        httpSecurity.formLogin(Customizer.withDefaults());

        return httpSecurity.build();
    }
}
