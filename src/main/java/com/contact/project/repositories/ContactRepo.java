package com.contact.project.repositories;

import com.contact.project.entity.Contact;
import com.contact.project.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/*
 *  * Repository for managing contacts.
 *
 * This interface provides methods for retrieving and manipulating contacts in the database.

 */
@Repository
public interface ContactRepo extends JpaRepository<Contact, Integer> {

    /**
     * Finds all contacts associated with a given user.
     *
     * @param user the user to find contacts for
     * @return a list of contacts associated with the user
     */
    public Page<Contact> findByUser(User user, Pageable pageable);

    /**
     * Finds all contacts associated with a given user ID.
     *
     * @param id the ID of the user to find contacts for
     * @return a list of contacts associated with the user
     */
    @Query("select c from Contact c where c.user.id = :id")
    public List<Contact> findByUserId(@Param("id") int id);


    /**
     * Finds all contacts associated with a given user ID.
     *
     * @param id the ID of the user to find contacts for
     * @return a list of contacts associated with the user
     */
    @Query("select c from Contact c where c.user.id = :id")
    public Page<Contact> findByUserId(@Param("id") int id, Pageable pageable);


    /**
     * Finds a paginated list of contacts whose names contain the given string.
     *
     * @param name     The name or part of the name to search for.
     * @param pageable The pagination and sorting information.
     * @return A paginated list of contacts with names containing the given string.
     */
    public Page<Contact> findByNameContaining(String name, Pageable pageable);


    /**
     * Finds a paginated list of contacts whose email addresses contain the given string.
     *
     * @param name     The email or part of the email to search for.
     * @param pageable The pagination and sorting information.
     * @return A paginated list of contacts with email addresses containing the given string.
     */
    public Page<Contact> findByEmailContaining(String name, Pageable pageable);

    /**
     * Finds a paginated list of contacts whose phone numbers contain the given string.
     *
     * @param name     The phone number or part of the phone number to search for.
     * @param pageable The pagination and sorting information.
     * @return A paginated list of contacts with phone numbers containing the given string.
     */
    public Page<Contact> findByPhoneNumberContaining(String name, Pageable pageable);

    
    
}
