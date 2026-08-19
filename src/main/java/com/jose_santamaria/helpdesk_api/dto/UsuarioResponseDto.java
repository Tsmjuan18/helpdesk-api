package com.jose_santamaria.helpdesk_api.dto;

import com.jose_santamaria.helpdesk_api.Enum.Rol;
import com.jose_santamaria.helpdesk_api.models.Usuario;

import lombok.Data;

@Data
public class UsuarioResponseDto {

    private Long id;

    private String nombre;

     
    private String email;


    private  Rol rol;

    public static UsuarioResponseDto desdeEntidad(Usuario usuario){

        UsuarioResponseDto dto = new UsuarioResponseDto();

        dto.setId(usuario.getId());
        dto.setNombre(usuario.getNombre());
        dto.setEmail(usuario.getEmail());
        dto.setRol(usuario.getRol());

        return dto;     

        


    }


    
}
