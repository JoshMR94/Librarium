package com.joshmr94.librarium.model;

import com.joshmr94.librarium.rbac.model.BaseEntity;
import com.joshmr94.librarium.rbac.model.Rol;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

/**
 *
 * @author joshmr94
 */
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Usuario extends BaseEntity implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "username", nullable = false, length = 255, unique = true)
    private String username;
    
    @Column(name = "password", nullable = false, length = 255)
    private String password;
    
    @Column(name = "descripcion", nullable = true, length = 255)
    private String descripcion;
    
    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Rol> roles = new HashSet<>();
    
    @ManyToMany
    @JoinTable(
            name="libros_usuario",
            joinColumns=@JoinColumn(name = "usuario_id", referencedColumnName = "id"),
            inverseJoinColumns =@JoinColumn(name="libro_id", referencedColumnName = "id")
    )
    private List<Libro> libros;
    
    @Column(nullable = true)
    @Enumerated(EnumType.ORDINAL)
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
        this.password = encryptAndEncodePassword(password);;
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

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }
    
    public static String encryptAndEncodePassword(String pwd) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return Base64.getEncoder().encodeToString(md.digest(pwd.getBytes("utf-8")));
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }      
}
