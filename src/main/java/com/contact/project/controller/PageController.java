package com.contact.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.contact.project.dto.UserForm;
import com.contact.project.entity.User;
import com.contact.project.services.UserService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class PageController {

    private UserService userService;

    @Autowired
    public PageController(UserService userService) {
        this.userService = userService;
    }

    /**
     * This request mapping is for about page
     * 
     * @return
     */
    @GetMapping("/about")
    public String aboutPage() {
        log.info("running aboutPage");
        return "about";
    }

    /**
     * This request mapping is for service page
     * 
     * @return
     */

    @GetMapping("/service")
    public String servicePage() {
        log.info("running servicePage");
        return new String("service");
    }

    /**
     * This request mapping is for home page
     * 
     * @return
     */
    @GetMapping("/")
    public String homePage() {
        return new String("home");
    }

    /**
     * This request mapping is for contact page
     * 
     * @return
     */
    @GetMapping("/contact")
    public String contactPage() {
        return new String("contact");
    }

    /**
     * This request mapping is for login page
     * 
     * @return
     */
    @GetMapping("/login")
    public String loginPage() {
        return new String("login");
    }

    /**
     * This request mapping is for register page
     * 
     * @return
     */
    @GetMapping("/register")
    public String registerPage(Model model) {
        UserForm userForm = new UserForm();

        model.addAttribute("user", userForm);
        return new String("register");
    }

    /**
     * This request mapping is for register form submission
     * 
     * @return
     */
    @PostMapping("/do-register")
    public String registerUser(@ModelAttribute("user") UserForm userForm) {

        log.info("register started");

        log.info("printing userform " + userForm.toString());

        User user = User.builder()
                .userName(userForm.getUserName())
                .email(userForm.getEmail())
                .password(userForm.getPassword())
                .about(userForm.getAbout())
                .phoneNumber(userForm.getPhoneNumber())
                .profilePic("https://www.freepik.com/vectors/default-profile-pic")
                .build();

        log.info("saving user");
        userService.saveUser(user);

        log.info("user saved successfully");
        return "redirect:/register";
    }

}
