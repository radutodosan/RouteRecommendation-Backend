package com.routerecommendationbackend.repositories;

import com.routerecommendationbackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    User findUserByUsername(String username);

    Optional<User> findByEmail(String email);

    List<User> findAllByOrderByPointsDesc();

    List<User> findAllByFullNameContains(String search);


}
