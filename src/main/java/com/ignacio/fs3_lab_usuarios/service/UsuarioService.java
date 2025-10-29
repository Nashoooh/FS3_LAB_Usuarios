package com.ignacio.fs3_lab_usuarios.service;

import com.ignacio.fs3_lab_usuarios.model.Usuario;
import com.ignacio.fs3_lab_usuarios.repository.UsuarioRepository;
import com.ignacio.fs3_lab_usuarios.repository.PrevisionRepository;
import com.ignacio.fs3_lab_usuarios.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PrevisionRepository previsionRepository;
    @Autowired
    private RolRepository rolRepository;

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> getUsuarioById(Integer id) {
        return usuarioRepository.findById(id);
    }

    public Usuario saveUsuario(Usuario usuario) {
        if (usuario.getPrevision() != null && usuario.getPrevision().getId() != null) {
            usuario.setPrevision(previsionRepository.findById(usuario.getPrevision().getId()).orElse(null));
        }
        if (usuario.getRol() != null && usuario.getRol().getId() != null) {
            usuario.setRol(rolRepository.findById(usuario.getRol().getId()).orElse(null));
        }
        // Validación de rut único
        Optional<Usuario> existenteRut = usuarioRepository.findByRut(usuario.getRut());
        if (existenteRut.isPresent() && (usuario.getId() == null || !existenteRut.get().getId().equals(usuario.getId()))) {
            throw new RuntimeException("El rut ya está registrado");
        }
        // Validación de email único
        Optional<Usuario> existenteEmail = usuarioRepository.findByEmail(usuario.getEmail());
        if (existenteEmail.isPresent() && (usuario.getId() == null || !existenteEmail.get().getId().equals(usuario.getId()))) {
            throw new RuntimeException("El email ya está registrado");
        }
        return usuarioRepository.save(usuario);
    }

    public void deleteUsuario(Integer id) {
        usuarioRepository.deleteById(id);
    }

    public Optional<Usuario> login(String email, String password) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        if (usuario.isPresent() && usuario.get().getPassword().equals(password)) {
            return usuario;
        }
        return Optional.empty();
    }
}
