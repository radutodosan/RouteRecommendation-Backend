package com.routerecommendationbackend.controllers;

import com.routerecommendationbackend.DTOs.UVTRewardDTO;
import com.routerecommendationbackend.entities.User;
import com.routerecommendationbackend.exceptions.RewardNotFoundException;
import com.routerecommendationbackend.services.UVTRewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UVTRewardController {
    private final UVTRewardService uvtRewardService;

    @GetMapping("/rewards/{username}")
    private List<UVTRewardDTO> getUserReward(@PathVariable String username){
        return uvtRewardService.getUserReward(username);
    }

    @GetMapping("/add-rewards")
    private User addReward(){
        return uvtRewardService.addRewards();
    }

    @PutMapping("/claim-reward")
    private UVTRewardDTO claimReward(@RequestBody UVTRewardDTO reward) throws RewardNotFoundException {

        return uvtRewardService.claimReward(reward);
    }
}
