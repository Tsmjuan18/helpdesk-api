package com.jose_santamaria.helpdesk_api.repositorys;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jose_santamaria.helpdesk_api.models.Ticket;
import com.jose_santamaria.helpdesk_api.models.Usuario;

@Repository
public interface TicketRepository  extends JpaRepository<Ticket,Long>{
    

    List<Ticket> findByCreadoPor (Usuario usuario);
}
