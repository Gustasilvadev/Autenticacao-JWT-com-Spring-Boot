package com.example.autenticationJwt.entity.dto;

import java.time.LocalDate;

public record CriarUsuarioDTORequest(

        String nome,
        String email,
        String password,
        LocalDate dataNascimento,
        String role

) {



}