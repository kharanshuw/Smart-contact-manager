package com.contact.project.services.implementation;

import com.contact.project.entity.User;
import com.contact.project.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/*
 * the CustomUserDetailService class provides a custom implementation of the UserDetailsService
 * interface, which is used by Spring Security to load user details from a database.
 */
@Service
public class CustomUserDetailService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(ContactServiceImpl.class);

    private UserRepository userRepository;

    private PasswordEncoder password;

    public CustomUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /*
     * This method is responsible for loading user details from the database based
     * on the provided
     * username. It returns a UserDetails object that represents the user. If the
     * user is not found,
     * it throws a UsernameNotFoundException.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return userRepository.findByEmail(username)
                .orElseThrow(() -> {
                    return new UsernameNotFoundException("user not found with this email id :" + username);
                });

    }

}
