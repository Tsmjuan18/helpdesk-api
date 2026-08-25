package com.jose_santamaria.helpdesk_api.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.jose_santamaria.helpdesk_api.repositorys.UsuarioRepository;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

     private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    public JwtAuthenticationFilter(JwtService jwtService, UsuarioRepository usuarioRepository) {
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                     @NonNull HttpServletResponse response,
                                     @NonNull FilterChain filterChain) throws ServletException, IOException {

        /*String authHeader = request.getHeader("Authorization");
        System.out.println("========== JWT FILTER ==========");
        System.out.println("AUTH HEADER: " + authHeader);

        // Si no hay header, o no viene en formato "Bearer <token>", dejamos pasar sin autenticar
        // (la ruta será rechazada más adelante si de verdad requería autenticación)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7); // le quita "Bearer " (7 caracteres)
        System.out.println("TOKEN: " + token);
        System.out.println("TOKEN VALIDO: " + jwtService.esTokenValido(token)); 

        if (jwtService.esTokenValido(token)) {
            String email = jwtService.extraerEmail(token);

            usuarioRepository.findByEmail(email).ifPresent(usuario -> {
                List<GrantedAuthority> authorities = List.of(
                        new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name())
                );

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(usuario, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authToken);
            });
        }*/

            String authHeader = request.getHeader("Authorization");

System.out.println("========== JWT FILTER ==========");
System.out.println("AUTH HEADER: " + authHeader);

if (authHeader == null || !authHeader.startsWith("Bearer ")) {
    System.out.println("NO LLEGO BEARER");
    filterChain.doFilter(request, response);
    return;
}

String token = authHeader.substring(7);

System.out.println("TOKEN: " + token);
System.out.println("TOKEN VALIDO: " + jwtService.esTokenValido(token));

if (jwtService.esTokenValido(token)) {

    String email = jwtService.extraerEmail(token);

    System.out.println("EMAIL DEL TOKEN: " + email);

    usuarioRepository.findByEmail(email).ifPresentOrElse(usuario -> {

        System.out.println("USUARIO ENCONTRADO: " + usuario.getEmail());
        System.out.println("ROL: " + usuario.getRol());

        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + usuario.getRol().name())
        );

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                        usuario,
                        null,
                        authorities
                );

        SecurityContextHolder.getContext().setAuthentication(authToken);

        System.out.println("AUTENTICACION CREADA");

    }, () -> {
        System.out.println("USUARIO NO ENCONTRADO");
    });

} else {
    System.out.println("TOKEN INVALIDO");
}

        filterChain.doFilter(request, response);
    }


    
}
