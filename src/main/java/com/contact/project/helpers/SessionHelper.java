package com.contact.project.helpers;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SessionHelper {
    public static void removeMessage() {
        try {
            log.info("removing message from session");

            HttpSession httpSession = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                    .getRequest().getSession();

            httpSession.removeAttribute("message");

            log.info("message removed successfully from session");
        } catch (Exception e) {
            log.error("error in removing message " + e.getMessage());
            e.printStackTrace();
        }
    }
}
