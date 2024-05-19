package com.routerecommendationbackend.controllers;

import com.routerecommendationbackend.DTOs.PasswordDTO;
import com.routerecommendationbackend.entities.User;
import com.routerecommendationbackend.exceptions.user.UserNotFoundException;
import com.routerecommendationbackend.exceptions.user.WrongPasswordException;
import com.routerecommendationbackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@CrossOrigin("*")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    private User createUser(@RequestBody User user){
        return userService.createUser(user);
    }

    @PostMapping("/login")
    private User getUser(@RequestBody User user) throws UserNotFoundException {
        return userService.getUser(user);
    }

    @GetMapping("/ranking")
    private List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/ranking/uvt")
    private List<User> getUVTUsers(){
        return userService.getUvtUsers();
    }

    @DeleteMapping("/profile/{id}")
    private ResponseEntity<?> deleteUser(@PathVariable Long id){
        User existingUser = userService.findById(id);
        if(existingUser == null){
            return ResponseEntity.notFound().build();
        }
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/profile/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) throws WrongPasswordException {
        User existingUser = userService.findById(id);

        if(existingUser == null){
            return ResponseEntity.notFound().build();
        }

        User updatedUser = userService.updateUser(id, user);

        return ResponseEntity.ok(updatedUser);
    }

    @PutMapping("/profile/change-pass/{id}")
    public ResponseEntity<User> changePassword(@PathVariable Long id, @RequestBody PasswordDTO passwordDTO) throws WrongPasswordException {
        User existingUser = userService.findById(id);

        if(existingUser == null){
            return ResponseEntity.notFound().build();
        }

        User updatedUser = userService.changePassword(id, passwordDTO.getOld_password(), passwordDTO.getNew_password());

        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/search/{search}")
    private List<User> searchUsers(@PathVariable String search){
        return userService.searchUsers(search);
    }

    @GetMapping("/user/{username}")
    private User getUserByUsername(@PathVariable String username) throws UserNotFoundException {
        return userService.findByUsername(username);
    }
}
