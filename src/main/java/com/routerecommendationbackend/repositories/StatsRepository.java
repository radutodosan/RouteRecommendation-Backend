package com.routerecommendationbackend.repositories;

import com.routerecommendationbackend.entities.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Route, Long> {


    @Query("SELECT route FROM Route route WHERE route.user.id = :user_id AND route.status = 1")
    List<Route> getRoutesByUserId(Long user_id);
}
