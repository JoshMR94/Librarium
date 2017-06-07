package com.joshmr94.librarium.rbac.model;

import com.joshmr94.librarium.rbac.model.BaseEntity;
import com.joshmr94.librarium.rbac.model.IEntity;
import javax.persistence.Column;
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
public class Operacion extends BaseEntity implements IEntity {

    /**
     * ID de la operación.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre de la operación.
     */
    @Column(length = 255, nullable = false)
    private String operacion;

    /**
     * Recurso sobre el que se realiza la operación.
     */
    @ManyToOne(optional = false)
    private Recurso recurso;

    /**
     * Constructor por defecto.
     */
    public Operacion() {
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

    public String getOperacion() {
        return operacion;
    }

    public void setOperacion(String operacion) {
        this.operacion = operacion;
    }

    /**
     * Obtiene el recurso sobre el que se realiza la operación.
     *
     * @return Recurso sobre el que se realiza la operación
     */
    public Recurso getRecurso() {
        return recurso;
    }

    /**
     * Actualiza el recurso sobre el que se realiza la operación.
     *
     * @param pRecurso Recurso sobre el que se realiza la operación
     */
    public void setRecurso(final Recurso pRecurso) {
        this.recurso = pRecurso;
    }

}
