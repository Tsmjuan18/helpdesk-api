package com.jose_santamaria.helpdesk_api.dto;

import java.time.LocalDateTime;

import com.jose_santamaria.helpdesk_api.Enum.Estado;
import com.jose_santamaria.helpdesk_api.Enum.Prioridad;
import com.jose_santamaria.helpdesk_api.models.Ticket;

import lombok.Data;

@Data
public class TicketResponseDto {

    private Long id;

    private String nombreCreador;

    private String titulo;
     
    private String descripcion;   

    private Prioridad prioridad;

    private Estado estado;

    
    private LocalDateTime slaVenceEn;
    
    private Boolean isVencido;

    public static TicketResponseDto desdeEntidad(Ticket ticket, Boolean vencido){

        TicketResponseDto dto= new TicketResponseDto();
        dto.setId(ticket.getId());
        dto.setNombreCreador(ticket.getCreadoPor().getNombre());
        dto.setTitulo(ticket.getTitulo());
        dto.setDescripcion(ticket.getDescripcion());
        dto.setPrioridad(ticket.getPrioridad());
        dto.setEstado(ticket.getEstado());
        dto.setSlaVenceEn(ticket.getSlaVenceEn());
        dto.setIsVencido(vencido);


        
        
        return dto;

        
    }


    
}
