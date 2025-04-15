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
     * Handles GET requests to the "/about" endpoint and returns the "about" view.
     * 
     * @return the "about" view
     */
    @GetMapping("/about")
    public String aboutPage() {
        log.info("running aboutPage");
        return "about";
    }

    /**
     * Handles GET requests to the "/service" endpoint and returns the "service"
     * view.
     * 
     * @return the "service" view
     */
    @GetMapping("/service")
    public String servicePage() {
        log.info("running servicePage");
        return new String("service");
    }

    /**
     * Handles GET requests to the "/" endpoint and returns the "home" view.
     * 
     * @return the "home" view
     */
    @GetMapping("/")
    public String homePage() {
        return new String("home");
    }

    /**
     * Handles GET requests to the "/contact" endpoint and returns the "contact"
     * view.
     * 
     * @return the "contact" view
     */
    @GetMapping("/contact")
    public String contactPage() {
        return new String("contact");
    }

    /**
     * Handles GET requests to the "/login" endpoint and returns the "login" view.
     * 
     * @return the "login" view
     */
    @GetMapping("/login")
    public String loginPage() {
        return new String("login");
    }

    /**
     * Handles GET requests to the "/register" endpoint, creates a new UserForm
     * object, and adds it to the model with the attribute name "user".
     * Returns the "register" view.
     * 
     * @param model the model
     * @return the "register" view
     */
    @GetMapping("/register")
    public String registerPage(Model model) {
        log.info("register request called");
        UserForm userForm = new UserForm();

        model.addAttribute("user", userForm);
        return new String("register");
    }

    /**
     * Handles POST requests to the "/process-Register" endpoint, validates the
     * UserForm object, creates a new User object, saves it to the database using
     * the UserService,
     * sets a success message in the HTTP session, and redirects the user to the
     * "/register" endpoint.
     * 
     * @param userForm      the UserForm object
     * @param bindingResult the binding result
     * @param httpSession   the HTTP session
     * @return the redirect URL
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

        log.info("creating new user");

        User user = new User();

        user.setUserName(userForm.getUserName());

        user.setEmail(userForm.getEmail());

        user.setPassword(userForm.getPassword());

        user.setAbout(userForm.getAbout());

        user.setPhoneNumber(userForm.getPhoneNumber());

        user.setProfilePic(
                "https://img.freepik.com/free-vector/blue-circle-with-white-user_78370-4707.jpg?t=st=1742800389~exp=1742803989~hmac=e822aa8e1edaa83e54c5231701e05580aaeef39628e1af4673d86e3835304a96&w=740");

        /*
         * This line creates a new `Message` object with a success message.
         */
        Message message = Message.builder().content("Successfully registerd").type(MessageType.green).build();

        /*
         * This line sets the `message` attribute in the HTTP session to the newly
         * created `Message` object.
         */
        httpSession.setAttribute("message", message);

        log.info("saving new user....");

        userService.saveUser(user);

        log.info("user saved successfully");
        return "redirect:/register";
    }

}
