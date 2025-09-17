package com.example.autenticationJwt.config;

import com.example.autenticationJwt.entity.Usuario;
import com.example.autenticationJwt.repository.UsuarioRepository;
import com.example.autenticationJwt.service.JwtTokenService;
import com.example.autenticationJwt.service.UserDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
public class UsuarioAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenService jwtTokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Só processa autenticação se não for endpoint público
        if (!isEndpointPublico(request)) {
            String token = recoveryToken(request);
            if (token != null) {
                String subject = jwtTokenService.getSubjectFromToken(token);
                Usuario usuario = usuarioRepository.findByEmail(subject)
                        .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
                UserDetailsImpl userDetails = new UserDetailsImpl(usuario);

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(), null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                throw new RuntimeException("O token está ausente.");
            }
        }
        filterChain.doFilter(request, response);
    }

    private String recoveryToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }

    private boolean isEndpointPublico(HttpServletRequest request) {
        // Pega a lista de endpoints públicos da SecurityConfiguration
        String requestURI = request.getRequestURI();

        // Verifica se é endpoint da aplicação
        for (String endpoint : SecurityConfiguration.ENDPOINTS_SEM_AUTENTICACAO) {
            if (requestURI.equals(endpoint)) {
                return true;
            }
        }

        // Verifica se é endpoint do Swagger
        for (String endpoint : SecurityConfiguration.SWAGGER_ENDPOINTS) {
            if (requestURI.startsWith(endpoint.replace("/**", ""))) {
                return true;
            }
        }

        return false;
    }
}