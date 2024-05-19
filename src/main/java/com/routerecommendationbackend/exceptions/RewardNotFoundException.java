package com.routerecommendationbackend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RewardNotFoundException extends Throwable {
    public RewardNotFoundException(String username, LocalDate date) {
        super("Reward for " + username + " on date: " + date + " not found!");
    }
}
