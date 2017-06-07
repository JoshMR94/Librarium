package com.joshmr94.librarium.rbac.model;

import com.joshmr94.librarium.rbac.model.BaseEntity;
import com.joshmr94.librarium.rbac.model.IEntityDescripcion;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author jmoran
 */

@Entity
public class Recurso extends BaseEntity implements IEntityDescripcion {

    /**
     * ID del recurso.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del recurso.
     */
    @Column(length = 255, nullable = false, unique = true)
    private String descripcion;
    
    @OneToMany(mappedBy = "recurso", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    List<Operacion> operaciones = new ArrayList<>();

    /**
     * Constructor por defecto.
     */
    public Recurso() {
    }

    public Recurso(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene el ID.
     *
     * @return Returns the id.
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * Actualiza el ID.
     *
     * @param pId The id to set.
     */
    @Override
    public void setId(final Long pId) {
        this.id = pId;
    }

    /**
     * Obtiene el nombre del recurso.
     *
     * @return Nombre del recurso
     */
    @Override
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Actualiza el nombre del recurso.
     *
     * @param pDescripcion Nombre del recurso
     */
    @Override
    public void setDescripcion(final String pDescripcion) {
        this.descripcion = pDescripcion;
    }

    public List<Operacion> getOperaciones() {
        return operaciones;
    }

    public void setOperaciones(List<Operacion> operaciones) {
        this.operaciones = operaciones;
    }

}
