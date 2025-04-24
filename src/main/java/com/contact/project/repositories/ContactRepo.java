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

/**
 * Repository for managing contacts.
 *
 * This interface provides methods for retrieving and manipulating contacts in the database.
 * 
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
         * @param name The name or part of the name to search for.
         * @param pageable The pagination and sorting information.
         * @return A paginated list of contacts with names containing the given string.
         */
        public Page<Contact> findByNameContaining(String name, Pageable pageable);


        /**
         * Finds a paginated list of contacts whose email addresses contain the given string.
         *
         * @param name The email or part of the email to search for.
         * @param pageable The pagination and sorting information.
         * @return A paginated list of contacts with email addresses containing the given string.
         */
        public Page<Contact> findByEmailContaining(String emailString, Pageable pageable);

        /**
         * Finds a paginated list of contacts whose phone numbers contain the given string.
         *
         * @param name The phone number or part of the phone number to search for.
         * @param pageable The pagination and sorting information.
         * @return A paginated list of contacts with phone numbers containing the given string.
         */
        public Page<Contact> findByPhoneNumberContaining(String phoneno, Pageable pageable);


        /**
         * Finds a paginated list of contacts associated with a given user whose names contain the
         * given string.
         *
         * @param user The {@link User} whose contacts are being searched. Must not be null.
         * @param nameString The substring to search for within the contact's name
         *        (case-insensitive).
         * @param pageable The pagination information, including page number, page size, and sorting
         *        criteria. See {@link Pageable} for details.
         * @return A {@link Page} of {@link Contact} objects associated with the specified user and
         *         whose names contain the given substring, according to the provided pagination and
         *         sorting. Returns an empty page if no matching contacts are found or if the
         *         requested page is out of bounds.
         */
        public Page<Contact> findByUserAndNameContaining(User user, String nameString,
                        Pageable pageable);

        /**
         * Finds a paginated list of contacts associated with a given user whose email addresses
         * contain the given string.
         *
         * @param user The {@link User} whose contacts are being searched. Must not be null.
         * @param emailString The substring to search for within the contact's email address
         *        (case-insensitive).
         * @param pageable The pagination information, including page number, page size, and sorting
         *        criteria. See {@link Pageable} for details.
         * @return A {@link Page} of {@link Contact} objects associated with the specified user and
         *         whose email addresses contain the given substring, according to the provided
         *         pagination and sorting. Returns an empty page if no matching contacts are found
         *         or if the requested page is out of bounds.
         */
        public Page<Contact> findByUserAndEmailContaining(User user, String emailString,
                        Pageable pageable);

        /**
         * Finds a paginated list of contacts associated with a given user whose phone numbers
         * contain the given string.
         *
         * @param user The {@link User} whose contacts are being searched. Must not be null.
         * @param phoneno The substring to search for within the contact's phone number
         *        (case-insensitive).
         * @param pageable The pagination information, including page number, page size, and sorting
         *        criteria. See {@link Pageable} for details.
         * @return A {@link Page} of {@link Contact} objects associated with the specified user and
         *         whose phone numbers contain the given substring, according to the provided
         *         pagination and sorting. Returns an empty page if no matching contacts are found
         *         or if the requested page is out of bounds.
         */
        public Page<Contact> findByUserAndPhoneNumberContaining(User user, String phoneno,
                        Pageable pageable);



        @Query("SELECT c FROM Contact c WHERE c.user.id = :userId AND c.name LIKE %:nameString%")
        Page<Contact> searchByNameLike(@Param("userId") int userId,
                        @Param("nameString") String nameString, Pageable pageable);

}
