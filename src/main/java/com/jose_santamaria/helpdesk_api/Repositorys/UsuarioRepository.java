package com.jose_santamaria.helpdesk_api.repositorys;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.jose_santamaria.helpdesk_api.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario,Long>{

    Optional<Usuario> findByEmail(String email);


} 
