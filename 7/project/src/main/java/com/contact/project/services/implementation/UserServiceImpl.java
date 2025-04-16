package com.contact.project.services.implementation;

import com.contact.project.entity.User;
import com.contact.project.exception.ResouseNotFound;
import com.contact.project.helpers.AppConstant;
import com.contact.project.repositories.UserRepository;
import com.contact.project.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * This is a simple method that saves a User object to the database using the
     * userRepository
     */
    @Override
    public User saveUser(User user) {
        log.info("saving user in service");

        String passwordString = passwordEncoder.encode(user.getPassword());

        user.setPassword(passwordString);

        List<String> roleList = new ArrayList<>();

        roleList.add(AppConstant.ROLE_USER);

        user.setRoleList(roleList);

        return userRepository.save(user);
    }

    /**
     * This is a simple method that retrieves a User object from the database by its
     * ID
     */
    @Override
    public Optional<User> getUserById(int id) {

        return userRepository.findById(id);

    }

    /**
     * This is a method that updates a User object in the database
     */
    @Override
    public User updateUser(User user) {

        /**
         * This line retrieves a User object from the database by its ID using the
         * userRepository.
         * If the user is not found, it throws a ResouseNotFound exception with the
         * message "resouse not found".
         */
        User user2 = userRepository.findById(user.getId()).orElseThrow(() -> new ResouseNotFound("resouse not found"));

        user2.setUserName(user.getUserName());
        user2.setEmail(user.getEmail());
        user2.setPassword(user.getPassword());
        user2.setAbout(user.getAbout());
        user2.setPhoneNumber(user.getPhoneNumber());
        user2.setProfilePic(user.getProfilePic());
        user2.setEnabled(user.isEnabled());
        user2.setEmailVerified(user.isEmailVerified());
        user2.setPhoneVerified(user.isPhoneVerified());
        user2.setProvider(user.getProvider());
        user2.setProviderUserId(user.getProviderUserId());

        /*
         * This line saves the updated user2 object to the database using the
         * userRepository.
         */
        User savedUser = userRepository.save(user2);

        return savedUser;

    }

    @Override
    public void deleteUser(int id) {
        User user2 = userRepository.findById(id).orElseThrow(() -> new ResouseNotFound("resouse not found"));

        userRepository.delete(user2);
    }

    @Override
    public boolean isUserExist(int id) {
        User user = userRepository.findById(id).orElse(null);

        return user != null ? true : false;
    }

    @Override
    public boolean isUserExistByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);

        log.info("user exist by email " + email);

        return user != null ? true : false;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findByEmail(String emailString) {
        try {
            log.info("finding loged in user info using email");
            return userRepository.findByEmail(emailString)
                    .orElseThrow(() -> new RuntimeException("User with this email is not present in database"));

        } catch (Exception e) {
            log.error("Error occurred while finding user by email", e);
            throw new RuntimeException("Error occurred while finding user by email", e);
        }
    }


}
