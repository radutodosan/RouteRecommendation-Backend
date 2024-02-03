package com.routerecommendationbackend.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.routerecommendationbackend.entities.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "saved_addresses")
public class SavedAddressDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "home")
    @JsonProperty("home")
    private String home;

    @Column(name = "work")
    @JsonProperty("work")
    private String work;

    @Column(name = "school")
    @JsonProperty("school")
    private String school;

    @Column(name = "other")
    @JsonProperty("other")
    private String other;

    public SavedAddressDTO() {

    }
}
