package com.contact.project.exception.global_exception_handler;


import com.contact.project.exception.InvalidContactIdException;
import com.contact.project.exception.ResouseNotFound;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestGlobalExceptionHandler {
    @ExceptionHandler(InvalidContactIdException.class)
    public ResponseEntity<String> handleInvalidContactIdException(InvalidContactIdException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
    }

    @ExceptionHandler(ResouseNotFound.class)
    public ResponseEntity<String> handleGenericException(ResouseNotFound resouseNotFound) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(resouseNotFound.getMessage());
    }
    
}
