package com.contact.project.config;

import com.contact.project.services.implementation.CustomUserDetailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Slf4j
public class SecurityConfig {

    private CustomUserDetailService customUserDetailService;

    private OAuthAuthenticationSuccessHandler oAuthSuccessHandler;

    private AuthFailureHandler authFailureHandler;

    @Autowired
    public SecurityConfig(CustomUserDetailService customUserDetailService,AuthFailureHandler authFailureHandler,
            OAuthAuthenticationSuccessHandler oAuthSuccessHandler) {

        this.customUserDetailService = customUserDetailService;

        this.oAuthSuccessHandler = oAuthSuccessHandler;

        this.authFailureHandler = authFailureHandler;
    }

    /*
     * This method creates a `DaoAuthenticationProvider` bean, which is a type of
     * authentication
     * provider that uses a `UserDetailsService` to load user details from database
     * .
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
     * encoder that uses the
     * BCrypt algorithm to hash and verify passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        log.info("Creating BCryptPasswordEncoder bean");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeHttpRequests(auth -> {
            auth.requestMatchers("/user/**").authenticated();
            auth.requestMatchers("/user/contacts/**").authenticated();
            auth.requestMatchers("/api/contact/**").permitAll();

            auth.anyRequest().permitAll();

        });

        // form login configuration
        httpSecurity.formLogin(formlogin -> {
            formlogin.loginPage("/login");

            formlogin.loginProcessingUrl("/authenticate");

            formlogin.defaultSuccessUrl("/user/dashboard");

            // formlogin.failureForwardUrl("/login?error=true");

            formlogin.usernameParameter("email");

            formlogin.passwordParameter("password");

            formlogin.failureHandler(authFailureHandler);

        });

        // logout configuration
        httpSecurity.logout(logoutform -> {
            log.info("logging out..... ");

            logoutform.logoutUrl("/logout");

            logoutform.logoutSuccessUrl("/login?logout=true");

            log.info("logged out successfully");
        });

        // oauth2 configuration
        httpSecurity.oauth2Login(oauth2 -> {
            oauth2.loginPage("/login");

            oauth2.successHandler(oAuthSuccessHandler);
        });

        // csrf configuration
        // httpSecurity.csrf(
        // csrf ->
        // csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));

        httpSecurity.sessionManagement(
                session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED));

        return httpSecurity.build();
    }
}
