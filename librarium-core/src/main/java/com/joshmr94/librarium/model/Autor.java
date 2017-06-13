package com.joshmr94.librarium.model;

import com.joshmr94.librarium.rbac.model.BaseEntity;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author joshmr94
 */
@Entity
@Table(name = "autor")
public class Autor extends BaseEntity implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    
    @Column(name = "nombre", nullable = false, length = 255)
    private String nombre;
    
    @Column(name = "apellido", nullable = false, length = 255)
    private String apellido;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_nacimiento", nullable = true)
    private Date fechaNacimiento;
    
    @Column(name = "descripcion", nullable = false, length = 255)
    private String descripcion;
    
    @ManyToMany
    @JoinTable(
            name="libros_autores",
            joinColumns=@JoinColumn(name = "autor_id", referencedColumnName = "id"),
            inverseJoinColumns =@JoinColumn(name="libro_id", referencedColumnName = "id")
    )
    private List<Libro> libros;

    public Autor(Long id, String nombre, String apellido, Date fechaNacimiento, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.fechaNacimiento = fechaNacimiento;
        this.descripcion = descripcion;
    }
    
    public Autor(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    
}
