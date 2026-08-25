package com.jose_santamaria.helpdesk_api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.jose_santamaria.helpdesk_api.Enum.Estado;
import com.jose_santamaria.helpdesk_api.Enum.Rol;
import com.jose_santamaria.helpdesk_api.dto.CambiarEstadoDto;
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

       Usuario usuarioActual = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

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
    

    public List<TicketResponseDto> listarMios() {
        Usuario usuarioActual = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return ticketRepository.findByCreadoPor(usuarioActual).stream()
                .map(this::mapearConVencido)
                .toList();
    }

    public TicketResponseDto obtenerPorId(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ticket no encontrado"));

        Usuario usuarioActual = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean esDueno = ticket.getCreadoPor().getId().equals(usuarioActual.getId());
        boolean esSoporteOAdmin = usuarioActual.getRol() == Rol.SOPORTE || usuarioActual.getRol() == Rol.ADMIN;

        if (!esDueno && !esSoporteOAdmin) {
            throw new RecursoNoEncontradoException("Ticket no encontrado"); // no revelamos que existe
        }

        return mapearConVencido(ticket);
    }

    public List<TicketResponseDto> listarTodos() {
        return ticketRepository.findAll().stream()
                .map(this::mapearConVencido)
                .toList();
    }

    public TicketResponseDto cambiarEstado(Long id, CambiarEstadoDto dto) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Ticket no encontrado"));

        ticket.setEstado(dto.getEstado());
        Ticket actualizado = ticketRepository.save(ticket);
        return mapearConVencido(actualizado);
    }

    public List<TicketResponseDto> listarVencidos() {
        return ticketRepository.findByEstadoNotAndSlaVenceEnBefore(Estado.RESUELTO, LocalDateTime.now()).stream()
                .map(this::mapearConVencido)
                .toList();
    }

    // Helper para no repetir el cálculo de "vencido" en cada método
    private TicketResponseDto mapearConVencido(Ticket ticket) {
        boolean vencido = LocalDateTime.now().isAfter(ticket.getSlaVenceEn()) && ticket.getEstado() != Estado.RESUELTO;
        return TicketResponseDto.desdeEntidad(ticket, vencido);
    }
}
