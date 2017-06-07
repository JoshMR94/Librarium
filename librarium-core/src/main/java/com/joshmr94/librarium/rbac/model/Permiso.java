package com.joshmr94.librarium.rbac.model;

import com.joshmr94.librarium.rbac.model.BaseEntity;
import com.joshmr94.librarium.rbac.model.IEntity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 *
 * @author jmoran
 */

@Entity
public class Permiso extends BaseEntity implements IEntity {

    /**
     * ID del permiso.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Rol asociado al permiso.
     */
    @ManyToOne(optional = false)
    private Rol rol;

    /**
     * Operación asociada al permiso.
     */
    @ManyToOne(optional = false)
    private Operacion operacion;

    /**
     * Constructor por defecto.
     */
    public Permiso() {
        super();
    }

    /**
     * Constructor en el que se proporciona información.
     *
     * @param pId ID del registro
     * @param pRol Rol asociado al permiso
     * @param pOperacion Operación permitida
     */
    public Permiso(final Long pId, final Rol pRol, final Operacion pOperacion) {
        this();
        this.id = pId;
        this.rol = pRol;
        this.operacion = pOperacion;
    }

    /**
     * Obtiene el ID del registro.
     *
     * @return ID del registro
     */
    @Override
    public Long getId() {
        return id;
    }

    /**
     * Actualiza el ID del registro.
     *
     * @param pId ID del registro
     */
    @Override
    public void setId(final Long pId) {
        this.id = pId;
    }

    /**
     * Obtiene el rol asociado al permiso.
     *
     * @return Rol asociado al permiso
     */
    public Rol getRol() {
        return rol;
    }

    /**
     * Actualiza el rol asociado al permiso.
     *
     * @param pRol Rol asociado al permiso
     */
    public void setRol(final Rol pRol) {
        this.rol = pRol;
    }

    /**
     * Obtiene la operación permitida.
     *
     * @return Operación permitida
     */
    public Operacion getOperacion() {
        return operacion;
    }

    /**
     * Actualiza la operación permitida.
     *
     * @param pOperacion Operación permitida
     */
    public void setOperacion(final Operacion pOperacion) {
        this.operacion = pOperacion;
    }

}
