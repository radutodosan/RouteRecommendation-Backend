package com.routerecommendationbackend.services;

import com.routerecommendationbackend.entities.Route;
import com.routerecommendationbackend.enums.Transport;
import com.routerecommendationbackend.exceptions.StatsException;
import com.routerecommendationbackend.repositories.StatsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class StatsService {

    private final StatsRepository statsRepository;

    public Map<Integer, List<Route>> getRoutesByMonth(Long user_id) throws StatsException{
        Map<Integer, List<Route>> routesByMonth = new HashMap<>();

        List<Route> userRoutes = statsRepository.getRoutesByUserId(user_id);


        for(Route route: userRoutes){
            if(routesByMonth.containsKey(route.getDate().getMonthValue())){
                routesByMonth.get(route.getDate().getMonthValue()).add(route);
            }
            else {
                List<Route> tmpList = new ArrayList<>();
                tmpList.add(route);
                routesByMonth.put(route.getDate().getMonthValue(), tmpList);
            }
        }

        return routesByMonth;
    }

    public Map<Integer, Integer> getRoutesPerMonth(Long user_id) throws StatsException{
        Map<Integer, Integer> nrOfRoutesByMonth = new HashMap<>();

        Map<Integer, List<Route>> userRoutesByMonth = this.getRoutesByMonth(user_id);

        for(int i = 1; i <= 12; i++)
            nrOfRoutesByMonth.put(i,0);

        for(Integer key: userRoutesByMonth.keySet()){
            nrOfRoutesByMonth.put(key, userRoutesByMonth.get(key).size());
        }

        return nrOfRoutesByMonth;
    }

    public Map<Integer, Integer> getKmCompletedPerMonth(Long user_id) throws StatsException{
        Map<Integer, Integer> kmCompleterPerMonth = new HashMap<>();

        Map<Integer, List<Route>> userRoutesPerMonth = this.getRoutesByMonth(user_id);

        for(int i = 1; i <= 12; i++)
            kmCompleterPerMonth.put(i,0);

        for(Integer key: userRoutesPerMonth.keySet()){
            List<Route> listOfRoutes = userRoutesPerMonth.get(key);
            int kmCompleted = 0;
            for(Route route: listOfRoutes)
                kmCompleted += route.getDistance();
            kmCompleterPerMonth.put(key, kmCompleted);
        }

        return kmCompleterPerMonth;
    }

    public Map<Integer, Integer> getEmissionsSavedPerMonth(Long user_id) throws StatsException{
        Map<Integer, Integer> emissionsSavedPerMonth = new HashMap<>();

        Map<Integer, List<Route>> userRoutesPerMonth = this.getRoutesByMonth(user_id);

        for(int i = 1; i <= 12; i++)
            emissionsSavedPerMonth.put(i,0);

        for(Integer key: userRoutesPerMonth.keySet()){
            List<Route> listOfRoutes = userRoutesPerMonth.get(key);
            int emissionsSaved = 0;
            for(Route route: listOfRoutes)
                emissionsSaved += route.getEmissions_saved();
            emissionsSavedPerMonth.put(key, emissionsSaved);
        }

        return emissionsSavedPerMonth;
    }

    public Map<Integer, Integer> getCalBurnedPerMonth(Long user_id) throws StatsException{
        Map<Integer, Integer> calBurnedPerMonth = new HashMap<>();

        Map<Integer, List<Route>> userRoutesPerMonth = this.getRoutesByMonth(user_id);

        for(int i = 1; i <= 12; i++)
            calBurnedPerMonth.put(i,0);

        for(Integer key: userRoutesPerMonth.keySet()){
            List<Route> listOfRoutes = userRoutesPerMonth.get(key);
            int calBurned = 0;
            for(Route route: listOfRoutes)
                calBurned += route.getCal_burned();
            calBurnedPerMonth.put(key, calBurned);
        }

        return calBurnedPerMonth;
    }

    public Map<Integer, Double> getMoneySavedPerMonth(Long user_id) throws StatsException{
        Map<Integer, Double> moneySavedPerMonth = new HashMap<>();

        Map<Integer, List<Route>> userRoutesPerMonth = this.getRoutesByMonth(user_id);

        for(int i = 1; i <= 12; i++)
            moneySavedPerMonth.put(i,0.0);

        for(Integer key: userRoutesPerMonth.keySet()){
            List<Route> listOfRoutes = userRoutesPerMonth.get(key);
            double moneySaved = 0;
            for(Route route: listOfRoutes)
                moneySaved += (double) route.getDistance() * 0.55;
            moneySavedPerMonth.put(key, moneySaved);
        }

        return moneySavedPerMonth;
    }

    public Map<Transport, Integer> getTransportPercentage(Long user_id) throws StatsException{
        Map<Transport, Integer> transportTypes = new HashMap<>();

        List<Route> userRoutes = statsRepository.getRoutesByUserId(user_id);

        for(Route route: userRoutes){
            if(transportTypes.containsKey(route.getTransport())){
                transportTypes.put(route.getTransport(), transportTypes.get(route.getTransport()) + 1);
            }
            else{
                transportTypes.put(route.getTransport(), 1);
            }
        }

        return transportTypes;
    }

}
