package com.jose_santamaria.helpdesk_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDto {


    private String accessToken;

    private String refreshToken;
    
}
