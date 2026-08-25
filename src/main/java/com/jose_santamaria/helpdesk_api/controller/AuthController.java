package com.jose_santamaria.helpdesk_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jose_santamaria.helpdesk_api.dto.AuthResponseDto;
import com.jose_santamaria.helpdesk_api.dto.LoginRequestDto;
import com.jose_santamaria.helpdesk_api.dto.LogoutRequestDto;
import com.jose_santamaria.helpdesk_api.dto.RefreshTokenRequestDto;
import com.jose_santamaria.helpdesk_api.dto.UsuarioRequestDto;
import com.jose_santamaria.helpdesk_api.dto.UsuarioResponseDto;
import com.jose_santamaria.helpdesk_api.service.AuthService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private  AuthService authService;

    // inyeccion de dependencias 
    public AuthController(AuthService authService){

        this.authService= authService;
    }

    //metodo para registrar usuario
    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponseDto> registro(@Valid @RequestBody UsuarioRequestDto dto){

        UsuarioResponseDto response = authService.registrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
        .body(response);
    }

    //metodo para el login usuario
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> login(@Valid @RequestBody LoginRequestDto dto){

        AuthResponseDto res = authService.login(dto);
        return ResponseEntity.ok(res);
    }

    //metodo para refrescar token
    @PostMapping("/refresh")
    public ResponseEntity<String> refrescar(@Valid @RequestBody RefreshTokenRequestDto dto){

        String nuevoAccesoToken = authService.refrescarToken(dto);
        return ResponseEntity.ok(nuevoAccesoToken);
    }

    //metodo para cerrar sesion
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody LogoutRequestDto dto){

        String response = authService.logout(dto);
        return ResponseEntity.ok(response);
    }





    
}
