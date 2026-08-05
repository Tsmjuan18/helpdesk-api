package com.jose_santamaria.helpdesk_api.dto;

import java.time.LocalDateTime;

import com.jose_santamaria.helpdesk_api.Enum.Estado;
import com.jose_santamaria.helpdesk_api.Enum.Prioridad;
import com.jose_santamaria.helpdesk_api.models.Usuario;


public class TicketResponseDto {

    private Long id;

    private String titulo;
     
    private String descripcion;   

    private Prioridad prioridad;

    private Estado estado;

    private Usuario creadoPor;
    
}
