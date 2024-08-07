package com.routerecommendationbackend.services;

import com.routerecommendationbackend.entities.User;
import com.routerecommendationbackend.exceptions.*;
import com.routerecommendationbackend.exceptions.user.EmailExistsException;
import com.routerecommendationbackend.exceptions.user.UserExistsException;
import com.routerecommendationbackend.exceptions.user.UserNotFoundException;
import com.routerecommendationbackend.exceptions.user.WrongPasswordException;
import com.routerecommendationbackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder = new PasswordEncoder() {
        @Override
        public String encode(CharSequence rawPassword) {
            return null;
        }

        @Override
        public boolean matches(CharSequence rawPassword, String encodedPassword) {
            return false;
        }
    };

    public User findByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    public User findById(Long id){
        return userRepository.findById(id).orElse(null);
    }

    public User createUser(User user) throws UserExistsException, EmailExistsException, EmptyCredentialsException {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new UserExistsException("User " + user.getUsername() + " already exists!");
        }

        if(userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new EmailExistsException("Email " + user.getEmail() + " already exists!");
        }

        if(user.getUsername().isBlank() && user.getEmail().isBlank() && user.getPassword().isBlank() && user.getFullName().isBlank()){
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

    public List<User> getUvtUsers(){
        List<User> allUsers = userRepository.findAllByOrderByPointsDesc();

        List<User> uvtUsers = new ArrayList<>();
        for (User user : allUsers){
            if(user.getEmail().contains("@e-uvt.ro")) {
                uvtUsers.add(user);
            }
        }

        return uvtUsers;
    }

    public List<User> searchUsers(String search){
        if(search.isBlank())
            return userRepository.findAll();
        return userRepository.findAllByFullNameContains(search);
    }


    public void deleteUser(Long id){
        userRepository.deleteById(id);
    }

    public User updateUser(Long id, User user) throws EmailExistsException, WrongPasswordException {
        User user1  = this.findById(id);

        if(!Objects.equals(user1.getEmail(), user.getEmail()) && userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new EmailExistsException("Email " + user.getEmail() + " already exists!");
        }

        if(user.getFullName().isBlank() || user.getEmail().isBlank()){
            throw new EmptyCredentialsException("Required credentials are empty!");
        }

        String password = user.getPassword();
        String encodedPassword = user1.getPassword();

        boolean isPwdRight = passwordEncoder.matches(password, encodedPassword);

        if(isPwdRight){
            user1.setFullName(user.getFullName());
            user1.setEmail(user.getEmail());
            return userRepository.save(user1);
        }

        throw new WrongPasswordException();
    }

    public User changePassword(Long id, String oldPass, String newPass) throws WrongPasswordException{
        User user1  = this.findById(id);

        String password = user1.getPassword();

        boolean isPwdRight = passwordEncoder.matches(oldPass, password);

        if(isPwdRight){
            user1.setPassword(passwordEncoder.encode(newPass));
            return userRepository.save(user1);
        }

        throw new WrongPasswordException();
    }

    public boolean forgotPassword(String username, String newPass) throws WrongPasswordException, UserNotFoundException {
        User user1  = this.findByUsername(username);

        if(!user1.getUsername().isEmpty()){
            user1.setPassword(passwordEncoder.encode(newPass));
            userRepository.save(user1);

            return true;
        }

        throw new WrongPasswordException();
    }

    public void updatePoints(Long id, int points) throws ThrowableException {
        User user = this.findById(id);

        if(user != null){
            user.setPoints(user.getPoints() + points);

            userRepository.save(user);
        }
        else{
            throw new ThrowableException("Error updating points for user with id: " + id);
        }
    }
}
