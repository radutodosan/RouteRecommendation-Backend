package com.routerecommendationbackend.controllerstest;

import com.routerecommendationbackend.controllers.RouteController;
import com.routerecommendationbackend.entities.Route;
import com.routerecommendationbackend.entities.User;
import com.routerecommendationbackend.enums.Status;
import com.routerecommendationbackend.exceptions.ThrowableException;
import com.routerecommendationbackend.exceptions.route.RouteNotFoundException;
import com.routerecommendationbackend.services.RouteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RouteController.class)
class RouteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
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
    void testAddRoute() throws Exception {
        when(routeService.addRoute(any(Route.class))).thenReturn(route);

        mockMvc.perform(post("/map")
                        .contentType("application/json")
                        .content("{\"id\": 1, \"user\": {\"id\": 1}, \"status\": \"PENDING\", \"emissions_saved\": 10}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void testGetPendingRoutes() throws Exception {
        List<Route> pendingRoutes = new ArrayList<>();
        pendingRoutes.add(route);
        when(routeService.getPendingRoutes(1L)).thenReturn(pendingRoutes);

        mockMvc.perform(get("/notifications/routes")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void testGetCompletedRoutes() throws Exception {
        route.setStatus(Status.COMPLETED);
        List<Route> completedRoutes = new ArrayList<>();
        completedRoutes.add(route);
        when(routeService.getCompletedRoutes(1L)).thenReturn(completedRoutes);

        mockMvc.perform(get("/statistics/routes")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L));
    }

    @Test
    void testCompleteRoute() throws Exception, ThrowableException, RouteNotFoundException {
        route.setStatus(Status.COMPLETED);
        when(routeService.
                completeRoute(any(Route.class))).thenReturn(route);

        mockMvc.perform(put("/notifications/route")
                        .contentType("application/json")
                        .content("{\"id\": 1, \"user\": {\"id\": 1}, \"status\": \"PENDING\", \"emissions_saved\": 10}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void testDeclineRoute() throws Exception, RouteNotFoundException {
        mockMvc.perform(delete("/notifications/route/1"))
                .andExpect(status().isOk());

        verify(routeService, times(1)).declineRoute(1L);
    }
}
