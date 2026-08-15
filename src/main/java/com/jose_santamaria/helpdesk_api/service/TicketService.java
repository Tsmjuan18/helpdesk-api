package com.jose_santamaria.helpdesk_api.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.jose_santamaria.helpdesk_api.Enum.Estado;
import com.jose_santamaria.helpdesk_api.dto.TicketRequestDto;
import com.jose_santamaria.helpdesk_api.dto.TicketResponseDto;
import com.jose_santamaria.helpdesk_api.exceptions.RecursoNoEncontradoException;
import com.jose_santamaria.helpdesk_api.models.Ticket;
import com.jose_santamaria.helpdesk_api.models.Usuario;
import com.jose_santamaria.helpdesk_api.repositorys.TicketRepository;
import com.jose_santamaria.helpdesk_api.repositorys.UsuarioRepository;

@Service
public class TicketService {

    private TicketRepository ticketRepository;
    private UsuarioRepository usuarioRepository;

    public TicketService(TicketRepository ticketRepository, UsuarioRepository usuarioRepository){

        this.ticketRepository= ticketRepository;
        this.usuarioRepository= usuarioRepository;
        
    }

    public TicketResponseDto crearTicket(TicketRequestDto dto){

        Usuario usuarioActual = usuarioRepository.findById(1L)
        .orElseThrow(() -> new RecursoNoEncontradoException("El usuario no existe"));

        Ticket ticket = new Ticket();

        ticket.setTitulo(dto.getTitulo());
        ticket.setDescripcion(dto.getDescripcion());
        ticket.setPrioridad(dto.getPrioridad());
        ticket.setCreadoPor(usuarioActual);

        int horas = switch(dto.getPrioridad()){

            case BAJA -> 72;
            case MEDIA ->  24;
            case ALTA ->  4;

        };

        LocalDateTime slaVenceEn= ticket.getCreadoEn().plusHours(horas);

        ticket.setSlaVenceEn(slaVenceEn);
        

        Ticket guardado =  ticketRepository.save(ticket);   
        Boolean vencido = LocalDateTime.now().isAfter(ticket.getSlaVenceEn()) && ticket.getEstado() != Estado.RESUELTO;
        return TicketResponseDto.desdeEntidad(guardado,vencido);



        





    }
    
}
