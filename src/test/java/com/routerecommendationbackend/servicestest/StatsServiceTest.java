package com.routerecommendationbackend.servicestest;

import com.routerecommendationbackend.entities.Route;
import com.routerecommendationbackend.enums.Transport;
import com.routerecommendationbackend.exceptions.stats.StatsException;
import com.routerecommendationbackend.repositories.StatsRepository;
import com.routerecommendationbackend.services.StatsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StatsServiceTest {

    @Mock
    private StatsRepository statsRepository;

    @InjectMocks
    private StatsService statsService;

    private Route route;
    private List<Route> routeList;

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
    }

    @Test
    void testGetRoutesByMonth() throws StatsException {
        when(statsRepository.getRoutesByUserId(1L)).thenReturn(routeList);

        Map<Integer, List<Route>> result = statsService.getRoutesByMonth(1L);

        assertNotNull(result);
        assertTrue(result.containsKey(LocalDate.now().getMonthValue()));
        assertEquals(1, result.get(LocalDate.now().getMonthValue()).size());
    }

    @Test
    void testGetRoutesPerMonth() throws StatsException {
        when(statsRepository.getRoutesByUserId(1L)).thenReturn(routeList);

        Map<Integer, Integer> result = statsService.getRoutesPerMonth(1L);

        assertNotNull(result);
        assertEquals(1, result.get(LocalDate.now().getMonthValue()));
    }

    @Test
    void testGetKmCompletedPerMonth() throws StatsException {
        when(statsRepository.getRoutesByUserId(1L)).thenReturn(routeList);

        Map<Integer, Double> result = statsService.getKmCompletedPerMonth(1L);

        assertNotNull(result);
        assertEquals(10.0, result.get(LocalDate.now().getMonthValue()));
    }

    @Test
    void testGetEmissionsSavedPerMonth() throws StatsException {
        when(statsRepository.getRoutesByUserId(1L)).thenReturn(routeList);

        Map<Integer, Integer> result = statsService.getEmissionsSavedPerMonth(1L);

        assertNotNull(result);
        assertEquals(5, result.get(LocalDate.now().getMonthValue()));
    }

    @Test
    void testGetCalBurnedPerMonth() throws StatsException {
        when(statsRepository.getRoutesByUserId(1L)).thenReturn(routeList);

        Map<Integer, Integer> result = statsService.getCalBurnedPerMonth(1L);

        assertNotNull(result);
        assertEquals(100, result.get(LocalDate.now().getMonthValue()));
    }

    @Test
    void testGetMoneySavedPerMonth() throws StatsException {
        when(statsRepository.getRoutesByUserId(1L)).thenReturn(routeList);

        Map<Integer, Double> result = statsService.getMoneySavedPerMonth(1L);

        assertNotNull(result);
        assertEquals(10.0 * 0.55, result.get(LocalDate.now().getMonthValue()));
    }

    @Test
    void testGetTransportPercentage() throws StatsException {
        when(statsRepository.getRoutesByUserId(1L)).thenReturn(routeList);

        Map<Transport, Integer> result = statsService.getTransportPercentage(1L);

        assertNotNull(result);
        assertEquals(1, result.get(Transport.Walk));
    }
}
