package com.routerecommendationbackend.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.routerecommendationbackend.entities.User;
import com.routerecommendationbackend.enums.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "friendship")
public class FriendshipDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user01_id", referencedColumnName = "id")
    private User user01;

    @OneToOne
    @JoinColumn(name = "user02_id", referencedColumnName = "id")
    private User user02;

    @Column(name = "status")
    @JsonProperty("status")
    private Status status = Status.PENDING;

    public FriendshipDTO(){

    }

    public FriendshipDTO(User user01, User user02){
        this.user01 = user01;
        this.user02 = user02;
    }
}
