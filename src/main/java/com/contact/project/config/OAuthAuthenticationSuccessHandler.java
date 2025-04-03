package com.contact.project.config;

import java.io.IOException;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OAuthAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        log.info("User logged in successfully");

        var user = (DefaultOAuth2User) authentication.getPrincipal();

        log.info("user name is : " + user.getName());

        Map<String, Object> userAttributes = user.getAttributes();

        // for (Map.Entry<String, Object> entry : usersAttributes.entrySet()) {
        // log.info("key is :" + entry.getKey() + " and value is : " +
        // entry.getValue());
        // }

        Object nameObject = userAttributes.getOrDefault("name", null);

        String nameString = nameObject.toString();

        log.info("name is : " + nameString);

        Object emailObject = userAttributes.getOrDefault("email", null);

        String emailString = emailObject.toString();

        log.info("email is" + emailString);

        Object pictureObject = userAttributes.getOrDefault("picture", null);

        String picture = pictureObject.toString();

        log.info("picture link " + picture);

        // log.info("printing authorities "+user.getAuthorities().toString());

        new DefaultRedirectStrategy().sendRedirect(request, response, "/user/dashboard");
    }

}
