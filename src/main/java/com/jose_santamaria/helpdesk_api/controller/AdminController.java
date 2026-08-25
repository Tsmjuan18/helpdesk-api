package com.jose_santamaria.helpdesk_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jose_santamaria.helpdesk_api.dto.AscenderUsuarioDto;
import com.jose_santamaria.helpdesk_api.dto.UsuarioResponseDto;
import com.jose_santamaria.helpdesk_api.service.AdminService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/soporte")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UsuarioResponseDto> ascenderASoporte(@Valid @RequestBody AscenderUsuarioDto dto) {
        return ResponseEntity.ok(adminService.ascenderASoporte(dto));
    }


    
}
