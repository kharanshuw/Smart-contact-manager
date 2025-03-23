package com.contact.project.exception;

public class ResouseNotFound  extends RuntimeException {
    public ResouseNotFound(String message)
    {
        super(message);
    }


    public ResouseNotFound()
    {
        super("Resouse not found");
    }
}
