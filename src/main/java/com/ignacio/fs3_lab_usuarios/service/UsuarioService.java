package com.ignacio.fs3_lab_usuarios.service;

import com.ignacio.fs3_lab_usuarios.model.Usuario;
import com.ignacio.fs3_lab_usuarios.repository.UsuarioRepository;
import com.ignacio.fs3_lab_usuarios.repository.PrevisionRepository;
import com.ignacio.fs3_lab_usuarios.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Autowired
    private PasswordEncoder passwordEncoder;

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
        
        // Si es una actualización y la contraseña es null, mantener la contraseña existente
        if (usuario.getId() != null && usuario.getPassword() == null) {
            Optional<Usuario> usuarioExistente = usuarioRepository.findById(usuario.getId());
            if (usuarioExistente.isPresent()) {
                usuario.setPassword(usuarioExistente.get().getPassword());
            }
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
        // Encriptar contraseña si es un usuario nuevo o si la contraseña cambió
        if (usuario.getPassword() != null && (usuario.getId() == null || !usuario.getPassword().startsWith("$2a$"))) {
            usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }
        return usuarioRepository.save(usuario);
    }

    public void deleteUsuario(Integer id) {
        usuarioRepository.deleteById(id);
    }

    public Optional<Usuario> login(String email, String password) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        if (usuario.isPresent() && passwordEncoder.matches(password, usuario.get().getPassword())) {
            return usuario;
        }
        return Optional.empty();
    }
    
    public boolean changePassword(String email, String newPassword) {
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
        if (usuario.isPresent()) {
            Usuario user = usuario.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            usuarioRepository.save(user);
            return true;
        }
        return false;
    }
    
    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }
}
