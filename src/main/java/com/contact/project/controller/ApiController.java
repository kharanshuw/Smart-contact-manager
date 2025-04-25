package com.contact.project.controller;

import com.cloudinary.api.ApiResponse;
import com.contact.project.entity.Contact;
import com.contact.project.services.ContactService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api")
public class ApiController {

    public Logger logger = LoggerFactory.getLogger(ApiController.class);

    private ContactService contactService;


    @Autowired
    public ApiController(ContactService contactService) {
        this.contactService = contactService;
    }

    /**
     * Handles the GET request to retrieve contact details by ID.
     *
     * @param id The unique identifier of the contact to be retrieved.
     * @return A ResponseEntity containing the contact information if found,
     *         or an appropriate HTTP status in case of errors.
     * @throws InvalidContactIdException If the provided ID is less than or equal to 0.
     */
    @GetMapping("/contact/{id}")
    public ResponseEntity<Contact> getContactDetails(@PathVariable int id) {

        // Log the beginning of the process with the received contact ID
        logger.info("Processing GET request for contact ID: {}", id);

        // Validate the ID to ensure it is a positive integer
        if (id <= 0) {
            // Log the error and throw a custom exception for invalid IDs
            throw new RuntimeException("Contact ID must be positive.");
        }

        // Retrieve the contact details using the service layer
        Contact contact = contactService.getContactById(id);

        // If the contact does not exist, handle gracefully
        if (contact == null) {
            logger.error("Contact not found for ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found without body
        }

        // Return the contact details directly in the ResponseEntity
        return ResponseEntity.ok(contact);
    }

}
