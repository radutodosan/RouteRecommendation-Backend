package com.routerecommendationbackend.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordDTO {

    private String old_password;
    private String new_password;
}
