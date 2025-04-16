package com.contact.project.helpers;

import com.contact.project.controller.ContactController;
import com.contact.project.services.implementation.ContactServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

/**
 * The LoggedInUserFetcher class is a utility class that provides a method for
 * fetching the email address of the currently logged-in user. The class is
 * designed to work with OAuth-based authentication systems, specifically Google
 * and GitHub.
 */

public class LoggedInUserFetcher {

    private static final Logger log = LoggerFactory.getLogger(ContactServiceImpl.class);

    /*
     * The getLoggedInUserEmail method is a static method that takes an
     * Authentication object as a parameter and returns the email address of the
     * currently logged-in user as a String. The method is designed to work with
     * OAuth-based authentication systems, specifically Google and GitHub.
     */
    public static String getLoggedInUserEmail(Authentication authentication) {

        /*
         * The method first checks if the `Authentication` object is an instance of
         * `OAuth2AuthenticationToken`. If it is, the method extracts the `OAuth2User`
         * object from the `Authentication` object and retrieves the user's attributes.
         *
         */
        if (authentication instanceof OAuth2AuthenticationToken) {

            /*
             * The method then checks the `clientId` of the `OAuth2AuthenticationToken` to
             * determine which OAuth provider is being used (Google or GitHub). Based on the
             * provider, the method extracts the email address from the user's attributes.
             *
             */
            OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

            String clientId = oAuth2AuthenticationToken.getAuthorizedClientRegistrationId();

            log.info("user is trying to log in using " + clientId);

            OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

            Map<String, Object> userAttributes = oAuth2User.getAttributes();

            String usernameString = "";

            /*
             * If the provider is Google, the method simply returns the email address from
             * the user's attributes.
             *
             */
            if (clientId.equalsIgnoreCase("google")) {

                usernameString = userAttributes.get("email").toString();

            }
            /*
             * If the provider is GitHub, the method checks if the email address is null or
             * empty. If it is, the method constructs an email address by concatenating the
             * user's login name with "@github.com".
             *
             */
            else if (clientId.equalsIgnoreCase("github")) {

                usernameString = userAttributes.get("email").toString();

                if (usernameString == null) {

                    log.error("logged in user email is empty or null");
                    String login = userAttributes.get("login").toString();
                    usernameString = login + "@github.com";
                }
            }

            return usernameString;
        }
        /*
         * If the `Authentication` object is not an instance of
         * `OAuth2AuthenticationToken`, the method logs a message indicating that it is
         * fetching the logged-in user's details from the database and returns the
         * username from the `Authentication` object.
         *
         */
        else {
            log.info("fetching logged in user details from database");
            return authentication.getName();
        }

    }
}
