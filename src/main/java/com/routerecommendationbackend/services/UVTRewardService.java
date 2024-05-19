package com.routerecommendationbackend.services;

import com.routerecommendationbackend.DTOs.UVTRewardDTO;
import com.routerecommendationbackend.entities.User;
import com.routerecommendationbackend.enums.Status;
import com.routerecommendationbackend.exceptions.uvtrewards.RewardNotFoundException;
import com.routerecommendationbackend.repositories.UVTRewardRepository;
import com.routerecommendationbackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UVTRewardService {
    private final UVTRewardRepository uvtRewardRepository;
    private final UserRepository userRepository;

    private final UserService userService;

    public List<UVTRewardDTO> getUserReward(String username){

        return uvtRewardRepository.findAllByUserUsername(username);
    }

    public User addRewards(){
        List<User> uvtUsers = userService.getUvtUsers();


        if(uvtUsers.size() == 1){
            UVTRewardDTO newFirstPlaceReward = new UVTRewardDTO(uvtUsers.get(0), 1, "500");
            uvtRewardRepository.save(newFirstPlaceReward);
        }
        else if (uvtUsers.size() == 2) {
            UVTRewardDTO newFirstPlaceReward = new UVTRewardDTO(uvtUsers.get(0), 1, "500");
            uvtRewardRepository.save(newFirstPlaceReward);

            UVTRewardDTO newSecondPlaceReward = new UVTRewardDTO(uvtUsers.get(1), 2, "250");
            uvtRewardRepository.save(newSecondPlaceReward);
        }
        else if(uvtUsers.size() > 2){
            UVTRewardDTO newFirstPlaceReward = new UVTRewardDTO(uvtUsers.get(0), 1, "500");
            uvtRewardRepository.save(newFirstPlaceReward);

            UVTRewardDTO newSecondPlaceReward = new UVTRewardDTO(uvtUsers.get(1), 2, "250");
            uvtRewardRepository.save(newSecondPlaceReward);

            UVTRewardDTO newThirdPlaceReward = new UVTRewardDTO(uvtUsers.get(2), 3, "100");
            uvtRewardRepository.save(newThirdPlaceReward);
        }

        return uvtUsers.get(0);
    }

    public UVTRewardDTO claimReward(UVTRewardDTO reward) throws RewardNotFoundException {

        UVTRewardDTO searchedReward = uvtRewardRepository.findByUserUsernameAndDateAndStatus(reward.getUser().getUsername(), reward.getDate(), reward.getStatus());

        if(searchedReward != null){
            searchedReward.setStatus(Status.COMPLETED);
            uvtRewardRepository.save(searchedReward);
        }
        else{
            throw new RewardNotFoundException(reward.getUser().getUsername(), reward.getDate());
        }

        return searchedReward;
    }
}
