package com.ignacio.fs3_lab_usuarios.controller;

import com.ignacio.fs3_lab_usuarios.model.Usuario;
import com.ignacio.fs3_lab_usuarios.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public List<Usuario> getAllUsuarios() {
        return usuarioService.getAllUsuarios();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> getUsuarioById(@PathVariable Integer id) {
        Optional<Usuario> usuario = usuarioService.getUsuarioById(id);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Usuario createUsuario(@RequestBody Usuario usuario) {
        return usuarioService.saveUsuario(usuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> updateUsuario(@PathVariable Integer id, @RequestBody Usuario usuario) {
        Optional<Usuario> usuarioExistente = usuarioService.getUsuarioById(id);
        if (usuarioExistente.isPresent()) {
            usuario.setId(id);
            return ResponseEntity.ok(usuarioService.saveUsuario(usuario));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Integer id) {
        usuarioService.deleteUsuario(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestParam String email, @RequestParam String password) {
        Optional<Usuario> usuario = usuarioService.login(email, password);
        return usuario.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.status(401).build());
    }
}
