package com.contact.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/user")
public class UserController {


    @GetMapping("/dashboard")
    public String userDashboard() {
        return new String("user/dashboard");
    }
    
    
    @GetMapping("/profile")
    public String userProfile() {
        return new String("user/profile");
    }
}
