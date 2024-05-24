package com.routerecommendationbackend.controllers;

import com.routerecommendationbackend.DTOs.SavedAddressDTO;
import com.routerecommendationbackend.services.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AddressController {

    private final AddressService addressService;

    @GetMapping("profile/addresses")
    private SavedAddressDTO getAddresses(@RequestParam Long id){
        return this.addressService.getAddresses(id);
    }

    @PutMapping("profile/addresses")
    private SavedAddressDTO saveAddresses(@RequestBody SavedAddressDTO savedAddressDTO){
        return this.addressService.saveAddresses(savedAddressDTO);
    }
}
