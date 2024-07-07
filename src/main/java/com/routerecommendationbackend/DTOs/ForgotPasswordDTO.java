package com.routerecommendationbackend.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ForgotPasswordDTO {

    private String username;
    private String tmp_password;
}
