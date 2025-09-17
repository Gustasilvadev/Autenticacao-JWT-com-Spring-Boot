package com.example.autenticationJwt.repository;

import com.example.autenticationJwt.entity.Role;
import com.example.autenticationJwt.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName(RoleName name);
}
