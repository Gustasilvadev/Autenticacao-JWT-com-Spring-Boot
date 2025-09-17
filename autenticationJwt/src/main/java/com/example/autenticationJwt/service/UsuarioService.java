package com.example.autenticationJwt.service;

import com.example.autenticationJwt.config.SecurityConfiguration;
import com.example.autenticationJwt.entity.Role;
import com.example.autenticationJwt.entity.RoleName;
import com.example.autenticationJwt.entity.Usuario;
import com.example.autenticationJwt.entity.dto.CriarUsuarioDTORequest;
import com.example.autenticationJwt.entity.dto.JwtTokenDTOResponse;
import com.example.autenticationJwt.entity.dto.LoginUsuarioDTORequest;
import com.example.autenticationJwt.repository.RoleRepository;
import com.example.autenticationJwt.repository.UsuarioRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;
    private final UsuarioRepository usuarioRepository;
    private final SecurityConfiguration securityConfiguration;
    private final RoleRepository roleRepository;

    public UsuarioService(AuthenticationManager authenticationManager, JwtTokenService jwtTokenService, UsuarioRepository usuarioRepository, SecurityConfiguration securityConfiguration,RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenService = jwtTokenService;
        this.usuarioRepository = usuarioRepository;
        this.securityConfiguration = securityConfiguration;
        this.roleRepository = roleRepository;
    }

    // Metodo responsável por autenticar um usuário e retornar um token JWT
    public JwtTokenDTOResponse autenticarUsuario(LoginUsuarioDTORequest loginUsuarioDTORequest) {
        // Cria um objeto de autenticação com o email e a senha do usuário
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginUsuarioDTORequest.email(), loginUsuarioDTORequest.password());

        // Autentica o usuário com as credenciais fornecidas
        Authentication authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        // Obtém o objeto UserDetails do usuário autenticado
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Gera um token JWT para o usuário autenticado
        return new JwtTokenDTOResponse(jwtTokenService.generateToken(userDetails));
    }

    public void criarUsuario(CriarUsuarioDTORequest criarUsuarioDTORequest) {
        // Verifica se o usuário já existe
        if (usuarioRepository.findByEmail(criarUsuarioDTORequest.email()).isPresent()) {
            throw new RuntimeException("Usuário já existe com este login: " + criarUsuarioDTORequest.email());
        }

        // Converte a string para o enum RoleName
        RoleName roleName;
        try {
            roleName = RoleName.valueOf(criarUsuarioDTORequest.role().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Role inválida: " + criarUsuarioDTORequest.role());
        }

        // Busca a role no banco de dados usando a instância do enum
        Optional<Role> roleOptional = roleRepository.findByName(roleName);

        if (roleOptional.isEmpty()) {
            throw new RuntimeException("Role não encontrada no banco de dados: " + roleName);
        }

        // Cria o novo usuário
        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(criarUsuarioDTORequest.nome());
        novoUsuario.setEmail(criarUsuarioDTORequest.email());
        novoUsuario.setPassword(securityConfiguration.passwordEncoder().encode(criarUsuarioDTORequest.password()));
        novoUsuario.setDataNascimento(criarUsuarioDTORequest.dataNascimento());
        novoUsuario.setStatus(1);

        // Atribui a role encontrada
        novoUsuario.setRoles(List.of(roleOptional.get()));

        usuarioRepository.save(novoUsuario);
    }
}
