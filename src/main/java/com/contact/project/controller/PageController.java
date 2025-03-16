package com.contact.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;


@Controller
@Slf4j
public class PageController {

    @GetMapping("/about")
    public String aboutPage() {
        log.info("running aboutPage");
        return "about";
    }


    @GetMapping("/service")
    public String servicePage() {
        log.info("running servicePage");
        return new String("service");
    }
    
    
    @GetMapping("/")
    public String homePage() {
        return new String("home");
    }
    
    @GetMapping("/contact")
    public String contactPage() {
        return new String("contact");
    }


    @GetMapping("/login")
    public String loginPage() {
        return new String("login");
    }


    @GetMapping("/register")
    public String registerPage() {
        return new String("register");
    }
    
    
    

}
