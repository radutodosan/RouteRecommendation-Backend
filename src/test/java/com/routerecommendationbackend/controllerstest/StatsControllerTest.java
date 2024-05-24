package com.routerecommendationbackend.controllerstest;

import com.routerecommendationbackend.controllers.StatsController;
import com.routerecommendationbackend.entities.Route;
import com.routerecommendationbackend.enums.Transport;
import com.routerecommendationbackend.services.StatsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StatsController.class)
class StatsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StatsService statsService;

    private Route route;
    private List<Route> routeList;
    private Map<Integer, List<Route>> routesByMonth;
    private Map<Integer, Integer> routesPerMonth;
    private Map<Integer, Double> kmCompletedPerMonth;
    private Map<Integer, Integer> emissionsSavedPerMonth;
    private Map<Integer, Integer> calBurnedPerMonth;
    private Map<Integer, Double> moneySavedPerMonth;
    private Map<Transport, Integer> transportPercentage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        route = new Route();
        route.setId(1L);
        route.setDate(LocalDate.now());
        route.setDistance(10.0);
        route.setEmissions_saved(5);
        route.setCal_burned(100);
        route.setTransport(Transport.Walk);

        routeList = new ArrayList<>();
        routeList.add(route);

        routesByMonth = new HashMap<>();
        routesByMonth.put(LocalDate.now().getMonthValue(), routeList);

        routesPerMonth = new HashMap<>();
        routesPerMonth.put(LocalDate.now().getMonthValue(), 1);

        kmCompletedPerMonth = new HashMap<>();
        kmCompletedPerMonth.put(LocalDate.now().getMonthValue(), 10.0);

        emissionsSavedPerMonth = new HashMap<>();
        emissionsSavedPerMonth.put(LocalDate.now().getMonthValue(), 5);

        calBurnedPerMonth = new HashMap<>();
        calBurnedPerMonth.put(LocalDate.now().getMonthValue(), 100);

        moneySavedPerMonth = new HashMap<>();
        moneySavedPerMonth.put(LocalDate.now().getMonthValue(), 10.0 * 0.55);

        transportPercentage = new HashMap<>();
        transportPercentage.put(Transport.Walk, 1);
    }

    @Test
    void testGetRoutesByMonth() throws Exception {
        when(statsService.getRoutesByMonth(1L)).thenReturn(routesByMonth);

        mockMvc.perform(get("/statistics/routes-per-month/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$." + LocalDate.now().getMonthValue()).isArray())
                .andExpect(jsonPath("$." + LocalDate.now().getMonthValue() + "[0].id").value(1L));
    }

    @Test
    void testGetNrOfRoutesPerMonth() throws Exception {
        when(statsService.getRoutesPerMonth(1L)).thenReturn(routesPerMonth);

        mockMvc.perform(get("/statistics/routes-month/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$." + LocalDate.now().getMonthValue()).value(1));
    }

    @Test
    void testGetKmCompletedPerMonth() throws Exception {
        when(statsService.getKmCompletedPerMonth(1L)).thenReturn(kmCompletedPerMonth);

        mockMvc.perform(get("/statistics/km-month/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$." + LocalDate.now().getMonthValue()).value(10.0));
    }

    @Test
    void testGetEmissionsSavedPerMonth() throws Exception {
        when(statsService.getEmissionsSavedPerMonth(1L)).thenReturn(emissionsSavedPerMonth);

        mockMvc.perform(get("/statistics/emissions-saved-month/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$." + LocalDate.now().getMonthValue()).value(5));
    }

    @Test
    void testGetCalBurnedPerMonth() throws Exception {
        when(statsService.getCalBurnedPerMonth(1L)).thenReturn(calBurnedPerMonth);

        mockMvc.perform(get("/statistics/cal-burned-month/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$." + LocalDate.now().getMonthValue()).value(100));
    }

    @Test
    void testGetMoneySavedPerMonth() throws Exception {
        when(statsService.getMoneySavedPerMonth(1L)).thenReturn(moneySavedPerMonth);

        mockMvc.perform(get("/statistics/money-saved-month/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$." + LocalDate.now().getMonthValue()).value(10.0 * 0.55));
    }

    @Test
    void testGetTransportPercentage() throws Exception {
        when(statsService.getTransportPercentage(1L)).thenReturn(transportPercentage);

        mockMvc.perform(get("/statistics/transport-percentage/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Walk").value(1));
    }
}
