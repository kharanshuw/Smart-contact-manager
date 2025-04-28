package com.contact.project.exception;


import com.contact.project.controller.ApiController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvalidContactIdException extends RuntimeException {
    
    public InvalidContactIdException(String message) {
        super(message);
    }
}
