package com.contact.project.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.contact.project.dto.ContactForm;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/user/contacts")

public class ContactController {

    Logger logger = LoggerFactory.getLogger(ContactController.class);

    @GetMapping("/add_contact")
    public String addcontactview(Model model) {

        logger.info("add contact view");

        ContactForm contactForm = new ContactForm();

        logger.info("contact form added to model");
        model.addAttribute("contactForm", contactForm);
        return new String("user/add_contact");
    }

    @PostMapping("/add_contact")
    public String processaddcontac(@ModelAttribute ContactForm contactForm) {

        

        logger.info("printing contactForm");

        logger.info(contactForm.toString());

        return "redirect:/user/contacts/add_contact";
    }

}
