package com.routerecommendationbackend.servicestest;

import com.routerecommendationbackend.entities.Route;
import com.routerecommendationbackend.entities.User;
import com.routerecommendationbackend.enums.Status;
import com.routerecommendationbackend.exceptions.ThrowableException;
import com.routerecommendationbackend.exceptions.route.RouteNotFoundException;
import com.routerecommendationbackend.repositories.RouteRepository;
import com.routerecommendationbackend.services.RouteService;
import com.routerecommendationbackend.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RouteServiceTest {

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private RouteService routeService;

    private Route route;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);

        route = new Route();
        route.setId(1L);
        route.setUser(user);
        route.setStatus(Status.PENDING);
        route.setEmissions_saved(10);
    }

    @Test
    void testFindById() {
        when(routeRepository.findById(1L)).thenReturn(Optional.of(route));

        Route foundRoute = routeService.findById(1L);

        assertNotNull(foundRoute);
        assertEquals(1L, foundRoute.getId());
    }

    @Test
    void testAddRoute() {
        when(routeRepository.save(route)).thenReturn(route);

        Route savedRoute = routeService.addRoute(route);

        assertNotNull(savedRoute);
        assertEquals(1L, savedRoute.getId());
    }

    @Test
    void testGetPendingRoutes() {
        List<Route> pendingRoutes = new ArrayList<>();
        pendingRoutes.add(route);
        when(routeRepository.findAllByUserIdAndStatusPending(1L)).thenReturn(pendingRoutes);

        List<Route> result = routeService.getPendingRoutes(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testGetCompletedRoutes() {
        route.setStatus(Status.COMPLETED);
        List<Route> completedRoutes = new ArrayList<>();
        completedRoutes.add(route);
        when(routeRepository.findAllByUserIdAndStatusCompleted(1L)).thenReturn(completedRoutes);

        List<Route> result = routeService.getCompletedRoutes(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testCompleteRoute() throws RouteNotFoundException, ThrowableException {
        when(routeRepository.findById(1L)).thenReturn(Optional.of(route));
        when(routeRepository.save(route)).thenReturn(route);

        Route completedRoute = routeService.completeRoute(route);

        assertNotNull(completedRoute);
        assertEquals(Status.COMPLETED, completedRoute.getStatus());
        verify(userService, times(1)).updatePoints(user.getId(), route.getEmissions_saved());
    }

    @Test
    void testCompleteRoute_NotFound() {
        when(routeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RouteNotFoundException.class, () -> {
            routeService.completeRoute(route);
        });
    }

    @Test
    void testDeclineRoute() throws RouteNotFoundException {
        when(routeRepository.findById(1L)).thenReturn(Optional.of(route));

        routeService.declineRoute(1L);

        verify(routeRepository, times(1)).delete(route);
    }

    @Test
    void testDeclineRoute_NotFound() {
        when(routeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RouteNotFoundException.class, () -> {
            routeService.declineRoute(1L);
        });
    }
}
