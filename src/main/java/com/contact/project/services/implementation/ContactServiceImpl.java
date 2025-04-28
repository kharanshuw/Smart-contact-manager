package com.contact.project.services.implementation;

import com.contact.project.entity.Contact;
import com.contact.project.entity.User;
import com.contact.project.exception.ResouseNotFound;
import com.contact.project.repositories.ContactRepo;
import com.contact.project.services.ContactService;
import com.contact.project.services.UserService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * Implementation of the ContactService interface. This class provides methods for managing
 * contacts, including saving, updating, deleting, and retrieving contacts.
 */
@Service
public class ContactServiceImpl implements ContactService {

    private static final Logger log = LoggerFactory.getLogger(ContactServiceImpl.class);

    private ContactRepo contactRespo;

    private UserService userService;

    @Autowired
    public ContactServiceImpl(ContactRepo contactRespo, UserService userService) {
        this.contactRespo = contactRespo;
        this.userService = userService;
    }

    /**
     * Saves a contact to the database.
     *
     * @param contact the contact to save
     * @return the saved contact
     */
    @Override
    @Transactional
    public Contact saveContact(Contact contact) {
        try {
            log.info("saving contact in services " + contact.toString());
            Contact contact2 = contactRespo.save(contact);
            log.info("successfully saved contact");
            return contact2;
        } catch (Exception e) {
            // TODO: handle exception
            log.error("error in saving contact " + e.toString());
            throw new RuntimeException("error in saving contact " + e.toString());
        }
    }


    /**
     * Updates an existing contact.
     *
     * @param contact the contact to update
     * @return the updated contact
     * @throws ResouseNotFound if no contact is found with the given ID
     */
    @Override
    @Transactional
    public Contact updateContact(Contact contact) {

        try {
            log.info("serching contact by id");

            Contact contact2 = contactRespo.findById(contact.getId()).orElseThrow(() -> {
                return new ResouseNotFound("contact not found with this id" + contact.getId());
            });

            return null;
        } catch (Exception e) {
            log.error("error in saving contact " + e.toString());
            throw new RuntimeException("error in saving contact " + e.toString());

        }

    }

    /**
     * Retrieves all contacts from the database.
     *
     * @return a list of all contacts
     */
    @Override
    public List<Contact> getAllContacts() {
        try {
            log.info("getting all contact in services");

            return contactRespo.findAll();
        } catch (Exception e) {
            log.error("error in getAllContacts contact " + e.toString());
            throw new RuntimeException("error in getAllContacts contact " + e.toString());

        }
    }

    /**
     * Retrieves a contact by its ID.
     *
     * @param id the ID of the contact to retrieve
     * @return the contact with the given ID
     * @throws RuntimeException if no contact is found with the given ID
     */
    @Override
    public Contact getContactById(int id) {
        try {
            log.info("getting contact by id in services");

            Optional<Contact> optionalContact = contactRespo.findById(id);

            if (optionalContact.isPresent()) {
                log.info("contact is present with given id");

                return optionalContact.get();
            } else {
                log.info("contact is not present with given id" + id);
                throw new RuntimeException("contact with given id is not present");
            }
        } catch (Exception e) {
            log.error("error in getContactById contact " + e.toString());
            throw new RuntimeException("error in getContactById contact " + e.toString());
        }
    }


    /**
     * Deletes a contact by its ID.
     *
     * @param id the ID of the contact to delete
     * @throws ResouseNotFound if no contact is found with the given ID
     */
    @Override
    @Transactional
    public void deleteContact(int id) {

        try {

            log.info("finding contact before deleting");

            Contact contact = contactRespo.findById(id).orElseThrow(() -> {
                return new ResouseNotFound("contact not found with this id" + id);
            });

            log.info("contact found can be deleted without problem");

            log.info("deleting contact by id {} in services", id);

            contactRespo.deleteContactById(id);

            log.info("successfully deleted contact by id:{}", id);
        } catch (Exception e) {
            log.error("error in deleting contact " + e.toString());
            throw new RuntimeException("error in deleting contact " + e.toString());
        }
    }


