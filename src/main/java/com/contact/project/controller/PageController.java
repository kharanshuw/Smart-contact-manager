package com.contact.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.contact.project.dto.UserForm;
import com.contact.project.entity.User;
import com.contact.project.helpers.Message;
import com.contact.project.helpers.MessageType;
import com.contact.project.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class PageController {

    private UserService userService;

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

    /*
     * * Creates a new `UserForm` object
     * Adds the `UserForm` object to the model with the attribute name "user"
     * Returns the string "register", which will render the "register" view
     */
    @GetMapping("/register")
    public String registerPage(Model model) {
        UserForm userForm = new UserForm();

        model.addAttribute("user", userForm);
        return new String("register");
    }

    /*
     * * Validating the `UserForm` object
     * Creating a new `User` object based on the validated form data
     * Saving the `User` object to the database
     * Setting a success message in the HTTP session
     * Redirecting the user to the `/register` endpoint
     */
    @PostMapping("/process-Register")
    public String processRegister(@Valid @ModelAttribute("user") UserForm userForm, BindingResult bindingResult,
            HttpSession httpSession) {

        log.info("register started");
        /*
         * This line checks if there are any validation errors in the `BindingResult`
         * object. If there are errors, the method returns the "register" view.
         */
        if (bindingResult.hasErrors()) {
            log.error("error in register form");
            return "register";
        }

        /*
         * creates a new `User` object.
         */
        User user = new User();

        user.setUserName(userForm.getUserName());

        user.setEmail(userForm.getEmail());

        user.setPassword(userForm.getPassword());

        user.setAbout(userForm.getAbout());

        user.setPhoneNumber(userForm.getPhoneNumber());

        user.setProfilePic(
                "https://img.freepik.com/free-vector/blue-circle-with-white-user_78370-4707.jpg?t=st=1742800389~exp=1742803989~hmac=e822aa8e1edaa83e54c5231701e05580aaeef39628e1af4673d86e3835304a96&w=740");

        log.info("saving user");

        /*
         * This line creates a new `Message` object with a success message.
         */
        Message message = Message.builder().content("Successfully registerd").type(MessageType.green).build();

        /*
         * This line sets the `message` attribute in the HTTP session to the newly
         * created `Message` object.
         */
        httpSession.setAttribute("message", message);

        userService.saveUser(user);

        log.info("user saved successfully");
        return "redirect:/register";
    }

}
