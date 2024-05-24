package com.routerecommendationbackend.controllerstest;

import com.routerecommendationbackend.DTOs.FriendshipDTO;
import com.routerecommendationbackend.controllers.FriendshipController;
import com.routerecommendationbackend.entities.User;
import com.routerecommendationbackend.enums.Status;
import com.routerecommendationbackend.exceptions.user.UserNotFoundException;
import com.routerecommendationbackend.services.FriendshipService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FriendshipController.class)
class FriendshipControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FriendshipService friendshipService;

    private User user1;
    private User user2;
    private FriendshipDTO friendship;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");

        user2 = new User();
        user2.setId(2L);
        user2.setUsername("user2");

        friendship = new FriendshipDTO(user1, user2);
    }

    @Test
    void testGetFriendsList() throws Exception {
        List<User> friendsList = new ArrayList<>();
        friendsList.add(user2);

        when(friendshipService.getFriendsList("user1")).thenReturn(friendsList);

        mockMvc.perform(get("/social/user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("user2"));
    }

    @Test
    void testGetNonFriendsList() throws Exception {
        List<User> nonFriendsList = new ArrayList<>();
        nonFriendsList.add(user2);

        when(friendshipService.getNonFriendsList("user1")).thenReturn(nonFriendsList);

        mockMvc.perform(get("/social/add-friends/user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("user2"));
    }

    @Test
    void testGetPendingFriendRequests() throws Exception {
        List<User> pendingRequests = new ArrayList<>();
        pendingRequests.add(user1);

        when(friendshipService.getPendingFriendRequests("user2")).thenReturn(pendingRequests);

        mockMvc.perform(get("/notifications/friends/user2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("user1"));
    }

    @Test
    void testSendFriendRequest() throws Exception, UserNotFoundException {
        when(friendshipService.sendFriendRequest("user1", "user2")).thenReturn(friendship);

        mockMvc.perform(post("/social/add-friends")
                        .param("username01", "user1")
                        .param("username02", "user2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user01.username").value("user1"))
                .andExpect(jsonPath("$.user02.username").value("user2"));
    }

    @Test
    void testAcceptFriendRequest() throws Exception, UserNotFoundException {
        friendship.setStatus(Status.COMPLETED);
        when(friendshipService.acceptFriendRequest("user2", "user1")).thenReturn(friendship);

        mockMvc.perform(put("/notifications/friends")
                        .param("username01", "user1")
                        .param("username02", "user2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    @Test
    void testDeleteFriendShip() throws Exception {
        mockMvc.perform(delete("/delete-friendship")
                        .param("username01", "user1")
                        .param("username02", "user2"))
                .andExpect(status().isOk());

        verify(friendshipService, times(1)).deleteFriendShip("user1", "user2");
    }

    @Test
    void testSearchFriends() throws Exception {
        List<User> friendsList = new ArrayList<>();
        friendsList.add(user2);

        when(friendshipService.searchFriends("user1", "user2")).thenReturn(friendsList);

        mockMvc.perform(get("/social")
                        .param("username", "user1")
                        .param("search", "user2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("user2"));
    }

    @Test
    void testSearchNonFriends() throws Exception {
        List<User> nonFriendsList = new ArrayList<>();
        nonFriendsList.add(user2);

        when(friendshipService.searchNonFriends("user1", "user2")).thenReturn(nonFriendsList);

        mockMvc.perform(get("/social/add-friends")
                        .param("username", "user1")
                        .param("search", "user2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("user2"));
    }

    @Test
    void testGetFriendsRanking() throws Exception {
        List<User> friendsList = new ArrayList<>();
        friendsList.add(user2);

        when(friendshipService.getFriendsRanking("user1")).thenReturn(friendsList);

        mockMvc.perform(get("/ranking/user1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("user2"));
    }
}
