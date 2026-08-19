package com.jose_santamaria.helpdesk_api.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioRequestDto {

    @NotBlank
    private String nombre;
    
    @NotBlank    
    @Email
    private String email;

    @NotBlank
    private String password;
    
    public UsuarioRequestDto(){

        
    }


    
}
