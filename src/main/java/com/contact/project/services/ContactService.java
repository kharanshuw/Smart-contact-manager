package com.contact.project.services;

import com.contact.project.entity.Contact;
import com.contact.project.entity.User;
import org.springframework.data.domain.Page;


import java.util.List;

public interface ContactService {
    public Contact saveContact(Contact contact);

    public Contact updateContact(Contact contact);

    public List<Contact> getAllContacts();

    public Contact getContactById(int id);

    public void deleteContact(int id);

    public List<Contact> getContactsByUserId(int id);

    public Page<Contact> getByUserId(User user, int page, int size, String sortBy, String direction);

    public Page<Contact> searchContacts(String searchField, String searchValue, int page, int size, String sortBy, String direction);


}
 