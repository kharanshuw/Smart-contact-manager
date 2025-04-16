package com.contact.project.controller;

import com.contact.project.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@Slf4j
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/dashboard")
    public String userDashboard() {
        log.info("dashboard running");
        return new String("user/dashboard");
    }

    @GetMapping("/profile")
    public String userProfile() {

        log.info("profile running");
        return new String("user/profile");
    }

}
