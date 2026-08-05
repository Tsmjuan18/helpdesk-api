package com.jose_santamaria.helpdesk_api.dto;
import com.jose_santamaria.helpdesk_api.Enum.Prioridad;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TicketRequestDto {

    @NotBlank
    private String titulo;

    @NotBlank    
    private String descripcion;

   
    @NotNull
    private Prioridad prioridad;

    
}
