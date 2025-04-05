package com.contact.project.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.contact.project.domain.Providers;
import com.contact.project.entity.User;
import com.contact.project.helpers.AppConstant;
import com.contact.project.repositories.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private UserRepository userRepository;

    @Autowired
    public OAuthAuthenticationSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        log.info("User logged in successfully");

        var oauth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

        String authorizedClientRegistrationId = oauth2AuthenticationToken.getAuthorizedClientRegistrationId();

        log.info("authorizedClientRegistrationId is : " + authorizedClientRegistrationId);

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

        else if (authorizedClientRegistrationId.equalsIgnoreCase("github")) {

            String username = userAttributes.get("name").toString();

            String emailString = userAttributes.get("email").toString();

            if (emailString == null) {
                String login = userAttributes.get("login").toString();
                emailString = login + "@gmail.com";
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

        else {
            log.error("OAuthAuthenticationSuccessHandler is unknown handler cannot register it");
        }

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
