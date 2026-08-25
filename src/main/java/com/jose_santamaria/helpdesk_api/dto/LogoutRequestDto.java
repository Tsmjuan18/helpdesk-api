package com.jose_santamaria.helpdesk_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LogoutRequestDto {

    @NotBlank
    private String refreshToken;
    
}
