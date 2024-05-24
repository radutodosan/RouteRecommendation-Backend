package com.routerecommendationbackend.servicestest;

import com.routerecommendationbackend.DTOs.UVTRewardDTO;
import com.routerecommendationbackend.entities.User;
import com.routerecommendationbackend.enums.Status;
import com.routerecommendationbackend.exceptions.uvtrewards.RewardNotFoundException;
import com.routerecommendationbackend.repositories.UVTRewardRepository;
import com.routerecommendationbackend.repositories.UserRepository;
import com.routerecommendationbackend.services.UVTRewardService;
import com.routerecommendationbackend.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UVTRewardServiceTest {

    @Mock
    private UVTRewardRepository uvtRewardRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private UVTRewardService uvtRewardService;

    private User user;
    private UVTRewardDTO reward;
    private LocalDate currentDate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUsername("testuser");

        currentDate = LocalDate.now();

        reward = new UVTRewardDTO();
        reward.setUser(user);
        reward.setDate(currentDate);
        reward.setStatus(Status.PENDING);
    }

    @Test
    void testGetUserReward() {
        List<UVTRewardDTO> rewards = new ArrayList<>();
        rewards.add(reward);
        when(uvtRewardRepository.findAllByUserUsername("testuser")).thenReturn(rewards);

        List<UVTRewardDTO> userRewards = uvtRewardService.getUserReward("testuser");

        assertNotNull(userRewards);
        assertEquals(1, userRewards.size());
        assertEquals("testuser", userRewards.get(0).getUser().getUsername());
    }

    @Test
    void testAddRewards_SingleUser() {
        List<User> uvtUsers = new ArrayList<>();
        uvtUsers.add(user);
        when(userService.getUvtUsers()).thenReturn(uvtUsers);

        User result = uvtRewardService.addRewards();

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(uvtRewardRepository, times(1)).save(any(UVTRewardDTO.class));
    }

    @Test
    void testAddRewards_MultipleUsers() {
        List<User> uvtUsers = new ArrayList<>();
        uvtUsers.add(user);
        uvtUsers.add(new User());
        uvtUsers.get(1).setUsername("testuser2");
        when(userService.getUvtUsers()).thenReturn(uvtUsers);

        User result = uvtRewardService.addRewards();

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(uvtRewardRepository, times(2)).save(any(UVTRewardDTO.class));
    }

    @Test
    void testClaimReward_Success() throws RewardNotFoundException {
        when(uvtRewardRepository.findByUserUsernameAndDateAndStatus("testuser", currentDate, Status.PENDING)).thenReturn(reward);

        UVTRewardDTO claimedReward = uvtRewardService.claimReward(reward);

        assertNotNull(claimedReward);
        assertEquals(Status.COMPLETED, claimedReward.getStatus());
        verify(uvtRewardRepository, times(1)).save(claimedReward);
    }

    @Test
    void testClaimReward_RewardNotFound() {
        when(uvtRewardRepository.findByUserUsernameAndDateAndStatus("testuser", currentDate, Status.PENDING)).thenReturn(null);

        assertThrows(RewardNotFoundException.class, () -> {
            uvtRewardService.claimReward(reward);
        });
    }
}
