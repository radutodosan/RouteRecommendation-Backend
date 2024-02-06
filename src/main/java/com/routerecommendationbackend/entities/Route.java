package com.routerecommendationbackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.routerecommendationbackend.enums.Status;
import com.routerecommendationbackend.enums.Transport;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "routes")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Column(nullable = false, unique = true)
    @JsonProperty("start")
    private String start;

    @Column(nullable = false, unique = true)
    @JsonProperty("end")
    private String end;

    @Column(nullable = false, unique = true)
    @JsonProperty("transport")
    private Transport transport;

    @Column(nullable = false, unique = true)
    @JsonProperty("distance")
    private int distance = 5;

    @Column(nullable = false, unique = true)
    @JsonProperty("time")
    private int time = 10;

    @Column(nullable = false, unique = true)
    @JsonProperty("emissions_saved")
    private int emissions_saved = 100;

    @Column(nullable = false, unique = true)
    @JsonProperty("cal_burned")
    private int cal_burned = 125;

    @Column(nullable = false, unique = true)
    @JsonProperty("status")
    private Status status = Status.PENDING;

    public Route(){

    }
}
