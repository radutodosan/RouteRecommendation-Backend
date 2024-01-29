package com.routerecommendationbackend.services;

import com.routerecommendationbackend.entities.User;
import com.routerecommendationbackend.exceptions.EmailExistsException;
import com.routerecommendationbackend.exceptions.UserExistsException;
import com.routerecommendationbackend.exceptions.UserNotFoundException;
import com.routerecommendationbackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncode;

    public User findByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public User createUser(User user) throws UserExistsException, EmailExistsException{
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserExistsException("User " + user.getUsername() + " already exists!");
        }

        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new EmailExistsException("Email " + user.getEmail() + " already exists!");

        }

        user.setPicture_url("https://robohash.org/" + user.getUsername() + ".png");
        user.setPassword(passwordEncode.encode(user.getPassword()));

        return userRepository.save(user);
    }
}
