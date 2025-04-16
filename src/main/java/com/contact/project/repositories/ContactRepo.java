package com.contact.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.contact.project.entity.Contact;

@Repository
public interface ContactRepo extends JpaRepository<Contact, Integer> {

}