    /**
     * Retrieves all contacts associated with a given user ID.
     *
     * @param id the ID of the user whose contacts are to be retrieved
     * @return a list of contacts associated with the given user ID
     */
    @Override
    public List<Contact> getContactsByUserId(int id) {
        try {
            log.info("getcContactsByUserId in services");

            List<Contact> contacts = contactRespo.findByUserId(id);

            log.info("list of contact assosiated with given user id found ");

            return contacts;
        } catch (Exception e) {
            // TODO: handle exception

            log.error("error in getcContactsByUserId  " + e.toString());
            throw new RuntimeException("error in getcContactsByUserId " + e.toString());

        }
    }

    /**
     * Retrieves a paginated list of contacts associated with a specific user.
     *
     * @param user The user whose contacts are to be retrieved.
     * @param page The page number to retrieve.
     * @param size The number of contacts per page.
     * @param sortBy The field by which the results should be sorted (e.g., name, email).
     * @param direction The sort direction, either "asc" for ascending or "desc" for descending.
     * @return A paginated list of contacts associated with the specified user.
     */
    public Page<Contact> getByUserId(User user, int page, int size, String sortBy,
            String direction) {

        log.info("page no is " + page);

        log.info("page size is " + size);

        log.info("page sort by " + sortBy);

        log.info("direction " + direction);


        Sort sort = direction.equals("desc") ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        log.info("fetching list of contact assosiated with user " + user.getId());

        Page<Contact> page1 = contactRespo.findByUserId(user.getId(), pageable);

        log.info("successfully fetched list of contact assosiated with user " + user.getId());

        return page1;
    }

