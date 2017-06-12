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
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author joshmr94
 */
@Entity
@Table(name = "libro")
public class Libro extends BaseEntity implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    
    @Column(name = "titulo", nullable = false, length = 255)
    private String titulo;
    
    @Column(name = "categoria", nullable = false, length = 255)
    private String categoria;
    
    @Column(name = "genero", nullable = false)
    private String genero;
    
    @Column(name = "ISBN", nullable = false)
    private String ISBN;
    
    @Column(name = "editorial", nullable = true)
    private String editorial;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_publicacion", nullable = true)
    private Date fechaPublicacion;
    
    @ManyToMany(mappedBy = "libros") //mapeada por atributo libros en Usuario
    private List<Usuario> usuarios;
    
    @ManyToMany(mappedBy = "libros") //mapeada por atributo libros en Autor
    private List<Autor> autores;

    public Libro(Long id, String titulo, String categoria, String genero, 
            String ISBN, String editorial, Date fechaPublicacion, 
            List<Autor> autores) {
        this.id = id;
        this.titulo = titulo;
        this.categoria = categoria;
        this.genero = genero;
        this.ISBN = ISBN;
        this.editorial = editorial;
        this.fechaPublicacion = fechaPublicacion;
        this.autores = autores;
    }
    
    public Libro(){}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public Date getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Date fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }
    
}
