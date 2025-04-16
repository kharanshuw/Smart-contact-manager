package com.contact.project.controller;

import com.contact.project.dto.ContactForm;
import com.contact.project.entity.Contact;
import com.contact.project.entity.User;
import com.contact.project.helpers.LoggedInUserFetcher;
import com.contact.project.services.ContactService;
import com.contact.project.services.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for managing user contacts.
 * This class handles requests related to adding and processing contacts.
 */
@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    public Logger logger = LoggerFactory.getLogger(ContactController.class);
    private UserService userService;

    private ContactService contactService;

    @Autowired
    public ContactController(UserService userService, ContactService contactService) {
        this.userService = userService;
        this.contactService = contactService;

    }

    /**
     * Displays the add contact view.
     *
     * @param model the model to which the contact form is added
     * @return the view name for adding a contact
     */
    @GetMapping("/add_contact")
    public String addcontactview(Model model) {

        logger.info("add contact view");

        ContactForm contactForm = new ContactForm();

        logger.info("contact form added to model");
        model.addAttribute("contactForm", contactForm);
        return new String("user/add_contact");
    }

    /**
     * Processes the addition of a new contact.
     *
     * @param contactForm    the contact form containing contact details
     * @param authentication the authentication object for the logged-in user
     * @return a redirect to the add contact view
     */
    @PostMapping("/add_contact")
    public String processaddcontact(@Valid @ModelAttribute ContactForm contactForm, BindingResult bindingResult,
            Authentication authentication) {

        if (bindingResult.hasErrors()) {
            logger.error("validation error occured in add contact form");
            return "/user/contacts/add_contact";
        }

        Contact contact = new Contact();

        contact.setAddress(contactForm.getAddress());

        contact.setEmail(contactForm.getEmail());

        contact.setDescription(contactForm.getDescription());

        contact.setFevorite(contactForm.isFevorite());

        contact.setName(contactForm.getName());

        contact.setPhoneNumber(contactForm.getPhoneNumber());

        contact.setFacebookLink(contactForm.getFacebookLink());

        contact.setInstagramLink(contactForm.getInstagramLink());

        logger.info("getting logged in user email for adding into contact");
        String email = LoggedInUserFetcher.getLoggedInUserEmail(authentication);

        logger.info("logged in user email fetched from LoggedInUserFetcher");

        User user = userService.findByEmail(email);

        contact.setUser(user);

        logger.info("printing contactForm");

        logger.info(contactForm.toString());

        contactService.saveContact(contact);

        return "redirect:/user/contacts/add_contact";
    }

}
