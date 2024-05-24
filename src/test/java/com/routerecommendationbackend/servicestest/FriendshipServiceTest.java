package com.routerecommendationbackend.servicestest;

import com.routerecommendationbackend.DTOs.FriendshipDTO;
import com.routerecommendationbackend.entities.User;
import com.routerecommendationbackend.enums.Status;
import com.routerecommendationbackend.exceptions.friendship.FriendRequestException;
import com.routerecommendationbackend.exceptions.user.UserNotFoundException;
import com.routerecommendationbackend.repositories.FriendshipRepository;
import com.routerecommendationbackend.repositories.UserRepository;
import com.routerecommendationbackend.services.FriendshipService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FriendshipServiceTest {

    @Mock
    private FriendshipRepository friendshipRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FriendshipService friendshipService;

    private User user01;
    private User user02;
    private FriendshipDTO friendshipDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user01 = new User();
        user01.setUsername("user01");
        user01.setFullName("radu01");

        user02 = new User();
        user02.setUsername("user02");
        user02.setFullName("radu02");

        friendshipDTO = new FriendshipDTO(user01, user02);
        friendshipDTO.setStatus(Status.PENDING);
    }

    @Test
    void testGetFriendsList() {
        List<User> list01 = new ArrayList<>();
        List<User> list02 = new ArrayList<>();
        list01.add(user02);
        when(friendshipRepository.findAllByUser01_UsernameCompleted("user01")).thenReturn(list01);
        when(friendshipRepository.findAllByUser02_UsernameCompleted("user01")).thenReturn(list02);

        List<User> friends = friendshipService.getFriendsList("user01");

        assertNotNull(friends);
        assertEquals(1, friends.size());
    }

    @Test
    void testSearchFriends() {
        List<User> listOfFriends = new ArrayList<>();
        listOfFriends.add(user02);
        when(friendshipRepository.findAllByUser01_UsernameCompleted("user01")).thenReturn(listOfFriends);
        when(friendshipRepository.findAllByUser02_UsernameCompleted("user01")).thenReturn(new ArrayList<>());

        List<User> friends = friendshipService.searchFriends("user01", "radu02");

        assertNotNull(friends);
        assertEquals(1, friends.size());
    }

    @Test
    void testGetNonFriendsList() {
        List<User> allUsers = new ArrayList<>();
        allUsers.add(user01);
        allUsers.add(user02);
        when(userRepository.findAll()).thenReturn(allUsers);
        when(friendshipRepository.findAllByUser01_UsernamePending("user01")).thenReturn(new ArrayList<>());
        when(friendshipRepository.findAllByUser02_UsernamePending("user01")).thenReturn(new ArrayList<>());

        List<User> nonFriends = friendshipService.getNonFriendsList("user01");

        assertNotNull(nonFriends);
        assertEquals(1, nonFriends.size());
    }

    @Test
    void testSearchNonFriends() {
        List<User> allUsers = new ArrayList<>();
        allUsers.add(user01);
        allUsers.add(user02);
        when(userRepository.findAll()).thenReturn(allUsers);
        when(friendshipRepository.findAllByUser01_UsernamePending("user01")).thenReturn(new ArrayList<>());
        when(friendshipRepository.findAllByUser02_UsernamePending("user01")).thenReturn(new ArrayList<>());

        List<User> nonFriends = friendshipService.searchNonFriends("user01", "user02");

        assertNotNull(nonFriends);
        assertEquals(1, nonFriends.size());
    }

    @Test
    void testGetPendingFriendRequests() {
        List<User> pendingRequests = new ArrayList<>();
        pendingRequests.add(user01);
        when(friendshipRepository.findAllByUser02_UsernamePending("user02")).thenReturn(pendingRequests);

        List<User> result = friendshipService.getPendingFriendRequests("user02");

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testSendFriendRequest() throws UserNotFoundException {
        when(userRepository.findUserByUsername("user01")).thenReturn(user01);
        when(userRepository.findUserByUsername("user02")).thenReturn(user02);
        when(friendshipRepository.save(any(FriendshipDTO.class))).thenReturn(friendshipDTO);

        FriendshipDTO result = friendshipService.sendFriendRequest("user01", "user02");

        assertNotNull(result);
        assertEquals(Status.PENDING, result.getStatus());
    }

    @Test
    void testAcceptFriendRequest() throws UserNotFoundException, FriendRequestException {
        when(userRepository.findUserByUsername("user01")).thenReturn(user01);
        when(userRepository.findUserByUsername("user02")).thenReturn(user02);
        when(friendshipRepository.findByUser01AndUser02Accept("user01", "user02")).thenReturn(friendshipDTO);
        when(friendshipRepository.save(any(FriendshipDTO.class))).thenReturn(friendshipDTO);

        FriendshipDTO result = friendshipService.acceptFriendRequest("user01", "user02");

        assertNotNull(result);
        assertEquals(Status.COMPLETED, result.getStatus());
    }

    @Test
    void testDeleteFriendShip() throws FriendRequestException {
        when(friendshipRepository.deleteFriendshipByUsername01AndUsername02("user01", "user02")).thenReturn(friendshipDTO);
        when(userRepository.findUserByUsername("user01")).thenReturn(user01);
        when(userRepository.findUserByUsername("user02")).thenReturn(user02);

        friendshipService.deleteFriendShip("user01", "user02");

        verify(friendshipRepository, times(1)).delete(friendshipDTO);
    }
}
