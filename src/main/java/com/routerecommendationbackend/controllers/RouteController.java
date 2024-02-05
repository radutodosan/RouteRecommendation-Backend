package com.routerecommendationbackend.controllers;

import com.routerecommendationbackend.entities.Route;
import com.routerecommendationbackend.services.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@CrossOrigin("*")
public class RouteController {

    private final RouteService routeService;

    private ResponseEntity<Route> addRoute(Route route){
        return ResponseEntity.ok(routeService.addRoute(route));
    }
}
