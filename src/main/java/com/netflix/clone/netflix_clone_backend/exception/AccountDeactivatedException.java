package com.netflix.clone.netflix_clone_backend.exception;

public class AccountDeactivatedException extends RuntimeException{
    public AccountDeactivatedException (String message) {
        super(message);
    }
}
