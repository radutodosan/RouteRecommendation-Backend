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
public class User implements Comparable<User>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @Column(nullable = false, unique = true)
    @JsonProperty("username")
    private String username;

    @Column(nullable = false)
    @JsonProperty("full_name")
    private String fullName;

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
    private int points = 125 ;

    public User(){

    }

    public User(String username, String full_name, String email, String password, String picture_url, int points){
        this. username = username;
        this.fullName = full_name;
        this.email = email;
        this.password = password;
        this.picture_url = picture_url;
        this.points = points;

    }

    public User(Long id, String username, String full_name, String email, String password, String picture_url, int points){
        this.id = id;
        this. username = username;
        this.fullName = full_name;
        this.email = email;
        this.password = password;
        this.picture_url = picture_url;
        this.points = points;

    }

    public User(String username, String full_name, String email, String password){
        this. username = username;
        this.fullName = full_name;
        this.email = email;
        this.password = password;
    }

    @Override
    public int compareTo(User u) {
        return getPoints() - u.getPoints();
    }
}
