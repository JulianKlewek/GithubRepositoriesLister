package com.example.infrastructure.controller.error;

public class NotExistingGithubUserException extends RuntimeException {

    public NotExistingGithubUserException(String message) {
        super(message);
    }
}
