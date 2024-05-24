package com.routerecommendationbackend.servicestest;

import com.routerecommendationbackend.entities.User;
import com.routerecommendationbackend.exceptions.user.EmailExistsException;
import com.routerecommendationbackend.exceptions.user.UserExistsException;
import com.routerecommendationbackend.exceptions.user.UserNotFoundException;
import com.routerecommendationbackend.repositories.UserRepository;
import com.routerecommendationbackend.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("password");
        user.setFullName("Test User");
    }

    @Test
    void testFindByUsername_UserExists() throws UserNotFoundException {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        User foundUser = userService.findByUsername("testuser");

        assertNotNull(foundUser);
        assertEquals("testuser", foundUser.getUsername());
    }

    @Test
    void testFindByUsername_UserNotFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> {
            userService.findByUsername("testuser");
        });
    }

    @Test
    void testCreateUser_Success() throws UserExistsException, EmailExistsException {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("testuser", createdUser.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUser_UserExists() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        assertThrows(UserExistsException.class, () -> {
            userService.createUser(user);
        });
    }

    @Test
    void testGetUser_Success() throws UserNotFoundException {
        when(userRepository.findUserByUsername(user.getUsername())).thenReturn(user);
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);

        user.setPassword("encodedPassword");

        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
    }

    @Test
    void testGetUser_UserNotFound() {
        when(userRepository.findUserByUsername(user.getUsername())).thenReturn(null);

        assertThrows(UserNotFoundException.class, () -> {
            userService.getUser(user);
        });
    }

    @Test
    void testGetAllUsers() {
        userService.getAllUsers();
        verify(userRepository, times(1)).findAllByOrderByPointsDesc();
    }

    @Test
    void testDeleteUser() {
        userService.deleteUser(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }
}
