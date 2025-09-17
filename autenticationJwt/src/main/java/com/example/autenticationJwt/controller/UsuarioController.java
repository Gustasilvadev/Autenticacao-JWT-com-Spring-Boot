package com.example.autenticationJwt.controller;

import com.example.autenticationJwt.entity.dto.CriarUsuarioDTORequest;
import com.example.autenticationJwt.entity.dto.JwtTokenDTOResponse;
import com.example.autenticationJwt.entity.dto.LoginUsuarioDTORequest;
import com.example.autenticationJwt.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/autenticar")
    public ResponseEntity<JwtTokenDTOResponse> autenticarUsuario(@RequestBody LoginUsuarioDTORequest loginUsuarioDTORequest) {
        JwtTokenDTOResponse token = usuarioService.autenticarUsuario(loginUsuarioDTORequest);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/criar")
    public ResponseEntity<Void> criarUsuario(@RequestBody CriarUsuarioDTORequest criarUsuarioDTORequest) {
        usuarioService.criarUsuario(criarUsuarioDTORequest);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/teste/autenticacao")
    public ResponseEntity<String> testeAutenticacao() {
        return new ResponseEntity<>("Autenticado com sucesso", HttpStatus.OK);
    }

    @GetMapping("/teste/cliente")
    public ResponseEntity<String> testeAutenticacaoCliente() {
        return new ResponseEntity<>("Cliente autenticado com sucesso", HttpStatus.OK);
    }

    @GetMapping("/teste/administrador")
    public ResponseEntity<String> testeAutenticacaoAdministrador() {
        return new ResponseEntity<>("Administrador autenticado com sucesso", HttpStatus.OK);
    }

}
