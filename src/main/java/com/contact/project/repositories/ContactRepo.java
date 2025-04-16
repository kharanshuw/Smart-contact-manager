package com.contact.project.repositories;

import com.contact.project.entity.Contact;
import com.contact.project.entity.User;
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
    public List<Contact> findByUser(User user);

    /**
     * Finds all contacts associated with a given user ID.
     *
     * @param id the ID of the user to find contacts for
     * @return a list of contacts associated with the user
     */
    @Query("select c from Contact c where c.user.id = :id")
    public List<Contact> findByUserId(@Param("id") int id);
}
