package com.routerecommendationbackend.exceptions.user;

public class WrongPasswordException extends Throwable {
    public WrongPasswordException() {
        super("Password is wrong!");
    }
}