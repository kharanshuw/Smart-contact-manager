package com.contact.project.controller;

import com.contact.project.dto.ContactForm;
import com.contact.project.dto.ContactSearchForm;
import com.contact.project.entity.Contact;
import com.contact.project.entity.User;
import com.contact.project.helpers.AppConstant;
import com.contact.project.helpers.LoggedInUserFetcher;
import com.contact.project.helpers.Message;
import com.contact.project.helpers.MessageType;
import com.contact.project.repositories.UserRepository;
import com.contact.project.services.ContactService;
import com.contact.project.services.ImageService;
import com.contact.project.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private UserRepository userRepository;

    @Autowired
    public ContactController(UserService userService, ContactService contactService,
            UserRepository userRepository, ImageService imageService) {
        this.userService = userService;
        this.contactService = contactService;
        this.imageService = imageService;
        this.userRepository = userRepository;
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
         * Step 1: Validate form input. If there are errors, log them, set an error message in the
         * session, and redirect back to the form view.
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


        // Step 2: Retrieve the currently logged-in user's email and log it
        logger.info("Fetching logged-in user email.");

        String username = LoggedInUserFetcher.getLoggedInUserEmail(authentication);

        logger.info("Logged-in user email: {}", username);

        User user = userService.findByEmail(username);

        logger.info("User fetched from the database: {}", user.toString());

        // Step 3: Create a new Contact entity and populate it with form data

        logger.info("Populating the Contact entity with form data.");

        Contact contact = new Contact();

        contact.setName(contactForm.getName());

        contact.setFevorite(contactForm.isFevorite());

        contact.setEmail(contactForm.getEmail());

        contact.setPhoneNumber(contactForm.getPhoneNumber());

        contact.setAddress(contactForm.getAddress());

        contact.setDescription(contactForm.getDescription());

        contact.setUser(user);

        contact.setInstagramLink(contactForm.getInstagramLink());

        contact.setFacebookLink(contactForm.getFacebookLink());

        // Step 4: Handle profile image upload (if provided in the form)

        if (contactForm.getProfileImage() != null && !contactForm.getProfileImage().isEmpty()) {

            logger.info("Profile image provided. Starting upload process.");


            String filename = UUID.randomUUID().toString();

            logger.info("Generated unique filename for profile image: {}", filename);


            String fileurl = imageService.uploadImage(contactForm.getProfileImage(), filename);

            logger.info("Profile image uploaded successfully. File URL: {}", fileurl);


            contact.setPicture(fileurl);

            contact.setCloudinaryImagename(filename);
        }

        else {
            logger.error("No profile image provided.");
        }

        // Step 5: Save the contact entity to the database

        logger.info("Contact entity populated: {}", contact);

        logger.info("Saving contact entity to the database.");


        contactService.saveContact(contact);

        logger.info("contact saved :" + contact.toString());

        // Step 6: Set a success message in the session and log it

        message.setContent("You have successfully added a new contact");

        message.setType(MessageType.green);

        httpSession.setAttribute("message", message);

        logger.info("Success message set in session. Redirecting to the add contact page.");

        return "redirect:/user/contacts/add_contact";
    }


    /**
     * Retrieves the list of contacts associated with the logged-in user. Supports pagination and
     * sorting preferences. Adds the contact list and pagination metadata to the model for rendering
     * in the view.
     *
     * @param page The page number to retrieve (0-based index). Defaults to 0 if not provided.
     * @param size The number of contacts per page. Defaults to 5 if not provided.
     * @param sortBy The field to sort the results by (e.g., "name"). Defaults to "name" if not
     *        provided.
     * @param direction The sort direction, either "asc" for ascending or "desc" for descending.
     *        Defaults to "asc".
     * @param model The {@link Model} object to which the contacts and pagination metadata will be
     *        added.
     * @param authentication The authentication object used to fetch the logged-in user's details.
     * @return The view name "user/contacts" for rendering the contact list.
     * @throws IllegalArgumentException If the user is not found or the input parameters are
     *         invalid.
     */
    @GetMapping("/view")
    public String viewContact(@RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction, Model model,
            Authentication authentication) {

        // Validate input parameters to ensure they meet the requirements for pagination
        if (page < 0 || size <= 0) {
            logger.error("Page must be >= 0 and size must be > 0.");
            throw new IllegalArgumentException("Page must be >= 0 and size must be > 0.");
        }


        // Fetch the email of the currently logged-in user using the Authentication object
        String useremail = LoggedInUserFetcher.getLoggedInUserEmail(authentication);

        logger.info("logged in user is : " + useremail);

        // Retrieve the user associated with the logged-in email from the database

        User user = userService.findByEmail(useremail);


        if (user == null) {

            // Log an error and throw an exception if the user is not found

            logger.error("User not found for email: {}", useremail);
            throw new IllegalArgumentException("User not found.");
        }


        logger.info("found user using email " + user.toString());

        // Fetch the paginated list of contacts associated with the user from the database

        logger.info("fetching contact list from database for logged in user");

        Page<Contact> contacts = contactService.getByUserId(user, page, size, sortBy, direction);

        // Handle the case where no contacts are found and add a message to the model

        if (contacts.isEmpty()) {
            logger.warn("No contacts found for user {}", user.getEmail());
            model.addAttribute("message", "No contacts found for the current search criteria.");
        }


        logger.info("successfully fetched contact list with size " + contacts.getSize());

        logger.info(
                "User {} fetched contacts: page={}, size={}, totalPages={}, hasPrevious={}, hasNext={}",
                user.getEmail(), page, size, contacts.getTotalPages(), contacts.hasPrevious(),
                contacts.hasNext());


        model.addAttribute("contact", contacts);


        model.addAttribute("totalpages", contacts.getTotalPages());


        model.addAttribute("hasPrevious", contacts.hasPrevious());


        model.addAttribute("hasnext", contacts.hasNext());

        int currentPageno = contacts.getNumber();

        model.addAttribute("pageno", currentPageno);

        model.addAttribute("contactSearchForm", new ContactSearchForm());

        return "user/contacts";

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
            @RequestParam(value = "searchField", defaultValue = "name") String searchField,
            @RequestParam(value = "searchKeyword", defaultValue = "") String searchKeyword,
            @RequestParam(value = "page", defaultValue = AppConstant.DEFAULT_PAGE + "") int page,
            @RequestParam(value = "size", defaultValue = AppConstant.DEFAULT_SIZE + "") int size,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy,
            @RequestParam(value = "direction",
                    defaultValue = AppConstant.DEFAULT_SORT_DIRECTION) String direction,
            Model model, Authentication authentication

    ) {


        // Validate searchField
        if (searchField == null || searchField.isEmpty()) {
            logger.error("Search field must not be null or empty.");
            throw new IllegalArgumentException("Search field must not be null or empty.");
        }

        // Validate searchKeyword
        if (searchKeyword == null || searchKeyword.isEmpty()) {
            logger.error("Search keyword must not be null or empty.");
            throw new IllegalArgumentException("Search keyword must not be null or empty.");
        }

        String username = LoggedInUserFetcher.getLoggedInUserEmail(authentication);

        logger.info("logged in user email is {}", username);

        User user = null;

        if (userService.isUserExistByEmail(username)) {
            logger.info("user exist by this email {}", username);
            logger.info("fetching user data ");
            user = userService.findByEmail(username);

            logger.info("successfully fetched user {}", user.toString());

        } else {
            logger.error("user does not exist with this email ");
            throw new RuntimeException("user does not exist with given username " + username);
        }


        logger.info(
                "Search request: field={}, keyword={}, page={}, size={}, sortBy={}, direction={}",
                searchField, searchKeyword, page, size, sortBy, direction);


        // Perform the search using the specified criteria and retrieve the results as a paginated
        // list
        Page<Contact> contacts = contactService.serchContactsWithUser(searchField, searchKeyword,
                page, size, sortBy, direction, user);


        logger.info("Contacts found: {}", contacts.getTotalElements());

        // If no contacts are found, add a message to the model and log a warning
        if (contacts.isEmpty()) {
            logger.error("No contacts found for keyword '{}'", searchKeyword);

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

        model.addAttribute("pagesize", AppConstant.DEFAULT_SIZE);

        model.addAttribute("searchField", searchField);

        model.addAttribute("searchKeyword", searchKeyword);

        logger.info(
                "hasprevious {} hasnext {} totalpages {} pageno {} pagesize {} contactsearchform {} ",
                hasprevious, hasNext, totalpages, currentPageno, AppConstant.DEFAULT_SIZE);

        return "user/search";
    }

}
