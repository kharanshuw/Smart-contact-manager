package com.contact.project.services.implementation;

import com.contact.project.entity.User;
import com.contact.project.exception.ResouseNotFound;
import com.contact.project.helpers.AppConstant;
import com.contact.project.repositories.UserRepository;
import com.contact.project.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
 * /** Service implementation for User-related operations. This class contains methods for saving,
 * retrieving, updating, deleting, and checking the existence of User entities.
 */
@Service
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
     * Saves a User entity in the database. Encrypts the user's password and assigns a default role
     * before saving.
     * 
     * @param user The User entity to be saved.
     * @return The saved User entity.
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
     * Retrieves a User entity from the database by its ID.
     * 
     * @param id The ID of the user to retrieve.
     * @return Optional containing the User entity, if found.
     */
    @Override
    public Optional<User> getUserById(int id) {

        return userRepository.findById(id);

    }

    /**
     * Updates an existing User entity in the database. Validates the user's existence before
     * updating.
     * 
     * @param user The User entity with updated data.
     * @return The updated User entity.
     */
    @Override
    public User updateUser(User user) {

        /**
         * This line retrieves a User object from the database by its ID using the userRepository.
         * If the user is not found, it throws a ResouseNotFound exception with the message "resouse
         * not found".
         */
        User user2 = userRepository.findById(user.getId())
                .orElseThrow(() -> new ResouseNotFound("resouse not found"));

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
         * This line saves the updated user2 object to the database using the userRepository.
         */
        User savedUser = userRepository.save(user2);

        return savedUser;

    }

    /**
     * Deletes a User entity from the database by its ID. Throws an exception if the user does not
     * exist.
     * 
     * @param id The ID of the user to delete.
     */
    @Override
    public void deleteUser(int id) {
        User user2 = userRepository.findById(id)
                .orElseThrow(() -> new ResouseNotFound("resouse not found"));

        userRepository.delete(user2);
    }

    /**
     * Checks if a User entity exists in the database by its ID.
     * 
     * @param id The ID of the user to check.
     * @return true if the user exists, false otherwise.
     */
    @Override
    public boolean isUserExist(int id) {
        User user = userRepository.findById(id).orElse(null);

        return user != null ? true : false;
    }

    /**
     * Checks if a User entity exists in the database by its email.
     * 
     * @param email The email to check.
     * @return true if the user exists, false otherwise.
     */
    @Override
    public boolean isUserExistByEmail(String email) {
        User user = userRepository.findByEmail(email).orElse(null);

        log.info("user exist by email " + email);

        return user != null ? true : false;
    }
 
    /**
     * Retrieves all User entities from the database.
     * 
     * @return List of all users.
     */
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    /**
     * Finds a User entity in the database by email.
     * 
     * @param emailString The email to find the user by.
     * @return The User entity, if found.
     * @throws RuntimeException if the user is not found.
     */
    @Override
    public User findByEmail(String emailString) {
        try {
            log.info("finding loged in user info using email");
            return userRepository.findByEmail(emailString).orElseThrow(
                    () -> new RuntimeException("User with this email is not present in database"));

        } catch (Exception e) {
            log.error("Error occurred while finding user by email", e);
            throw new RuntimeException("Error occurred while finding user by email", e);
        }
    }
}
