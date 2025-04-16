package com.contact.project.services;

import java.util.List;

import com.contact.project.entity.Contact;

public interface ContactService {
    public Contact saveContact(Contact contact);

    public Contact updateContact(Contact contact);

    public List<Contact> getAllContacts();

    public Contact getContactById(int id);

    public void deleteContact(int id);
}
