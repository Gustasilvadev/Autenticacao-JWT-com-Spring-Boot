package com.example.autenticationJwt.entity.dto;


import com.example.autenticationJwt.entity.Role;

import java.util.List;

public record UsuarioDTOResponse(

        Long id,
        String email,
        List<Role> roles

) {
}