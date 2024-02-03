package com.routerecommendationbackend.repositories;

import com.routerecommendationbackend.DTOs.SavedAddressDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<SavedAddressDTO, Long> {

    @Query("select a FROM SavedAddressDTO a where a.user.id = :id")
    SavedAddressDTO findByUserId(Long id);
}
