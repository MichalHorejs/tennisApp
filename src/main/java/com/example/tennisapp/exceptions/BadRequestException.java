package com.example.tennisapp.exceptions;

/**
 * This class represents a custom exception for bad requests.
 * It extends the RuntimeException class and takes a message as a parameter.
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}