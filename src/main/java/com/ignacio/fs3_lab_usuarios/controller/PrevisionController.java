package com.ignacio.fs3_lab_usuarios.controller;

import com.ignacio.fs3_lab_usuarios.model.Prevision;
import com.ignacio.fs3_lab_usuarios.repository.PrevisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/previsiones")
public class PrevisionController {
    @Autowired
    private PrevisionRepository previsionRepository;

    @GetMapping
    public ResponseEntity<List<Prevision>> getAllPrevisiones() {
        List<Prevision> previsiones = previsionRepository.findAll();
        return ResponseEntity.ok(previsiones);
    }
}
