package com.joshmr94.librarium.rbac.model;

import com.joshmr94.librarium.rbac.model.BaseEntity;
import com.joshmr94.librarium.rbac.model.IEntityDescripcion;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

/**
 *
 * @author jmoran
 */
@Entity
public class Rol extends BaseEntity implements IEntityDescripcion {

    /**
     * ID del rol.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del rol.
     */
    @Column(length = 255, nullable = false, unique = true)
    private String descripcion;

    /**
     * Conjunto de subroles. EAGER dados los pocos elementos que contendrá y la
     * facilidad de manejo que añade.
     */
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REFRESH)
    private Set<Rol> subroles = new HashSet<>();
    
    @OneToMany(mappedBy = "rol", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Permiso> permisos = new HashSet<>();

    /**
     * Si creamos el rol en workflow
     */
    @Column
    private Boolean addWorkflow;

    public Rol() {
    }

    public Rol(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Obtiene el ID del rol.
     *
     * @return ID del rol
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * Actualiza el ID del rol.
     *
     * @param pId ID del rol
     */
    @Override
    public void setId(final Long pId) {
        this.id = pId;
    }

    /**
     * Obtiene el nombre del rol.
     *
     * @return Nombre del rol
     */
    @Override
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Actualiza el nombre del rol.
     *
     * @param pDescripcion Nombre del rol
     */
    @Override
    public void setDescripcion(final String pDescripcion) {
        this.descripcion = pDescripcion;
    }

    /**
     * Obtiene el conjunto de subroles.
     *
     * @return Conjunto de subroles
     */
    public Set<Rol> getSubroles() {
        return subroles;
    }

    /**
     * Actualiza el conjunto de subroles.
     *
     * @param pSubroles Conjunto de subroles
     */
    public void setSubroles(final Set<Rol> pSubroles) {
        this.subroles = pSubroles;
    }

    /**
     *
     * @return
     */
    public Boolean getAddWorkflow() {
        return addWorkflow;
    }

    /**
     *
     * @param addWorkflow
     */
    public void setAddWorkflow(Boolean addWorkflow) {
        this.addWorkflow = addWorkflow;
    }

    public Set<Permiso> getPermisos() {
        return permisos;
    }

    public void setPermisos(Set<Permiso> permisos) {
        this.permisos = permisos;
    }

}
