package com.example.autenticationJwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private UsuarioAuthenticationFilter usuarioAuthenticationFilter;

    // Endpoints do swagger
    public static final String[] SWAGGER_ENDPOINTS = {
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-ui",
            "/swagger-ui/**",
            "/swagger-ui.html",
    };

    // Endpoints que não requerem autenticação
    public static final String[] ENDPOINTS_SEM_AUTENTICACAO = {
            "/usuario/autenticar",
            "/usuario/criar"
    };

    // Endpoints que requerem autenticação básica
    public static final String[] ENDPOINTS_AUTENTICADOS = {
            "/usuario/teste/autenticacao"
    };

    // Endpoints exclusivos para clientes
    public static final String[] ENDPOINTS_CLIENTE = {
            "/usuario/teste/cliente"
    };

    // Endpoints exclusivos para administradores
    public static final String[] ENDPOINTS_ADMINISTRADOR = {
            "/usuario/teste/administrador"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(SWAGGER_ENDPOINTS).permitAll()
                        .requestMatchers(ENDPOINTS_SEM_AUTENTICACAO).permitAll()
                        .requestMatchers(ENDPOINTS_ADMINISTRADOR).hasRole("ADMINISTRADOR")
                        .requestMatchers(ENDPOINTS_CLIENTE).hasRole("USUARIO")
                        .requestMatchers(ENDPOINTS_AUTENTICADOS).authenticated()
                        .anyRequest().denyAll()
                )
                .addFilterBefore(usuarioAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }



    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}