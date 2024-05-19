package com.routerecommendationbackend.exceptions.route;

public class RouteNotFoundException extends Throwable {
    public RouteNotFoundException(Long id) {
        super("Route with id: " + id + " was not found!");
    }
}