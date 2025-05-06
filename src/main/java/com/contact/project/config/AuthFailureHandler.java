package com.contact.project.config;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import com.contact.project.helpers.Message;
import com.contact.project.helpers.MessageType;
import com.contact.project.services.implementation.UserServiceImpl;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Custom authentication failure handler for handling user login failures.
 * This class is triggered when an authentication attempt fails in Spring
 * Security.
 * <p>
 * If the user's account is disabled, a message is set in the session and the
 * user is redirected to the login page.
 * Otherwise, a generic login failure is handled.
 * </p>
 *
 */
@Component
public class AuthFailureHandler implements AuthenticationFailureHandler {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    /**
     * Handles authentication failures and redirects users accordingly.
     * 
     * @param request   The HTTP request object.
     * @param response  The HTTP response object.
     * @param exception The authentication exception that triggered the failure.
     * 
     * @throws IOException      If an input or output error is detected.
     * @throws ServletException If a servlet-related error occurs.
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {

        log.warn("Authentication failure detected: {}", exception.getMessage());

        if (exception instanceof DisabledException) {

            log.error("DisabledException: User account is disabled.");

            HttpSession httpSession = request.getSession();

            Message message = new Message();

            message.setContent("User account is disabled. Please contact support.");

            message.setType(MessageType.red);

            httpSession.setAttribute("message", message);

            log.info("Redirecting user to /login due to disabled account.");

            response.sendRedirect("/login");
        }

        else {

            log.info("Redirecting user to /login?error=true due to authentication failure.");

            response.sendRedirect("/login?error=true");
        }
    }

}
