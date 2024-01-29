package com.routerecommendationbackend.services;

import com.routerecommendationbackend.entities.User;
import com.routerecommendationbackend.exceptions.EmailExistsException;
import com.routerecommendationbackend.exceptions.EmptyCredentialsException;
import com.routerecommendationbackend.exceptions.UserExistsException;
import com.routerecommendationbackend.exceptions.UserNotFoundException;
import com.routerecommendationbackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User findByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public User findById(Long id) throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("hello"));
    }

    public User createUser(User user) throws UserExistsException, EmailExistsException, EmptyCredentialsException {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserExistsException("User " + user.getUsername() + " already exists!");
        }

        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new EmailExistsException("Email " + user.getEmail() + " already exists!");

        }

        if(user.getUsername().isBlank() && user.getEmail().isBlank() && user.getPassword().isBlank() && user.getFull_name().isBlank()){
            throw new EmptyCredentialsException("Required credentials are empty!");
        }

        user.setPicture_url("https://robohash.org/" + user.getUsername() + ".png");
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    public User getUser(User user) throws UserNotFoundException {

        User user1  = userRepository.findUserByUsername(user.getUsername());
        if(user1 != null){
            String password = user.getPassword();
            String encodedPassword = user1.getPassword();

            boolean isPwdRight = passwordEncoder.matches(password, encodedPassword);


            if(isPwdRight){
                return user1;
            }
        }
        else{
            throw new UserNotFoundException(user.getUsername());

        }
        return null;
    }

    public List<User> getAllUsers(){
        return userRepository.findAllByOrderByPointsDesc();
    }

    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }
}
