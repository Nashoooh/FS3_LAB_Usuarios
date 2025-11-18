package com.ignacio.fs3_lab_usuarios.controller;

import com.ignacio.fs3_lab_usuarios.model.LoginResponse;
import com.ignacio.fs3_lab_usuarios.model.Usuario;
import com.ignacio.fs3_lab_usuarios.service.UsuarioService;
import com.ignacio.fs3_lab_usuarios.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllUsuarios() {
        List<Usuario> usuarios = usuarioService.getAllUsuarios();
        List<Map<String, Object>> response = usuarios.stream().map(this::usuarioSinPassword).toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getUsuarioById(@PathVariable Integer id) {
        Optional<Usuario> usuario = usuarioService.getUsuarioById(id);
        return usuario.map(user -> ResponseEntity.ok(usuarioSinPassword(user)))
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createUsuario(@RequestBody Usuario usuario) {
        Usuario savedUser = usuarioService.saveUsuario(usuario);
        return ResponseEntity.ok(usuarioSinPassword(savedUser));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateUsuario(@PathVariable Integer id, @RequestBody Usuario usuario) {
        Optional<Usuario> usuarioExistente = usuarioService.getUsuarioById(id);
        if (usuarioExistente.isPresent()) {
            usuario.setId(id);
            Usuario updatedUser = usuarioService.saveUsuario(usuario);
            return ResponseEntity.ok(usuarioSinPassword(updatedUser));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    private Map<String, Object> usuarioSinPassword(Usuario usuario) {
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", usuario.getId());
        userMap.put("nombre", usuario.getNombre());
        userMap.put("rut", usuario.getRut());
        userMap.put("fechaNacimiento", usuario.getFechaNacimiento());
        userMap.put("email", usuario.getEmail());
        userMap.put("activo", usuario.getActivo());
        userMap.put("prevision", usuario.getPrevision());
        userMap.put("rol", usuario.getRol());
        return userMap;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUsuario(@PathVariable Integer id) {
        Optional<Usuario> usuario = usuarioService.getUsuarioById(id);
        Map<String, String> response = new HashMap<>();
        if (usuario.isPresent()) {
            usuarioService.deleteUsuario(id);
            response.put("mensaje", "Usuario eliminado correctamente");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Usuario no encontrado");
            return ResponseEntity.status(404).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody com.ignacio.fs3_lab_usuarios.model.LoginRequest loginRequest) {
        Optional<Usuario> usuario = usuarioService.login(loginRequest.getEmail(), loginRequest.getPassword());
        if (usuario.isPresent()) {
            Usuario user = usuario.get();
            String token = jwtUtil.generateToken(
                user.getId(),
                user.getEmail(),
                user.getNombre(),
                user.getRut(),
                user.getRol().getId(),
                user.getRol().getNombre()
            );
            
            LoginResponse response = new LoginResponse(
                token,
                user.getId(),
                user.getEmail(),
                user.getNombre(),
                user.getRut(),
                user.getRol().getId(),
                user.getRol().getNombre()
            );
            
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Credenciales inválidas");
            return ResponseEntity.status(401).body(error);
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            if (!jwtUtil.isTokenValid(token)) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Token inválido o expirado");
                return ResponseEntity.status(401).body(error);
            }
            
            Integer userId = jwtUtil.extractUserId(token);
            Optional<Usuario> usuario = usuarioService.getUsuarioById(userId);
            
            if (usuario.isPresent()) {
                Usuario user = usuario.get();
                Map<String, Object> response = new HashMap<>();
                response.put("id", user.getId());
                response.put("nombre", user.getNombre());
                response.put("rut", user.getRut());
                response.put("email", user.getEmail());
                response.put("fechaNacimiento", user.getFechaNacimiento());
                response.put("activo", user.getActivo());
                response.put("rol", user.getRol());
                response.put("prevision", user.getPrevision());
                
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Usuario no encontrado");
                return ResponseEntity.status(404).body(error);
            }
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Token inválido");
            return ResponseEntity.status(401).body(error);
        }
    }
    
    @PostMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(@RequestBody com.ignacio.fs3_lab_usuarios.model.ChangePasswordRequest request) {
        Map<String, String> response = new HashMap<>();
        
        if (request.getEmail() == null || request.getEmail().isEmpty()) {
            response.put("error", "El email es requerido");
            return ResponseEntity.status(400).body(response);
        }
        
        if (request.getNewPassword() == null || request.getNewPassword().isEmpty()) {
            response.put("error", "La nueva contraseña es requerida");
            return ResponseEntity.status(400).body(response);
        }
        
        boolean success = usuarioService.changePassword(request.getEmail(), request.getNewPassword());
        
        if (success) {
            response.put("mensaje", "Contraseña actualizada correctamente");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Usuario no encontrado con ese email");
            return ResponseEntity.status(404).body(response);
        }
    }
    
    @PostMapping("/verify-email")
    public ResponseEntity<Map<String, Object>> verifyEmail(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        String email = request.get("email");
        
        if (email == null || email.isEmpty()) {
            response.put("exists", false);
            response.put("error", "El email es requerido");
            return ResponseEntity.status(400).body(response);
        }
        
        Optional<Usuario> usuario = usuarioService.findByEmail(email);
        
        if (usuario.isPresent()) {
            response.put("exists", true);
            response.put("email", email);
            return ResponseEntity.ok(response);
        } else {
            response.put("exists", false);
            return ResponseEntity.status(404).body(response);
        }
    }
}
