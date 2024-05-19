package com.routerecommendationbackend.repositories;

import com.routerecommendationbackend.DTOs.UVTRewardDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UVTRewardRepository extends JpaRepository<UVTRewardDTO, Long> {

//    @Query("SELECT reward FROM UVTRewardDTO reward WHERE reward.user.username = :username")
    List<UVTRewardDTO> findAllByUserUsername(String username);
}
