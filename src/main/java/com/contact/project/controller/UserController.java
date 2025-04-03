package com.contact.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;


@Controller
@Slf4j
@RequestMapping("/user")
public class UserController {


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
