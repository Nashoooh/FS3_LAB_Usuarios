package com.ignacio.fs3_lab_usuarios.repository;

import com.ignacio.fs3_lab_usuarios.model.Prevision;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrevisionRepository extends JpaRepository<Prevision, Integer> {
    Prevision findByNombre(String nombre);
}
