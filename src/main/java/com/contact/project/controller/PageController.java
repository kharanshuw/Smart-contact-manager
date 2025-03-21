package com.contact.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.contact.project.dto.UserForm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
@Slf4j
public class PageController {


    /**
     * This request mapping is for about page
     * @return
     */
    @GetMapping("/about")
    public String aboutPage() {
        log.info("running aboutPage");
        return "about";
    }


    /**
     * This request mapping is for service page
     * @return
     */

    @GetMapping("/service")
    public String servicePage() {
        log.info("running servicePage");
        return new String("service");
    }
    
    
    /**
     * This request mapping is for home page
     * @return
     */
    @GetMapping("/")
    public String homePage() {
        return new String("home");
    }
    
    /**
     * This request mapping is for contact page
     * @return
     */
    @GetMapping("/contact")
    public String contactPage() {
        return new String("contact");
    }


    /**
     * This request mapping is for login page
     * @return
     */
    @GetMapping("/login")
    public String loginPage() {
        return new String("login");
    }


    /**
     * This request mapping is for register page
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
     * @return
     */
    @PostMapping("/do-register")
    public String postMethodName(@ModelAttribute("user") UserForm userForm ) {
        
        log.info("register started");


        log.info("printing userform "+ userForm.toString());

        return "register";
    }
    

}
