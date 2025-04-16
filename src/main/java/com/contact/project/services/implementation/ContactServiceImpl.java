package com.contact.project.services.implementation;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contact.project.entity.Contact;
import com.contact.project.exception.ResouseNotFound;
import com.contact.project.repositories.ContactRepo;
import com.contact.project.services.ContactService;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ContactServiceImpl implements ContactService {

    private ContactRepo contactRespo;

    @Autowired
    public ContactServiceImpl(ContactRepo contactRespo) {
        this.contactRespo = contactRespo;
    }

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

}
