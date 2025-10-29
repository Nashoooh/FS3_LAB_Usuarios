package com.ignacio.fs3_lab_usuarios.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String rut;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private Boolean activo = true;

    @ManyToOne
    @JoinColumn(name = "prevision_id")
    private Prevision prevision;

    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Rol rol;

    // Getters y setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }

    public Date getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(Date fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public Prevision getPrevision() { return prevision; }
    public void setPrevision(Prevision prevision) { this.prevision = prevision; }

    public Rol getRol() { return rol; }
    public void setRol(Rol rol) { this.rol = rol; }
}
