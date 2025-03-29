package com.contact.project.services.implementation;

import java.util.*;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.contact.project.entity.User;
import com.contact.project.repositories.UserRepository;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomUserDetailService implements UserDetailsService {

    private UserRepository userRepository;

    private PasswordEncoder password;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Optional<User> optionalUser = userRepository.findByEmail(username);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                log.info("User found with email: {}", user.getEmail());

                List<String> roles = user.getRoleList().stream()
                        .map(role -> role.startsWith("ROLE_") ? role.substring(5) : role)
                        .collect(Collectors.toList());

                if (roles.isEmpty()) {
                    log.error("role list is empty");
                }

                return org.springframework.security.core.userdetails.User.builder()
                        .username(user.getEmail())
                        .password(user.getPassword())
                        .roles(roles.toArray(new String[roles.size()]))
                        .build();
            } else {
                log.info("User with given username not found");
                throw new UsernameNotFoundException("User not found");
            }
        } catch (Exception e) {
            log.error("Error in loadUserByUsername", e);
            throw new RuntimeException(e);
        }

    }

}
