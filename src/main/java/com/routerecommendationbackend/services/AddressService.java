package com.routerecommendationbackend.services;

import com.routerecommendationbackend.DTOs.SavedAddressDTO;
import com.routerecommendationbackend.repositories.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    public SavedAddressDTO saveAddresses(SavedAddressDTO savedAddressDTO){

        SavedAddressDTO updatedAddresses = this.addressRepository.findByUserId(savedAddressDTO.getUser().getId());

        if(updatedAddresses != null){
            updatedAddresses.setHome(savedAddressDTO.getHome());
            updatedAddresses.setWork(savedAddressDTO.getWork());
            updatedAddresses.setSchool(savedAddressDTO.getSchool());
            updatedAddresses.setOther(savedAddressDTO.getOther());

            return addressRepository.save(updatedAddresses);
        }

        return addressRepository.save(savedAddressDTO);
    }

    public SavedAddressDTO getAddresses(Long id){
        return addressRepository.findByUserId(id);
    }
}
