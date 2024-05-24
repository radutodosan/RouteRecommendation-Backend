package com.routerecommendationbackend.controllerstest;

import com.routerecommendationbackend.DTOs.PasswordDTO;
import com.routerecommendationbackend.controllers.UserController;
import com.routerecommendationbackend.entities.User;
import com.routerecommendationbackend.exceptions.user.UserNotFoundException;
import com.routerecommendationbackend.exceptions.user.WrongPasswordException;
import com.routerecommendationbackend.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("password");
        user.setFullName("Test User");
    }

    @Test
    void testCreateUser() throws Exception {
        when(userService.createUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/signup")
                        .contentType("application/json")
                        .content("{\"username\": \"testuser\", \"email\": \"testuser@example.com\", \"password\": \"password\", \"fullName\": \"Test User\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void testGetUser() throws Exception, UserNotFoundException {
        when(userService.getUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/login")
                        .contentType("application/json")
                        .content("{\"username\": \"testuser\", \"password\": \"password\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void testGetAllUsers() throws Exception {
        mockMvc.perform(get("/ranking"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteUser() throws Exception {
        when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(delete("/profile/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateUser() throws Exception, WrongPasswordException {
        when(userService.findById(1L)).thenReturn(user);
        when(userService.updateUser(any(Long.class), any(User.class))).thenReturn(user);

        mockMvc.perform(put("/profile/1")
                        .contentType("application/json")
                        .content("{\"username\": \"testuser\", \"email\": \"testuser@example.com\", \"password\": \"password\", \"fullName\": \"Test User\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void testChangePassword() throws Exception, WrongPasswordException {
        PasswordDTO passwordDTO = new PasswordDTO();
        passwordDTO.setOld_password("oldPassword");
        passwordDTO.setNew_password("newPassword");

        when(userService.findById(1L)).thenReturn(user);
        when(userService.changePassword(1L, "oldPassword", "newPassword")).thenReturn(user);

        mockMvc.perform(put("/profile/change-pass/1")
                        .contentType("application/json")
                        .content("{\"old_password\": \"oldPassword\", \"new_password\": \"newPassword\"}"))
                .andExpect(status().isOk());
    }
}
