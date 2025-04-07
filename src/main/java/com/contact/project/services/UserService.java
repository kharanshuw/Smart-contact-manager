package com.contact.project.services;

import java.util.List;
import java.util.Optional;

import com.contact.project.entity.User;

public interface UserService {
    public User saveUser(User user);

    public Optional<User> getUserById(int id);

    public User updateUser(User user);

    public void deleteUser(int id);

    boolean isUserExist(int id);

    boolean isUserExistByEmail(String email);

    List<User> getAllUsers();

    public User findByEmail(String emailString);
}
