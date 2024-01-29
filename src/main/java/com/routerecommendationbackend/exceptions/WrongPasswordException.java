package com.routerecommendationbackend.exceptions;

public class WrongPasswordException extends Throwable {
    public WrongPasswordException() {
        super("Password is wrong!");
    }
}