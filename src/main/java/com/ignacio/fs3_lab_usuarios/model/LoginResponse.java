package com.ignacio.fs3_lab_usuarios.model;

public class LoginResponse {
    private String token;
    private String tipo = "Bearer";
    private Integer id;
    private String email;
    private String nombre;
    private String rut;
    private Integer rolId;
    private String rolNombre;

    public LoginResponse(String token, Integer id, String email, String nombre, String rut, Integer rolId, String rolNombre) {
        this.token = token;
        this.id = id;
        this.email = email;
        this.nombre = nombre;
        this.rut = rut;
        this.rolId = rolId;
        this.rolNombre = rolNombre;
    }

    // Getters y setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }

    public Integer getRolId() { return rolId; }
    public void setRolId(Integer rolId) { this.rolId = rolId; }

    public String getRolNombre() { return rolNombre; }
    public void setRolNombre(String rolNombre) { this.rolNombre = rolNombre; }
}
