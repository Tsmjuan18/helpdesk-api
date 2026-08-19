package com.jose_santamaria.helpdesk_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.jose_santamaria.helpdesk_api.dto.UsuarioRequestDto;
import com.jose_santamaria.helpdesk_api.dto.UsuarioResponseDto;
import com.jose_santamaria.helpdesk_api.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth/")
public class AuthController {

    private  AuthService authService;

    public AuthController(AuthService authService){

        this.authService= authService;


    }

    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponseDto> registro(@Valid @RequestBody UsuarioRequestDto dto){


        UsuarioResponseDto response = authService.registrar(dto);

        return ResponseEntity.status(HttpStatus.CREATED)
        .body(response);       


    }


    
}
