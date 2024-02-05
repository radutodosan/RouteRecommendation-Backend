package com.routerecommendationbackend.repositories;

import com.routerecommendationbackend.entities.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {

    @Query("SELECT route FROM Route route WHERE route.user.id = :id AND route.status = 0")
    List<Route> findAllByUserIdAndStatusPending(Long id);

    @Query("SELECT route FROM Route route WHERE route.user.id = :id AND route.status = 1")
    List<Route> findAllByUserIdAndStatusCompleted(Long id);


}
