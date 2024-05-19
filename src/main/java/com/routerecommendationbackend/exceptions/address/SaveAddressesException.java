package com.routerecommendationbackend.exceptions.address;

import com.routerecommendationbackend.DTOs.SavedAddressDTO;

public class SaveAddressesException extends RuntimeException{
    public SaveAddressesException(SavedAddressDTO savedAddressDTO) {
        super("Failed saving addresses: Home:" +
                savedAddressDTO.getHome() + ", Work: " + savedAddressDTO.getWork() + ", School: " + savedAddressDTO.getSchool() + ", Other: " + savedAddressDTO.getOther()
                + " for " + savedAddressDTO.getUser().getUsername());
    }
}

