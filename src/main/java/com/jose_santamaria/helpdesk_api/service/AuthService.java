package com.jose_santamaria.helpdesk_api.service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.jose_santamaria.helpdesk_api.Enum.Rol;
import com.jose_santamaria.helpdesk_api.dto.AuthResponseDto;
import com.jose_santamaria.helpdesk_api.dto.LoginRequestDto;
import com.jose_santamaria.helpdesk_api.dto.LogoutRequestDto;
import com.jose_santamaria.helpdesk_api.dto.RefreshTokenRequestDto;
import com.jose_santamaria.helpdesk_api.dto.UsuarioRequestDto;
import com.jose_santamaria.helpdesk_api.dto.UsuarioResponseDto;
import com.jose_santamaria.helpdesk_api.exceptions.CredencialesInvalidasException;
import com.jose_santamaria.helpdesk_api.exceptions.RecursoDuplicadoException;
import com.jose_santamaria.helpdesk_api.exceptions.RecursoNoEncontradoException;
import com.jose_santamaria.helpdesk_api.models.RefreshToken;
import com.jose_santamaria.helpdesk_api.models.Usuario;
import com.jose_santamaria.helpdesk_api.repositorys.RefreshTokenRepository;
import com.jose_santamaria.helpdesk_api.repositorys.UsuarioRepository;
import com.jose_santamaria.helpdesk_api.security.JwtService;

@Service
public class AuthService {

    private UsuarioRepository usuarioRepository;
    private RefreshTokenRepository refreshTokenRepository;
    private PasswordEncoder passwordEncoder;
    private JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository,PasswordEncoder passwordEncoder, JwtService jwtService
                        ,RefreshTokenRepository refreshTokenRepository){

        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService= jwtService;
        this.refreshTokenRepository= refreshTokenRepository;
    }
    // METODO PARA REGISTRAR UN USUARIO
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

    // METODO PARA EL LOG IN
    public AuthResponseDto login(LoginRequestDto dto){

        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(dto.getEmail());

        if (usuarioExistente.isEmpty()) {

            throw new CredencialesInvalidasException("Email o contraseña invalidas");
            
        }
        
        Usuario usuario = usuarioExistente.get();

        boolean coincide= passwordEncoder.matches(dto.getPassword(),usuario.getPassword());
        if (!coincide) {

            throw new CredencialesInvalidasException("Email o contraseña invalidas");            
        }

        
        String tokenDeAcceso= jwtService.generarToken(usuario);

        String valorRefreshToken = UUID.randomUUID().toString();

        RefreshToken refreshToken = new RefreshToken();
        
        refreshToken.setToken(valorRefreshToken);
        refreshToken.setUsuario(usuario);
        refreshToken.setExpiraEn(LocalDateTime.now().plusDays(7));
        refreshTokenRepository.save(refreshToken);

        return  new AuthResponseDto(tokenDeAcceso, valorRefreshToken);       


    }

    //Refrescar token
    public String refrescarToken (RefreshTokenRequestDto dto){

        Optional<RefreshToken> res = refreshTokenRepository.findByToken(dto.getRefreshToken());

        if (res.isEmpty()) {
            throw new CredencialesInvalidasException("Refresh token invalido");            
        }

        RefreshToken refresh = res.get();
        if (refresh.getRevocado()) {
            throw new CredencialesInvalidasException("Refresh Token esta revocado");       
        }

        boolean vencido= LocalDateTime.now().isAfter(refresh.getExpiraEn());
        if (vencido) {
            throw new CredencialesInvalidasException("Refresh Token expiro");           
        }

        String token = jwtService.generarToken(refresh.getUsuario());
        return token;

    }

    //metodo para Logout
    public String logout(LogoutRequestDto dto){

        Optional<RefreshToken> refreshTokenExistente = refreshTokenRepository.findByToken(dto.getRefreshToken());

        if (refreshTokenExistente.isEmpty()) {
            throw new CredencialesInvalidasException("El token no existe");            
        }

        RefreshToken refreshToken = refreshTokenExistente.get();
        refreshToken.setRevocado(true);
        refreshTokenRepository.save(refreshToken);
        return "Sesion_cerrada";
    }
    
}
