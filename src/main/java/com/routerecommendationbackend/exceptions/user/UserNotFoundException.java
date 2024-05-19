package com.routerecommendationbackend.exceptions.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends Throwable {
    public UserNotFoundException(String username) {
        super("User with username `" + username + "` was not found!");
    }
}
