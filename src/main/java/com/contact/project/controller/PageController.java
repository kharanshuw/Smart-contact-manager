package com.contact.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.extern.slf4j.Slf4j;


@Controller
@Slf4j
public class PageController {

    @GetMapping("/about")
    public String aboutPage(@RequestParam String param) {
        log.info("running aboutPage");
        return "about";
    }


    
    
    

}
