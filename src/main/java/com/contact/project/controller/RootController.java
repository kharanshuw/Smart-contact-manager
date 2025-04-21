package com.contact.project.controller;

import com.contact.project.entity.User;
import com.contact.project.helpers.LoggedInUserFetcher;
import com.contact.project.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;


/**
 * RootController provides global methods for controllers in the application. It includes
 * functionality for fetching and injecting details of the currently logged-in user into the model.
 */
@ControllerAdvice
@Slf4j
public class RootController {

    private UserService userService;


    @Autowired
    public RootController(UserService userService) {
        this.userService = userService;
    }


    /**
     * Fetches and adds logged-in user details to the model, making them accessible across all
     * views.
     * 
     * @param model Spring's Model object for transferring data to the view.
     * @param authentication Spring Security's Authentication object containing user credentials.
     */
    @ModelAttribute
    public void getLoggedInUserdetails(Model model, Authentication authentication) {

        if (authentication == null) {
            return;
        }

        log.info("adding logged in user info to model...");

        String emailString = LoggedInUserFetcher.getLoggedInUserEmail(authentication);

        log.info("name of loged in user is " + emailString);

        User user = userService.findByEmail(emailString);

        log.info("user name" + user.getUserName());

        log.info("user email " + user.getEmail());

        model.addAttribute("user", user);

        log.info("added successfully logged in user details in model");
    }

}
