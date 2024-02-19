package com.routerecommendationbackend.repositories;

import com.routerecommendationbackend.entities.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Route, Long> {


    List<Route> getRoutesByUserId(Long user_id);
}
