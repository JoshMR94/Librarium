package com.joshmr94.librarium.model;

import com.joshmr94.librarium.rbac.model.Rol;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author joshmr94
 */
public class Usuario {
    
    private Long id;
    private String username;
    private String password;
    private String descripcion;
    private Set<Rol> roles = new HashSet<>();
    
    private TipoUsuario tipoUsuario;
    public enum TipoUsuario {
        ADMIN,
        EDITOR,
        USUARIO;
    }

    public Usuario(Long id, String username, String password, String descripcion, TipoUsuario tipoUsuario) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.descripcion = descripcion;
        this.tipoUsuario = tipoUsuario;
    }
    
    public Usuario(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Set<Rol> getRoles() {
        return roles;
    }

    public void setRoles(Set<Rol> roles) {
        this.roles = roles;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }
    
    
}
