package com.contact.project.helpers;

import com.contact.project.services.implementation.ContactServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component

public class SessionHelper {

    private static final Logger log = LoggerFactory.getLogger(ContactServiceImpl.class);

    /**
     * Removes the "message" attribute from the current HTTP session.
     * <p>
     * This method is used to clear any previously stored message from the session.
     *
     * @throws Exception if an error occurs while removing the message from the
     *                   session
     */
    public static void removeMessage() {
        try {
            log.info("removing message from session");
            /**
             * This line gets the current HTTP session (a way to store data for a user's
             * session) from the request attributes. It's a bit complicated, but basically,
             * it's getting the session object that's associated with the current request.
             */
            HttpSession httpSession = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getRequest().getSession();

            /**
             * removes an attribute (a piece of data) called "message" from the session
             * object. This is the main purpose of the removeMessage method!
             */
            httpSession.removeAttribute("message");

            log.info("message removed successfully from session");
        } catch (Exception e) {
            log.error("error in removing message " + e.getMessage());
            e.printStackTrace();
        }
    }
}
