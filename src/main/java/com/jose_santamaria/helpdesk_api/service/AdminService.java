package com.jose_santamaria.helpdesk_api.service;

import org.springframework.stereotype.Service;

import com.jose_santamaria.helpdesk_api.Enum.Rol;
import com.jose_santamaria.helpdesk_api.dto.AscenderUsuarioDto;
import com.jose_santamaria.helpdesk_api.dto.UsuarioResponseDto;
import com.jose_santamaria.helpdesk_api.exceptions.RecursoNoEncontradoException;
import com.jose_santamaria.helpdesk_api.models.Usuario;
import com.jose_santamaria.helpdesk_api.repositorys.UsuarioRepository;

@Service
public class AdminService {

     private final UsuarioRepository usuarioRepository;

    public AdminService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public UsuarioResponseDto ascenderASoporte(AscenderUsuarioDto dto) {
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RecursoNoEncontradoException("Usuario no encontrado"));

        usuario.setRol(Rol.SOPORTE);
        Usuario actualizado = usuarioRepository.save(usuario);

        return UsuarioResponseDto.desdeEntidad(actualizado);
    }
    
}
