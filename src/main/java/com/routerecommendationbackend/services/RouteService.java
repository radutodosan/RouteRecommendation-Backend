package com.routerecommendationbackend.services;

import com.routerecommendationbackend.entities.Route;
import com.routerecommendationbackend.enums.Status;
import com.routerecommendationbackend.exceptions.ThrowableException;
import com.routerecommendationbackend.exceptions.route.RouteNotFoundException;
import com.routerecommendationbackend.repositories.RouteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;
    private final UserService userService;


    public Route findById(Long id) {
        return routeRepository.findById(id).orElse(null);
    }

    public Route addRoute(Route route) {

        return routeRepository.save(route);
    }

    public List<Route> getPendingRoutes(Long id) {
        return routeRepository.findAllByUserIdAndStatusPending(id);
    }

    public List<Route> getCompletedRoutes(Long id) {
        return routeRepository.findAllByUserIdAndStatusCompleted(id);
    }

    public Route completeRoute(Route route) throws RouteNotFoundException, ThrowableException {
        Route completedRoute = findById(route.getId());

        if (completedRoute != null) {
            completedRoute.setStatus(Status.COMPLETED);

            userService.updatePoints(completedRoute.getUser().getId(), completedRoute.getEmissions_saved());

            return routeRepository.save(completedRoute);
        }
        else
            throw new RouteNotFoundException(route.getId());
    }

    public void declineRoute(Long id) throws RouteNotFoundException {
        Route declinedRoute = findById(id);

        if (declinedRoute != null)
            routeRepository.delete(declinedRoute);
        else
            throw new RouteNotFoundException(id);
    }
}

