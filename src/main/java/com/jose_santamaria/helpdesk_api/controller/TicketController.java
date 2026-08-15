package com.jose_santamaria.helpdesk_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jose_santamaria.helpdesk_api.dto.TicketRequestDto;
import com.jose_santamaria.helpdesk_api.dto.TicketResponseDto;
import com.jose_santamaria.helpdesk_api.service.TicketService;

import jakarta.validation.Valid;
@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService){

        this.ticketService= ticketService;
    }

    @PostMapping 
    public ResponseEntity<TicketResponseDto> crearTicket(@Valid @RequestBody TicketRequestDto body){

        TicketResponseDto response = ticketService.crearTicket(body);

        return ResponseEntity.status(HttpStatus.CREATED)
        .body(response);
        
    }


    
}
