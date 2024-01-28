package com.routerecommendationbackend.controllers;

import com.routerecommendationbackend.entities.User;
import com.routerecommendationbackend.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
}
