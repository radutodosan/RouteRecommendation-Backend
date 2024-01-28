package com.routerecommendationbackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends Throwable {
    public UserNotFoundException(String username) {
        super("User with name `" + username + "` was not found!");
    }
}
