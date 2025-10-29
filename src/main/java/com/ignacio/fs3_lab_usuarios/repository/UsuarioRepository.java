package com.ignacio.fs3_lab_usuarios.repository;

import com.ignacio.fs3_lab_usuarios.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByRut(String rut);
}
