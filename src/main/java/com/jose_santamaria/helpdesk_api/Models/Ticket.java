package com.jose_santamaria.helpdesk_api.models;

import java.time.LocalDateTime;

import com.jose_santamaria.helpdesk_api.Enum.Estado;
import com.jose_santamaria.helpdesk_api.Enum.Prioridad;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
 
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

     @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String descripcion;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Prioridad prioridad;

    
    @Enumerated(EnumType.STRING)
    private Estado estado= Estado.ABIERTO;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "creado_por_id" , nullable = false)
    private Usuario creadoPor;

    @Column(nullable = false)
    private LocalDateTime creadoEn;

    @Column(nullable = false)
    private LocalDateTime slaVenceEn;

    public Ticket(){
        this.creadoEn = LocalDateTime.now();
    }
    

    
}
