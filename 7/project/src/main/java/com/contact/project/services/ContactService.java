package com.contact.project.services;

import com.contact.project.entity.Contact;

import java.util.List;

public interface ContactService {
    public Contact saveContact(Contact contact);

    public Contact updateContact(Contact contact);

    public List<Contact> getAllContacts();

    public Contact getContactById(int id);

    public void deleteContact(int id);

    public List<Contact> getcContactsByUserId(int id);
}
