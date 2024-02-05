package com.routerecommendationbackend.services;

import com.routerecommendationbackend.entities.Route;
import com.routerecommendationbackend.entities.User;
import com.routerecommendationbackend.enums.Status;
import com.routerecommendationbackend.repositories.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;


    public Route findById(Long id){
        return routeRepository.findById(id).orElse(null);
    }
    public Route addRoute(Route route){
        return routeRepository.save(route);
    }

    public List<Route> getPendingRoutes(Long id){
        return routeRepository.findAllByUserIdAndStatusPending(id);
    }

    public List<Route> getCompletedRoutes(Long id){
        return routeRepository.findAllByUserIdAndStatusCompleted(id);
    }

    public Route completeRoute(Route route){
        Route completedRoute = findById(route.getId());

        if(completedRoute != null){
            completedRoute.setStatus(Status.COMPLETED);

            return routeRepository.save(completedRoute);
        }

        return null;
    }
}
