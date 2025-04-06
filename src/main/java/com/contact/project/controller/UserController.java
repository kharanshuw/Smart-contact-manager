package com.contact.project.controller;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.contact.project.helpers.LoggedInUserFetcher;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequestMapping("/user")
public class UserController {

    @GetMapping("/dashboard")
    public String userDashboard(Authentication authentication) {
        String name = LoggedInUserFetcher.getLoggedInUserEmail(authentication);

        log.info("name of loged in user is " + name);

        log.info("dashboard running");
        return new String("user/dashboard");
    }

    @GetMapping("/profile")
    public String userProfile(Principal principal) {

        String name = principal.getName();

        log.info("name of loged in user is " + name);

        log.info("profile running");
        return new String("user/profile");
    }
}
