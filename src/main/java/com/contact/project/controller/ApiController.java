package com.contact.project.controller;

import com.contact.project.entity.Contact;
import com.contact.project.exception.InvalidContactIdException;
import com.contact.project.helpers.Message;
import com.contact.project.helpers.MessageType;
import com.contact.project.services.ContactService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.contact.project.dto.CsrfTokenResponse;



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
     * @return A ResponseEntity containing the contact information if found, or an appropriate HTTP
     *         status in case of errors.
     * @throws InvalidContactIdException If the provided ID is less than or equal to 0.
     */
    @GetMapping("/contact/{id}")
    public ResponseEntity<Contact> getContactDetails(@PathVariable int id) {

        // Log the beginning of the process with the received contact ID
        logger.info("Processing GET request for contact ID: {}", id);

        // Validate the ID to ensure it is a positive integer
        if (id <= 0) {
            // Log the error and throw a custom exception for invalid IDs
            logger.error("Contact ID must be positive.");
            throw new RuntimeException("Contact ID must be positive.");
        }

        // Retrieve the contact details using the service layer
        Contact contact = contactService.getContactById(id);

        // If the contact does not exist, handle gracefully
        if (contact == null) {
            logger.error("Contact not found for ID: {}", id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found without
                                                                        // body
        }

        // Return the contact details directly in the ResponseEntity
        return ResponseEntity.ok(contact);
    }

    @DeleteMapping("/contact/delete/{id}")
    public ResponseEntity<String> deleteContact(@PathVariable int id, HttpSession httpSession) {

        logger.info("Processing deleteContact GET request for contact ID: {}", id);

        // Validate the ID to ensure it is a positive integer
        if (id <= 0) {
            // Log the error and throw a custom exception for invalid IDs
            logger.error("Contact not found for ID: {}", id);
            throw new InvalidContactIdException("Contact ID must be positive.");
        }
        logger.info("deleting contact...");

        contactService.deleteContact(id);

        logger.info("deleted successfully");

        Message message = new Message();

        message.setContent("contact is deleted successfully !!");

        message.setType(MessageType.green);

        httpSession.setAttribute("message", message);

        // Return a success response
        return ResponseEntity.ok("Contact deleted successfully");
    }


    /**
     * Retrieves the CSRF token associated with the current request.
     *
     * @param request The HttpServletRequest object containing the CSRF token attribute.
     * @return A ResponseEntity containing the CSRF token details if available, or a 404 status if
     *         not.
     */
    @GetMapping("/csrf-token")
    public ResponseEntity<CsrfTokenResponse> getCsrfToken(HttpServletRequest request) {

        logger.info("Processing CSRF token request");

        CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");

        if (csrfToken == null) {
            logger.error("CSRF token not found in request");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        logger.info("csrf token : {}", csrfToken.toString());
        logger.info("CSRF token retrieved successfully");
        return ResponseEntity.ok(new CsrfTokenResponse(csrfToken));
    }

}
