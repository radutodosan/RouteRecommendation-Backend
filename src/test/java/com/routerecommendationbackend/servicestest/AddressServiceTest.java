package com.routerecommendationbackend.servicestest;

import com.routerecommendationbackend.DTOs.SavedAddressDTO;
import com.routerecommendationbackend.entities.User;
import com.routerecommendationbackend.repositories.AddressRepository;
import com.routerecommendationbackend.services.AddressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressService addressService;

    private User user;
    private SavedAddressDTO savedAddressDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setId(1L);

        savedAddressDTO = new SavedAddressDTO();
        savedAddressDTO.setUser(user);
        savedAddressDTO.setHome("Home Address");
        savedAddressDTO.setWork("Work Address");
        savedAddressDTO.setSchool("School Address");
        savedAddressDTO.setOther("Other Address");
    }

    @Test
    void testSaveAddresses_NewAddress() {
        when(addressRepository.save(savedAddressDTO)).thenReturn(savedAddressDTO);

        SavedAddressDTO result = addressService.saveAddresses(savedAddressDTO);

        assertNotNull(result);
        assertEquals("Home Address", result.getHome());
        verify(addressRepository, times(1)).save(savedAddressDTO);
    }

    @Test
    void testSaveAddresses_UpdateAddress() {
        SavedAddressDTO existingAddress = new SavedAddressDTO();
        existingAddress.setUser(user);
        existingAddress.setHome("Old Home Address");
        existingAddress.setWork("Old Work Address");
        existingAddress.setSchool("Old School Address");
        existingAddress.setOther("Old Other Address");

        when(addressRepository.findByUserId(user.getId())).thenReturn(existingAddress);
        when(addressRepository.save(existingAddress)).thenReturn(existingAddress);

        SavedAddressDTO result = addressService.saveAddresses(savedAddressDTO);

        assertNotNull(result);
        assertEquals("Home Address", result.getHome());
        assertEquals("Work Address", result.getWork());
        verify(addressRepository, times(1)).save(existingAddress);
    }

    @Test
    void testGetAddresses() {
        when(addressRepository.findByUserId(user.getId())).thenReturn(savedAddressDTO);

        SavedAddressDTO result = addressService.getAddresses(user.getId());

        assertNotNull(result);
        assertEquals("Home Address", result.getHome());
    }
}
