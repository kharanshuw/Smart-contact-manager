package com.contact.project.services.implementation;

import com.contact.project.entity.Contact;
import com.contact.project.exception.ResouseNotFound;
import com.contact.project.repositories.ContactRepo;
import com.contact.project.services.ContactService;
import com.contact.project.services.UserService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


/**
 * Implementation of the ContactService interface.
 * This class provides methods for managing contacts, including saving, updating, deleting, and retrieving contacts.
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
    public List<Contact> getcContactsByUserId(int id) {
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

}
