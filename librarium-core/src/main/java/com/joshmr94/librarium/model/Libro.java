package com.joshmr94.librarium.model;

/**
 *
 * @author joshmr94
 */
public class Libro {
    
    private Long id;
    private String titulo;
    private String autor;
    private String categoria;
    private String genero;
    private String ISBN;
    private String editorial;

    public Libro(Long id, String titulo, String autor, String categoria, String genero, String ISBN, String editorial) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
        this.categoria = categoria;
        this.genero = genero;
        this.ISBN = ISBN;
        this.editorial = editorial;
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

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
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
    
    
    
}
