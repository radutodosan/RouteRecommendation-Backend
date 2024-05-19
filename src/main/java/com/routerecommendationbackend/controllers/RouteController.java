package com.routerecommendationbackend.controllers;

import com.routerecommendationbackend.entities.Route;
import com.routerecommendationbackend.exceptions.ThrowableException;
import com.routerecommendationbackend.exceptions.route.RouteNotFoundException;
import com.routerecommendationbackend.services.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@CrossOrigin("*")
public class RouteController {

    private final RouteService routeService;

    @PostMapping("map")
    private ResponseEntity<Route> addRoute(@RequestBody Route route){
        return ResponseEntity.ok(routeService.addRoute(route));
    }

    @GetMapping("notifications/routes")
    private ResponseEntity<List<Route>> getPendingRoutes(@RequestParam Long id){
        return ResponseEntity.ok(routeService.getPendingRoutes(id));
    }

    @GetMapping("statistics/routes")
    private ResponseEntity<List<Route>> getCompletedRoutes(@RequestParam Long id){
        return ResponseEntity.ok(routeService.getCompletedRoutes(id));
    }

    @PutMapping("notifications/route")
    private ResponseEntity<Route> completeRoute(@RequestBody Route route) throws RouteNotFoundException, ThrowableException {
        return ResponseEntity.ok(routeService.completeRoute(route));
    }

    @DeleteMapping("notifications/route/{id}")
    private void declineRoute(@PathVariable Long id) throws RouteNotFoundException {
        routeService.declineRoute(id);
    }

}