    /**
     * Searches for contacts based on a specified field and value, with support for pagination and
     * sorting.
     *
     * @param searchField The field to search by (e.g., "name", "email", "phone").
     * @param searchValue The value to search for within the specified field.
     * @param page The page number to retrieve (0-based index).
     * @param size The number of records to retrieve per page.
     * @param sortBy The field by which the results should be sorted (e.g., "name", "email").
     * @param direction The sort direction, either "asc" for ascending or "desc" for descending.
     * @return A paginated {@link Page} of {@link Contact} objects matching the search criteria. If
     *         no results are found, an empty page is returned.
     * @throws IllegalArgumentException If the provided search field, page, size, or direction is
     *         invalid.
     * @throws RuntimeException If an unexpected error occurs during the search.
     */
    public Page<Contact> searchContacts(String searchField, String searchValue, int page, int size,
            String sortBy, String direction) {
        try {
            log.info(
                    "Page: {}, Size: {}, SortBy: {}, Direction: {}, SearchField: {}, SearchValue: {}",
                    page, size, sortBy, direction, searchField, searchValue);

            if (searchField == null || searchField.isEmpty()) {
                log.error("Search field must not be null or empty.");
                throw new IllegalArgumentException("Search field must not be null or empty.");
            }


            if (page < 0 || size <= 0) {
                log.error("Page number must be >= 0 and size must be > 0.");
                throw new IllegalArgumentException(
                        "Page number must be >= 0 and size must be > 0.");
            }

            if (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc")) {
                log.error("Sort direction must be 'asc' or 'desc'.");
                throw new IllegalArgumentException("Sort direction must be 'asc' or 'desc'.");
            }

            Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();


            Pageable pageable = PageRequest.of(page, size, sort);

            Page<Contact> page1 = null;

            switch (searchField) {
                case "name":
                    page1 = contactRespo.findByNameContaining(searchValue, pageable);
                    break;
                case "email":
                    page1 = contactRespo.findByEmailContaining(searchValue, pageable);
                    break;
                case "phoneNumber":
                    page1 = contactRespo.findByPhoneNumberContaining(searchValue, pageable);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid search field: " + searchField);
            }

            if (page1.isEmpty()) {
                log.warn("No contacts found for search field: {} and value: {}", searchField,
                        searchValue);
                return page1;
            } else {

                return page1;
            }

        } catch (IllegalArgumentException e) {
            log.error("illegal argument error {}", e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.error("exception occured {}", e);
            throw new RuntimeException(e);
        }

    }

    /**
     * Searches for contacts associated with a specific user based on a given search field and
     * value, with pagination and sorting capabilities.
     *
     * @param searchField The field to search within. Supported values are "name", "email", and
     *        "phoneNumber". Must not be null or empty.
     * @param searchValue The substring to search for within the specified {@code searchField}.
     * @param page The page number to retrieve (0-based index). Must be greater than or equal to 0.
     * @param size The number of contacts to retrieve per page. Must be greater than 0.
     * @param sortBy The field to sort the results by (e.g., "name", "email", "phoneNumber"). Ensure
     *        this field corresponds to a property in the {@link Contact} entity.
     * @param direction The sort direction. Must be either "asc" (ascending) or "desc" (descending),
     *        case-insensitive.
     * @param user The {@link User} object whose contacts are being searched. Must not be null.
     * @return A Page of Contact objects that match the search criteria for the given user, ordered
     *         and paginated according to the provided parameters. Returns an empty page if no
     *         matching contacts are found.
     * @throws IllegalArgumentException If the {@code searchField} is null or empty, or if the
     *         {@code page} is less than 0, or if the {@code size} is less than or equal to 0, or if
     *         the {@code direction} is not "asc" or "desc", or if an invalid {@code searchField} is
     *         provided.
     * @throws RuntimeException If any other unexpected error occurs during the search operation.
     *
     * @see Contact
     * @see User
     * @see Page
     * @see Pageable
     * @see Sort
     */
    public Page<Contact> serchContactsWithUser(String searchField, String searchValue, int page,
            int size, String sortBy, String direction, User user) {
        try {
            log.info(
                    "Page: {}, Size: {}, SortBy: {}, Direction: {}, SearchField: {}, SearchValue: {}",
                    page, size, sortBy, direction, searchField, searchValue);

            // Validate that the search field is not null or empty.

            if (searchField == null || searchField.isEmpty()) {
                log.error("Search field must not be null or empty.");
                throw new IllegalArgumentException("Search field must not be null or empty.");
            }


            // Validate that the page number is not negative and the page size is positive.

            if (page < 0 || size <= 0) {
                log.error("Page number must be >= 0 and size must be > 0.");
                throw new IllegalArgumentException(
                        "Page number must be >= 0 and size must be > 0.");
            }

            // Validate that the sort direction is either "asc" or "desc" (case-insensitive).

            if (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc")) {
                log.error("Sort direction must be 'asc' or 'desc'.");
                throw new IllegalArgumentException("Sort direction must be 'asc' or 'desc'.");
            }

            // Determine the sort order based on the provided direction.

            Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending()
                    : Sort.by(sortBy).ascending();


            // Create a Pageable object to handle pagination and sorting.

            Pageable pageable = PageRequest.of(page, size, sort);


            // Declare a variable to hold the Page of Contact objects.

            Page<Contact> page1 = null;

            // Perform the search based on the specified search field.

            switch (searchField) {

                // Search contacts by name containing the search value for the given user.

                case "name":
                    log.info("serching contact by user");

                    page1 = contactRespo.searchByNameLike(user.getId(), searchValue, pageable);
                    break;

                // Search contacts by email containing the search value for the given user.
                case "email":


                    log.info("serching contact by user");
                    page1 = contactRespo.findByUserAndEmailContaining(user, searchValue, pageable);
                    break;


                // Search contacts by phone number containing the search value for the given user.

                case "phoneNumber":

                    log.info("serching contact by user");
                    page1 = contactRespo.findByUserAndPhoneNumberContaining(user, searchValue,
                            pageable);
                    break;


                default:

                    // If an invalid search field is provided, throw an IllegalArgumentException.

                    throw new IllegalArgumentException("Invalid search field: " + searchField);
            }

            // Check if any contacts were found for the given search criteria.
            if (page1.isEmpty()) {


                log.warn("No contacts found for search field: {} and value: {}", searchField,
                        searchValue);
                // Return the empty page.
                return page1;
            } else {
                // Return the page of found contacts.
                return page1;
            }

        } catch (IllegalArgumentException e) {
            log.error("illegal argument error {}", e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.error("exception occured {}", e);
            throw new RuntimeException(e);
        }

    }

}
