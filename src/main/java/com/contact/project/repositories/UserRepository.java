package com.contact.project.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.contact.project.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    // extra methods db related
    // custom query method

    public Optional<User> findByEmail(String emailString);

    public Optional<User> findByEmailAndPassword(String email, String password);
}
