package com.contact.project.config;

import com.contact.project.domain.Providers;
import com.contact.project.entity.User;
import com.contact.project.helpers.AppConstant;
import com.contact.project.repositories.UserRepository;
import com.contact.project.services.implementation.ContactServiceImpl;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
 * The OAuthAuthenticationSuccessHandler class is a custom implementation of the AuthenticationSuccessHandler interface in Spring Security.
 * Its primary purpose is to handle the successful authentication of a user through an OAuth 2.0 provider (e.g., Google, GitHub).
 */
@Component
@Slf4j
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final Logger log = LoggerFactory.getLogger(OAuthAuthenticationSuccessHandler.class);

    private UserRepository userRepository;

    @Autowired
    public OAuthAuthenticationSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /*
     * The `onAuthenticationSuccess` method is responsible for handling the
     * successful authentication of a user through an OAuth 2.0 provider. This
     * method is called by the Spring Security framework when the user has
     * successfully authenticated using an OAuth 2.0 provider.
     * **Method Parameters:**
     *
     * `HttpServletRequest request`: The HTTP request object that contains
     * information about the user's request.
     * `HttpServletResponse response`: The HTTP response object that will be sent
     * back to the user's browser.
     * `Authentication authentication`: The authentication object that contains
     * information about the user's authentication, including the OAuth 2.0 token.
     *
     * The method throws two types of exceptions:
     *
     * `IOException`: If an I/O error occurs while processing the request or
     * response.
     * `ServletException`: If a servlet error occurs while processing the request or
     * response.
     *
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        log.info("User logged in successfully");

        /*
         * This line extracts the `OAuth2AuthenticationToken` object from the
         * `Authentication` object. The `OAuth2AuthenticationToken` object contains
         * information about the user's authentication, including the OAuth 2.0 token.
         *
         */
        var oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

        /*
         * This line gets the authorized client registration ID from the
         * `OAuth2AuthenticationToken` object. The authorized client registration ID
         * identifies the OAuth 2.0 provider that was used for authentication.
         */
        String authorizedClientRegistrationId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();

        log.info("authorizedClientRegistrationId is : " + authorizedClientRegistrationId);

        /*
         * This line extracts the `DefaultOAuth2User` object from the `Authentication`
         * object. The `DefaultOAuth2User` object contains information about the user,
         * including their attributes.
         */
        var oauth2User = (DefaultOAuth2User) authentication.getPrincipal();

        Map<String, Object> userAttributes = oauth2User.getAttributes();

        /*
         * for (Map.Entry<String, Object> entry : userAttributes.entrySet()) {
         * log.info("key is :" + entry.getKey() + " and value is : " +
         * entry.getValue());
         * }
         */

        User user = new User();

        List<String> roleList = new ArrayList<>();

        roleList.add(AppConstant.ROLE_USER);

        user.setRoleList(roleList);

        /*
         * This line checks if the authorized client registration ID is equal to
         * "google". If it is, the code inside the `if` statement is executed.
         */
        if (authorizedClientRegistrationId.equalsIgnoreCase("google")) {

            String nameString = userAttributes.get("name").toString();

            String emailString = userAttributes.get("email").toString();

            String profilepictureString = userAttributes.get("picture").toString();

            String passwordString = "password";

            String provideruseridString = oauth2User.getName();

            String about = "This account is generated using google oauth2";

            String phonenoString = null;

            user.setUserName(nameString);

            user.setEmail(emailString);

            user.setPassword(passwordString);

            user.setProvider(Providers.GOOGLE);

            user.setProfilePic(profilepictureString);

            user.setProviderUserId(provideruseridString);

            user.setAbout(about);

            user.setPhoneNumber(phonenoString);

            user.setEnabled(true);

            user.setEmailVerified(true);

            user.setPhoneVerified(false);
        }

        /*
         * * This line checks if the authorized client registration ID is equal to
         * "github". If it is, the code inside the `else if` statement is executed.
         */
        else if (authorizedClientRegistrationId.equalsIgnoreCase("github")) {

            String username = userAttributes.get("name").toString();

            String emailString = userAttributes.get("email").toString();

            if (emailString == null) {
                String login = userAttributes.get("login").toString();
                emailString = login + "@github.com";
            }

            String profilepicString = userAttributes.get("avatar_url").toString();

            String passwordString = "password";

            String providerId = oauth2User.getName();

            String about = userAttributes.get("bio").toString();

            if (about == null) {
                about = "This account is generated using github oauth2";
            }

            String phonenoString = null;

            user.setUserName(username);

            user.setEmail(emailString);

            user.setPassword(passwordString);

            user.setAbout(about);

            user.setProfilePic(profilepicString);

            user.setPhoneNumber(phonenoString);

            user.setProvider(Providers.GITHUB);

            user.setProviderUserId(providerId);

            user.setEmailVerified(true);

        }

        /*
         * If autherized cliend registration is id not google or github then we will
         * throw error
         */
        else {
            log.error("OAuthAuthenticationSuccessHandler is unknown handler cannot register it");
        }

        /*
         * This line checks if a user with the same email already exists in the
         * database. If
         * there is no user with the same email, the code inside the `if` statement is
         * executed.this is to ensure that only new user get registerd
         */
        User user2 = userRepository.findByEmail(user.getEmail()).orElse(null);

        if (user2 == null) {
            log.info("registering user....");
            userRepository.save(user);
            log.info("user registerd successfully");
        } else {
            log.error("user is alredy registerd with given email cannot register again with same email");
        }
        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/dashboard");
    }

}
