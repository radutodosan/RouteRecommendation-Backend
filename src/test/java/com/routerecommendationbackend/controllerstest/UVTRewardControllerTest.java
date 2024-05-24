package com.routerecommendationbackend.controllerstest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.routerecommendationbackend.DTOs.UVTRewardDTO;
import com.routerecommendationbackend.controllers.UVTRewardController;
import com.routerecommendationbackend.entities.User;
import com.routerecommendationbackend.enums.Status;
import com.routerecommendationbackend.exceptions.uvtrewards.RewardNotFoundException;
import com.routerecommendationbackend.services.UVTRewardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UVTRewardController.class)
class UVTRewardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UVTRewardService uvtRewardService;

    private User user;
    private UVTRewardDTO reward;
    private LocalDate currentDate;

    @Autowired
    private ObjectMapper objectMapper;

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
    void testGetUserReward() throws Exception {
        List<UVTRewardDTO> rewards = new ArrayList<>();
        rewards.add(reward);
        when(uvtRewardService.getUserReward("testuser")).thenReturn(rewards);

        mockMvc.perform(get("/rewards/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].user.username").value("testuser"));
    }

    @Test
    void testAddReward() throws Exception {
        when(uvtRewardService.addRewards()).thenReturn(user);

        mockMvc.perform(get("/add-rewards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void testClaimReward() throws Exception, RewardNotFoundException {
        reward.setStatus(Status.COMPLETED);
        when(uvtRewardService.claimReward(any(UVTRewardDTO.class))).thenReturn(reward);

        mockMvc.perform(put("/claim-reward")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(reward)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

}
