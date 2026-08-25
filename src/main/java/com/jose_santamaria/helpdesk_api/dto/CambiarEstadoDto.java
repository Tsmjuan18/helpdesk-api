package com.jose_santamaria.helpdesk_api.dto;

import com.jose_santamaria.helpdesk_api.Enum.Estado;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CambiarEstadoDto {
    
    @NotNull
    private Estado estado;
}
