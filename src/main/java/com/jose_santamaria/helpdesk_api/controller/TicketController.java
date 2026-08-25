package com.jose_santamaria.helpdesk_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jose_santamaria.helpdesk_api.dto.CambiarEstadoDto;
import com.jose_santamaria.helpdesk_api.dto.TicketRequestDto;
import com.jose_santamaria.helpdesk_api.dto.TicketResponseDto;
import com.jose_santamaria.helpdesk_api.service.TicketService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;
    //Inyeccion de dependencias
    public TicketController(TicketService ticketService){

        this.ticketService= ticketService;
    }
    //metodo para crear ticket
    @PostMapping 
    public ResponseEntity<TicketResponseDto> crearTicket(@Valid @RequestBody TicketRequestDto body){

        TicketResponseDto response = ticketService.crearTicket(body);

        return ResponseEntity.status(HttpStatus.CREATED)
        .body(response);
        
    }

    @GetMapping("/mios")
    public ResponseEntity<List<TicketResponseDto>> listarMios() {
        return ResponseEntity.ok(ticketService.listarMios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDto> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(ticketService.obtenerPorId(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('SOPORTE') or hasRole('ADMIN')")
    public ResponseEntity<List<TicketResponseDto>> listarTodos() {
        return ResponseEntity.ok(ticketService.listarTodos());
    }

    @PatchMapping("/{id}/estado")
    @PreAuthorize("hasRole('SOPORTE') or hasRole('ADMIN')")
    public ResponseEntity<TicketResponseDto> cambiarEstado(@PathVariable Long id, @Valid @RequestBody CambiarEstadoDto dto) {
        return ResponseEntity.ok(ticketService.cambiarEstado(id, dto));
    }

    @GetMapping("/vencidos")
    @PreAuthorize("hasRole('SOPORTE') or hasRole('ADMIN')")
    public ResponseEntity<List<TicketResponseDto>> listarVencidos() {
        return ResponseEntity.ok(ticketService.listarVencidos());
    }


    
}
