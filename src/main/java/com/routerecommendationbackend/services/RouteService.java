package com.routerecommendationbackend.services;

import com.routerecommendationbackend.entities.Route;
import com.routerecommendationbackend.repositories.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;

    public Route addRoute(Route route){
        return routeRepository.save(route);
    }


}
