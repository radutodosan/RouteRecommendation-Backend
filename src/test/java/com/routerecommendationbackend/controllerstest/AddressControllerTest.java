package com.routerecommendationbackend.controllerstest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.routerecommendationbackend.DTOs.SavedAddressDTO;
import com.routerecommendationbackend.controllers.AddressController;
import com.routerecommendationbackend.entities.User;
import com.routerecommendationbackend.services.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AddressController.class)
class AddressControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AddressService addressService;

    private User user;
    private SavedAddressDTO savedAddressDTO;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);
        user.setUsername("user01");

        savedAddressDTO = new SavedAddressDTO();
        savedAddressDTO.setId(1L);
        savedAddressDTO.setUser(user);
        savedAddressDTO.setHome("Home Address");
        savedAddressDTO.setWork("Work Address");
        savedAddressDTO.setSchool("School Address");
        savedAddressDTO.setOther("Other Address");
    }

    @Test
    void testGetAddresses() throws Exception {
        when(addressService.getAddresses(1L)).thenReturn(savedAddressDTO);

        mockMvc.perform(get("/profile/addresses")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.home").value("Home Address"))
                .andExpect(jsonPath("$.work").value("Work Address"))
                .andExpect(jsonPath("$.school").value("School Address"))
                .andExpect(jsonPath("$.other").value("Other Address"));
    }

    @Test
    void testSaveAddresses() throws Exception {
        when(addressService.saveAddresses(savedAddressDTO)).thenReturn(savedAddressDTO);

        MvcResult result = mockMvc.perform(put("/profile/addresses")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(savedAddressDTO)))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        System.out.println("Response content: " + content);

        mockMvc.perform(put("/profile/addresses")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(savedAddressDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.home").value("Home Address"))
                .andExpect(jsonPath("$.work").value("Work Address"))
                .andExpect(jsonPath("$.school").value("School Address"))
                .andExpect(jsonPath("$.other").value("Other Address"));
    }
}
