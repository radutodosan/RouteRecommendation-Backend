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

        String home = "";
        if(savedAddressDTO.getHome() != null){
            home = Base64.getEncoder().encodeToString(savedAddressDTO.getHome().getBytes());
            savedAddressDTO.setHome(home);

        }

        String work = "";
        if(savedAddressDTO.getWork() != null){
            work = Base64.getEncoder().encodeToString(savedAddressDTO.getWork().getBytes());
            savedAddressDTO.setWork(work);
        }

        String school = "";
        if(savedAddressDTO.getSchool() != null){
            school = Base64.getEncoder().encodeToString(savedAddressDTO.getSchool().getBytes());
            savedAddressDTO.setSchool(school);
        }

        String other = "";
        if(savedAddressDTO.getOther() != null){
            other = Base64.getEncoder().encodeToString(savedAddressDTO.getOther().getBytes());
            savedAddressDTO.setOther(other);
        }

        addressRepository.save(savedAddressDTO);

        if(!home.isEmpty())
            savedAddressDTO.setHome(decodeAddress(home));
        if(!work.isEmpty())
            savedAddressDTO.setWork(decodeAddress(work));
        if(!school.isEmpty())
            savedAddressDTO.setSchool(decodeAddress(school));
        if(!other.isEmpty())
            savedAddressDTO.setOther(decodeAddress(other));

        return savedAddressDTO;
    }

    public SavedAddressDTO getAddresses(Long id){
        SavedAddressDTO addresses = addressRepository.findByUserId(id);

        if(addresses != null){
            addresses.setHome(decodeAddress(addresses.getHome()));
            addresses.setWork(decodeAddress(addresses.getWork()));
            addresses.setSchool(decodeAddress(addresses.getSchool()));
            addresses.setOther(decodeAddress(addresses.getOther()));

            return addresses;
        }

        return null;
    }
}
