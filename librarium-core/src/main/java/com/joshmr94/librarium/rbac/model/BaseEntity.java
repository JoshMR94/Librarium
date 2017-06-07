package com.joshmr94.librarium.rbac.model;

import java.io.Serializable;

/**
 * Interfaz base para todas las entidades del sistema.
 *
 * @author Miguel J. Trujillo Alvarado 
 */
public abstract class BaseEntity implements IEntity, Serializable {

    /**
     * Indica si el registro es nuevo.
     *
     * @return Verdadero si aún no tiene ID
     */
    @Override
    public boolean isNew() {
        return getId() == null;
    }

    /**
     * Indica si admite baja lógica.
     *
     * @return Verdadero si admite baja lógica
     */
    public Boolean getBajable() {
        return isBajable();
    }

    /**
     * Indica si admite baja lógica.
     *
     * @return Verdadero si admite baja lógica
     */
    public boolean isBajable() {
        return IEntityBaja.class.isAssignableFrom(getClass());
    }

    /**
     * Indica si está de baja lógica.
     *
     * @return Verdadero si está de baja lógica
     */
    public Boolean getBaja() {
        return Boolean.FALSE;
    }

    /**
     * Código hash del registro.
     *
     * @return Código hash del registro
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        int hcId = 0;
        if (getId() != null) {
            hcId = getId().hashCode();
        }
        result = prime * result + hcId;
        return result;
    }

    /**
     * Compara el objeto con otro.
     *
     * @param obj Objeto a comparar
     * @return Verdadero si se refieren al mismo registro
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        return !compareObjects(obj);
    }

    private boolean compareObjects(final Object obj) {
        Class<?> cl = getBaseClass(getClass());
        Class<?> objCl = getBaseClass(obj.getClass());
        if (!cl.equals(objCl)) {
            return true;
        }
        BaseEntity other = (BaseEntity) obj;
        if (getId() == null || other.getId() == null) {
            return true;
        } else if (!getId().equals(other.getId())) {
            return true;
        }
        return false;
    }

    /**
     * Obtiene la clase base del objeto (no proxy).
     *
     * @param clazz Clase de la que se quiere obtener la clase base
     * @return Clase base
     */
    protected Class<?> getBaseClass(final Class<?> clazz) {
        Class<?> result = clazz;
        if (result != null) {
            while (result != null && result.toString().contains("$")) {
                result = result.getSuperclass();
            }
        }
        return result;
    }

    /**
     * Convierte el objeto a "Human-Readable".
     *
     * @return Objeto como "Human-Readable"
     */
    @Override
    public String toString() {
        String s = "";
        if (IEntityCodigoDescripcion.class.isAssignableFrom(getClass())) {
            s = s + ((IEntityCodigoDescripcion) this).getCodigo() + "/";
        }
        if (IEntityDescripcion.class.isAssignableFrom(getClass())) {
            s = s + ((IEntityDescripcion) this).getDescripcion() + "/";
        }
        return getClass().getName() + "[" + s + getId() + "]";
    }

}
