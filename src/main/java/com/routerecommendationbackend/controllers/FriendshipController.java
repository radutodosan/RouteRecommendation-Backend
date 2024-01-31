package com.routerecommendationbackend.controllers;

import com.routerecommendationbackend.DTOs.FriendshipDTO;
import com.routerecommendationbackend.exceptions.UserNotFoundException;
import com.routerecommendationbackend.services.FriendshipService;
import com.routerecommendationbackend.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@CrossOrigin("*")
public class FriendshipController {
    private final FriendshipService friendshipService;

    @GetMapping("social/{username}")
    private List<User> getFriendsList(@PathVariable String username){
        return friendshipService.getFriendsList(username);
    }

    @GetMapping("social/add-friends/{username}")
    private List<User> getNonFriendsList(@PathVariable String username){
        return friendshipService.getNonFriendsList(username);
    }

    @GetMapping("notifications/friends/{username}")
    private List<User> getPendingFriendRequests(@PathVariable String username){
        return friendshipService.getPendingFriendRequests(username);
    }

    @PostMapping("social/add-friends")
    private ResponseEntity<FriendshipDTO> sendFriendRequest(@RequestParam String username01, @RequestParam String username02) throws UserNotFoundException {
        return ResponseEntity.ok(friendshipService.sendFriendRequest(username01, username02));
    }

    @PutMapping("notifications/friends")
    private ResponseEntity<FriendshipDTO> acceptFriendRequest(@RequestParam String username01, @RequestParam String username02) throws UserNotFoundException {
        return ResponseEntity.ok(friendshipService.acceptFriendRequest(username02, username01));
    }

//    @DeleteMapping("delete-friendship")
//    private ResponseEntity<FriendshipDTO> deleteFriendShip(@RequestParam String username01, @RequestParam String username02){
//        return ResponseEntity.ok(friendshipService.deleteFriendShip(username01, username02));
//    }

    @DeleteMapping("delete-friendship")
    private void deleteFriendShip(@RequestParam String username01, @RequestParam String username02){
        friendshipService.deleteFriendShip(username01, username02);
    }

    @GetMapping("social")
    private ResponseEntity<List<User>> searchFriends(@RequestParam String username, @RequestParam String search){
        return ResponseEntity.ok(friendshipService.searchFriends(username, search));
    }

    @GetMapping("social/add-friends")
    private ResponseEntity<List<User>> searchNonFriends(@RequestParam String username, @RequestParam String search){
        return ResponseEntity.ok(friendshipService.searchNonFriends(username, search));
    }

    @GetMapping("ranking/{username}")
    private ResponseEntity<List<User>> getFriendsRanking(@PathVariable String username){
        return ResponseEntity.ok(friendshipService.getFriendsRanking(username));
    }
}
