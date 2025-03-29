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

/*
* the CustomUserDetailService class provides a custom implementation of the
* UserDetailsService interface, which is used by Spring Security to load user
* details from a database.
*/
@Slf4j
@Service
public class CustomUserDetailService implements UserDetailsService {

    private UserRepository userRepository;

    private PasswordEncoder password;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /*
     * This method is responsible for loading user details from the database based
     * on the provided username.
     * It returns a UserDetails object that represents the user.
     * If the user is not found, it throws a UsernameNotFoundException.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {

            /*
             * method first tries to find a user with the provided username using the
             * UserRepository.
             */
            Optional<User> optionalUser = userRepository.findByEmail(username);

            /*
             * if the user is found, it extracts the user's email, password, and roles from
             * the User object.
             */
            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                log.info("User found with email: {}", user.getEmail());

                /*
                 * user.getRoleList(): This method call retrieves the list of roles associated
                 * with the User object.
                 * .stream(): This method call converts the list of roles into a stream, which
                 * is a sequence of elements that can be processed in a pipeline fashion.
                 * .map(role -> role.startsWith("ROLE_") ? role.substring(5) : role): This
                 * method call applies a transformation to each element in the stream.
                 * Specifically, it checks if the role starts with the string "ROLE_". If it
                 * does, it removes the first 5 characters ("ROLE_") from the role using the
                 * substring(5) method. If it doesn't, it leaves the role unchanged.
                 * .collect(Collectors.toList()): This method call collects the transformed
                 * elements from the stream into a new list.
                 */
                List<String> roles = user.getRoleList().stream()
                        .map(role -> role.startsWith("ROLE_") ? role.substring(5) : role)
                        .collect(Collectors.toList());

                if (roles.isEmpty()) {
                    log.error("role list is empty");
                }

                /*
                 * This line of code is used to create a new `User` object using the
                 * `User.builder()` method from the
                 * `org.springframework.security.core.userdetails` package.
                 */
                return org.springframework.security.core.userdetails.User.builder()
                        .username(user.getEmail())
                        .password(user.getPassword())
                        .roles(roles.toArray(new String[roles.size()]))
                        .build();
            } else {
                log.info("User with given username not found");
                throw new UsernameNotFoundException("User not found with email: " + username);
            }
        } catch (Exception e) {
            log.error("Error in loadUserByUsername", e);
            throw new RuntimeException(e);
        }

    }

}
