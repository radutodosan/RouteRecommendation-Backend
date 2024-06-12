package com.routerecommendationbackend.services;

import com.routerecommendationbackend.DTOs.SavedAddressDTO;
import com.routerecommendationbackend.repositories.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;


    public String decodeAddress(String encodedAddress){
        byte[] decodedBytes = Base64.getDecoder().decode(encodedAddress);
        return new String(decodedBytes);
    }

    public SavedAddressDTO saveAddresses(SavedAddressDTO savedAddressDTO){

        SavedAddressDTO updatedAddresses = this.addressRepository.findByUserId(savedAddressDTO.getUser().getId());

        if(updatedAddresses != null){

            String home = Base64.getEncoder().encodeToString(savedAddressDTO.getHome().getBytes());
            String work = Base64.getEncoder().encodeToString(savedAddressDTO.getWork().getBytes());
            String school = Base64.getEncoder().encodeToString(savedAddressDTO.getSchool().getBytes());
            String other = Base64.getEncoder().encodeToString(savedAddressDTO.getOther().getBytes());

            updatedAddresses.setHome(home);
            updatedAddresses.setWork(work);
            updatedAddresses.setSchool(school);
            updatedAddresses.setOther(other);

            addressRepository.save(updatedAddresses);

            updatedAddresses.setHome(decodeAddress(home));
            updatedAddresses.setWork(decodeAddress(work));
            updatedAddresses.setSchool(decodeAddress(school));
            updatedAddresses.setOther(decodeAddress(other));

            return updatedAddresses;
        }

        return addressRepository.save(savedAddressDTO);
    }

    public SavedAddressDTO getAddresses(Long id){
        SavedAddressDTO addresses = addressRepository.findByUserId(id);

        addresses.setHome(decodeAddress(addresses.getHome()));
        addresses.setWork(decodeAddress(addresses.getWork()));
        addresses.setSchool(decodeAddress(addresses.getSchool()));
        addresses.setOther(decodeAddress(addresses.getOther()));

        return addresses;
    }
}
