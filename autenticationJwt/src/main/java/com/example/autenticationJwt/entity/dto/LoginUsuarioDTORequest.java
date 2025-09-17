package com.example.autenticationJwt.entity.dto;

public record LoginUsuarioDTORequest(

        String email,
        String password

) {
}