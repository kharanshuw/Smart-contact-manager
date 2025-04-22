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
import org.springframework.security.core.parameters.P;
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
    public ContactServiceImpl(ContactRepo contactRespo) {
        this.contactRespo = contactRespo;
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

            log.info("contact found can be deleted withoud problem");

            log.info("deleting contact by id in services");

            contactRespo.deleteById(id);

            log.info("successfully deleted contact by id");
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

}
