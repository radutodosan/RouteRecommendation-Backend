package com.routerecommendationbackend.exceptions;

public class FriendRequestExistsException extends RuntimeException{
    public FriendRequestExistsException(String message) {
        super(message);
    }
}
