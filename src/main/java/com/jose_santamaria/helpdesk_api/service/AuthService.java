package com.jose_santamaria.helpdesk_api.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jose_santamaria.helpdesk_api.Enum.Rol;
import com.jose_santamaria.helpdesk_api.dto.UsuarioRequestDto;
import com.jose_santamaria.helpdesk_api.dto.UsuarioResponseDto;
import com.jose_santamaria.helpdesk_api.exceptions.RecursoDuplicadoException;
import com.jose_santamaria.helpdesk_api.models.Usuario;
import com.jose_santamaria.helpdesk_api.repositorys.UsuarioRepository;

@Service
public class AuthService {

    private UsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder;

    public AuthService(UsuarioRepository usuarioRepository,PasswordEncoder passwordEncoder){

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UsuarioResponseDto registrar(UsuarioRequestDto dto){

        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(dto.getEmail());
        
            if (usuarioExistente.isPresent()) {
                
                throw new RecursoDuplicadoException("Ya existe un usuario con ese email");
            }

            Usuario usuario = new Usuario();
            
            usuario.setEmail(dto.getEmail());
            usuario.setNombre(dto.getNombre());
            usuario.setRol(Rol.USUARIO);
            usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
            Usuario usuarioGuardado= usuarioRepository.save(usuario);
            

            
            return UsuarioResponseDto.desdeEntidad(usuarioGuardado);         
        


    }
    
}
