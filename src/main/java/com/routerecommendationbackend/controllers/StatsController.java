package com.routerecommendationbackend.controllers;

import com.routerecommendationbackend.entities.Route;
import com.routerecommendationbackend.enums.Transport;
import com.routerecommendationbackend.services.StatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@CrossOrigin("*")
public class StatsController {

    private final StatsService statsService;

    @GetMapping("statistics/routes-per-month/{user_id}")
    private Map<Integer, List<Route>> getRoutesByMonth(@PathVariable Long user_id){
        return statsService.getRoutesByMonth(user_id);
    }

    @GetMapping("statistics/routes-month/{user_id}")
    private Map<Integer, Integer> getNrOfRoutesPerMonth(@PathVariable Long user_id){
        return statsService.getRoutesPerMonth(user_id);
    }

    @GetMapping("statistics/km-month/{user_id}")
    private Map<Integer, Double> getKmCompletedPerMonth(@PathVariable Long user_id){
        return statsService.getKmCompletedPerMonth(user_id);
    }

    @GetMapping("statistics/emissions-saved-month/{user_id}")
    private Map<Integer, Integer> getEmissionsSavedPerMonth(@PathVariable Long user_id){
        return statsService.getEmissionsSavedPerMonth(user_id);
    }

    @GetMapping("statistics/cal-burned-month/{user_id}")
    private Map<Integer, Integer> getCalBurnedPerMonth(@PathVariable Long user_id){
        return statsService.getCalBurnedPerMonth(user_id);
    }

    @GetMapping("statistics/money-saved-month/{user_id}")
    private Map<Integer, Double> getMoneySavedPerMonth(@PathVariable Long user_id){
        return statsService.getMoneySavedPerMonth(user_id);
    }

    @GetMapping("statistics/transport-percentage/{user_id}")
    private Map<Transport, Integer> getTransportPercentage(@PathVariable Long user_id){
        return statsService.getTransportPercentage(user_id);
    }
}
