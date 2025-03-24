package com.contact.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.contact.project.dto.UserForm;
import com.contact.project.entity.User;
import com.contact.project.helpers.Message;
import com.contact.project.helpers.MessageType;
import com.contact.project.services.UserService;

import jakarta.servlet.http.HttpSession;
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
    public String registerUser(@ModelAttribute("user") UserForm userForm,HttpSession httpSession) {

        log.info("register started");

        log.info("printing userform " + userForm.toString());

        User user = new User();

        user.setUserName(userForm.getUserName());

        user.setEmail(userForm.getEmail());

        user.setPassword(userForm.getPassword());

        user.setAbout(userForm.getAbout());

        user.setPhoneNumber(userForm.getPhoneNumber());

        user.setProfilePic(
                "https://img.freepik.com/free-vector/blue-circle-with-white-user_78370-4707.jpg?t=st=1742800389~exp=1742803989~hmac=e822aa8e1edaa83e54c5231701e05580aaeef39628e1af4673d86e3835304a96&w=740");

        log.info("saving user");

        Message message = Message.builder().content("Successfully registerd").type(MessageType.green).build();

        httpSession.setAttribute("message", message);

        userService.saveUser(user);

        log.info("user saved successfully");
        return "redirect:/register";
    }

}
