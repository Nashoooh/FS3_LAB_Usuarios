package com.ignacio.fs3_lab_usuarios.repository;

import com.ignacio.fs3_lab_usuarios.model.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends JpaRepository<Rol, Integer> {
    Rol findByNombre(String nombre);
}
