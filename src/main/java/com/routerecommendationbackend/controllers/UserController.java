package com.routerecommendationbackend.controllers;

import com.routerecommendationbackend.entities.User;
import com.routerecommendationbackend.exceptions.UserNotFoundException;
import com.routerecommendationbackend.exceptions.WrongPasswordException;
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

    @DeleteMapping("/profile/{id}")
    private ResponseEntity<?> deleteUser(@PathVariable Long id) throws UserNotFoundException {
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
//        existingUser.setFull_name(user.getFull_name());
//        existingUser.setEmail(user.getEmail());
//        existingUser.setSaved_address(user.getSaved_address());
//        existingUser.setPassword(user.getPassword());

        User updatedUser = userService.updateUser(id, user);

        return ResponseEntity.ok(updatedUser);
    }
}
