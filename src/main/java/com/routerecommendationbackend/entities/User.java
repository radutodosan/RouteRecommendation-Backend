package com.routerecommendationbackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @Column(nullable = false, unique = true)
    @JsonProperty("username")
    private String username;

    @Column(nullable = false)
    @JsonProperty("full_name")
    private String full_name;

    @Column(nullable = false, unique = true)
    @JsonProperty("email")
    private String email;

    @Column(name = "password", length = 255)
    @JsonProperty("password")
    private String password;

    @Column(name = "picture_url")
    @JsonProperty("picture_url")
    private String picture_url;

    @Column(name = "points")
    @JsonProperty("points")
    private int points;

    @Column(name = "saved_address")
    @JsonProperty("saved_address")
    private String saved_address;

    public User(){

    }

    public User(String username, String full_name, String email, String password, String picture_url, int points, String saved_address){
        this. username = username;
        this.full_name = full_name;
        this.email = email;
        this.password = password;
        this.picture_url = picture_url;
        this.points = points;
        this.saved_address = saved_address;

    }

    public User(Long id, String username, String full_name, String email, String password, String picture_url, int points, String saved_address){
        this.id = id;
        this. username = username;
        this.full_name = full_name;
        this.email = email;
        this.password = password;
        this.picture_url = picture_url;
        this.points = points;
        this.saved_address = saved_address;

    }

    public User(String username, String full_name, String email, String password){
        this. username = username;
        this.full_name = full_name;
        this.email = email;
        this.password = password;
    }

}
