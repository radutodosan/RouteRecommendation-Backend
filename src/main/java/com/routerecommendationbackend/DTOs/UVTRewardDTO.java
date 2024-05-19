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
@Table(name = "uvt_rewards")
public class UVTRewardDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(name = "placement")
    @JsonProperty("placement")
    private Integer placement;

    @Column(name = "reward")
    @JsonProperty("reward")
    private String reward;

    @Column(name = "status")
    @JsonProperty("status")
    private Status status = Status.PENDING;

    public UVTRewardDTO(){

    }

    public UVTRewardDTO(User user, Integer placement, String reward){
        this.user = user;
        this.placement = placement;
        this.reward = reward;
    }
}
