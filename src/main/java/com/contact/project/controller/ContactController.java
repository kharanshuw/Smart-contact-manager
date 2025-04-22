package com.contact.project.controller;

import com.contact.project.dto.ContactForm;
import com.contact.project.entity.Contact;
import com.contact.project.entity.User;
import com.contact.project.helpers.AppConstant;
import com.contact.project.helpers.LoggedInUserFetcher;
import com.contact.project.helpers.Message;
import com.contact.project.helpers.MessageType;
import com.contact.project.services.ContactService;
import com.contact.project.services.ImageService;
import com.contact.project.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;



/**
 * Controller for managing user contacts. This class handles requests related to adding and
 * processing contacts.
 */
@Controller
@RequestMapping("/user/contacts")
public class ContactController {

    public Logger logger = LoggerFactory.getLogger(ContactController.class);

    private UserService userService;

    private ContactService contactService;

    private ImageService imageService;


    public ContactController(UserService userService, ContactService contactService,
            ImageService imageService) {
        this.userService = userService;
        this.contactService = contactService;
        this.imageService = imageService;
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
     * @param contactForm the contact form containing contact details
     * @param authentication the authentication object for the logged-in user
     * @return a redirect to the add contact view
     */
    @PostMapping("/add_contact")
    public String processaddcontact(@Valid @ModelAttribute ContactForm contactForm,
            BindingResult bindingResult, Authentication authentication, HttpSession httpSession) {

        Message message = new Message();

        /*
         * The method first checks if there are any validation errors in the form data using the
         * `bindingResult.hasErrors()` method. If there are errors, it logs an error message and
         * sets a message in the HTTP session to inform the user to correct the errors. The method
         * then returns the user to the `/add_contact` page.
         */
        if (bindingResult.hasErrors()) {

            logger.error("validation error occured in add contact form");

            List<ObjectError> errors = bindingResult.getAllErrors();

            for (ObjectError objectError : errors) {
                logger.error("printing error {}", objectError);
            }

            message.setContent("Please correct the following errors");

            message.setType(MessageType.red);

            httpSession.setAttribute("message", message);

            return "user/add_contact";
        }

        logger.info("printing file info which we got from form  "
                + contactForm.getProfileImage().getOriginalFilename());

        String uniquefilename = null;

        String fileUrl = null;


        if (contactForm.getProfileImage() != null && !contactForm.getProfileImage().isEmpty()) {


            uniquefilename = UUID.randomUUID().toString();

            logger.info("generated unique filename for contactimage " + uniquefilename);


            fileUrl = imageService.uploadImage(contactForm.getProfileImage(), uniquefilename);

            logger.info("generated file url for ProfileImage " + fileUrl);

        }


        logger.info("file url of contact image : " + fileUrl);

        Contact contact = new Contact();

        contact.setAddress(contactForm.getAddress());

        contact.setEmail(contactForm.getEmail());

        contact.setDescription(contactForm.getDescription());

        contact.setFevorite(contactForm.isFevorite());

        contact.setName(contactForm.getName());

        contact.setPhoneNumber(contactForm.getPhoneNumber());

        contact.setFacebookLink(contactForm.getFacebookLink());

        contact.setInstagramLink(contactForm.getInstagramLink());

        contact.setPicture(fileUrl);

        contact.setCloudinaryImagename(uniquefilename);

        logger.info("getting logged in user email for adding into contact");
        String email = LoggedInUserFetcher.getLoggedInUserEmail(authentication);

        logger.info("logged in user email fetched from LoggedInUserFetcher");

        User user = userService.findByEmail(email);

        contact.setUser(user);

        logger.info("printing contactForm");

        logger.info(contactForm.toString());

        contactService.saveContact(contact);

        message.setContent("you have successfully added a new contact");

        message.setType(MessageType.green);

        /*
         * Finally, the method sets a success message in the HTTP session and redirects the user to
         * the `/user/contacts/add_contact` page.
         *
         */
        httpSession.setAttribute("message", message);

        return "redirect:/user/contacts/add_contact";
    }


    @GetMapping("/view")
    public String viewContact(@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction, Model model,
            Authentication authentication) {

        String useremail = LoggedInUserFetcher.getLoggedInUserEmail(authentication);

        logger.info("logged in user is : " + useremail);

        User user = userService.findByEmail(useremail);

        logger.info("found user using email " + user.toString());

        Page<Contact> contacts = null;

        logger.info("fetching contact list from database for logged in user");

        contacts = contactService.getByUserId(user, page, size, sortBy, direction);

        logger.info("successfully fetched contact list with size " + contacts.getSize());


        model.addAttribute("contact", contacts);

        logger.info("printing totalpages" + contacts.getTotalPages());

        model.addAttribute("totalpages", contacts.getTotalPages());

        logger.info("printing hasprevious" + contacts.hasPrevious());

        model.addAttribute("hasPrevious", contacts.hasPrevious());

        logger.info("printing hasnext" + contacts.hasNext());

        model.addAttribute("hasnext", contacts.hasNext());

        int pageno = contacts.getNumber();

        logger.info("current page no" + pageno);

        model.addAttribute("pageno", pageno);


        return new String("user/contacts");
    }


    /**
     * Handles search requests for contacts based on the provided search criteria, including the
     * field to search by, search keyword, pagination, and sorting preferences. Adds the search
     * results and pagination metadata to the model.
     *
     * @param searchField The field to search by (e.g., "name", "email", "phone"). Defaults to
     *        {@link AppConstant#DEFAULT_SEARCH_FIELD} if not provided.
     * @param searchKeyword The keyword to search for within the specified field. Must not be null.
     * @param page The page number to retrieve. Defaults to {@link AppConstant#DEFAULT_PAGE}. Must
     *        be greater than or equal to 0.
     * @param size The number of records per page. Defaults to {@link AppConstant#DEFAULT_SIZE}.
     *        Must be greater than 0.
     * @param sortBy The field to sort the results by. Defaults to "name".
     * @param direction The sort direction, either "asc" for ascending or "desc" for descending.
     *        Defaults to {@link AppConstant#DEFAULT_SORT_DIRECTION}.
     * @param model The {@link Model} object to which the search results and pagination metadata
     *        will be added.
     * @return The view name "user/search" to render the search results page.
     * @throws IllegalArgumentException If the search field is invalid, page or size values are out
     *         of range, or the sort direction is neither "asc" nor "desc".
     */
    @GetMapping("/search")
    public String searchHandler(
            @RequestParam(value = "field",
                    defaultValue = AppConstant.DEFAULT_SEARCH_FIELD) String searchField,
            @RequestParam(value = "keyword") String searchKeyword,
            @RequestParam(value = "page", defaultValue = AppConstant.DEFAULT_PAGE + "") int page,
            @RequestParam(value = "size", defaultValue = AppConstant.DEFAULT_SIZE + "") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction",
                    defaultValue = AppConstant.DEFAULT_SORT_DIRECTION) String direction,
            Model model

    ) {


        logger.info(
                "Search request: field={}, keyword={}, page={}, size={}, sortBy={}, direction={}",
                searchField, searchKeyword, page, size, sortBy, direction);


        // Perform the search using the specified criteria and retrieve the results as a paginated
        // list
        Page<Contact> contacts = contactService.searchContacts(searchField, searchKeyword, page,
                size, sortBy, direction);


        logger.info("Contacts found: {}", contacts.getTotalElements());

        // If no contacts are found, add a message to the model and log a warning
        if (contacts.isEmpty()) {
            logger.warn("No contacts found for keyword '{}'", searchKeyword);

            model.addAttribute("message", "No contacts found matching your search criteria.");
        }


        // Add pagination metadata to the model
        model.addAttribute("contact", contacts);

        boolean hasprevious = contacts.hasPrevious();

        model.addAttribute("hasPrevious", hasprevious);

        boolean hasNext = contacts.hasNext();

        model.addAttribute("hasNext", hasNext);

        int totalpages = contacts.getTotalPages();

        model.addAttribute("totalpages", totalpages);

        int currentPageno = contacts.getNumber();

        model.addAttribute("pageno", currentPageno);

        return "user/search";
    }

}
