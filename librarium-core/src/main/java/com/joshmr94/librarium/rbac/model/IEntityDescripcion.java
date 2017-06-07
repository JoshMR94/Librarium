package com.joshmr94.librarium.rbac.model;



/**
 * Interfaz base para las entidades con descripción.
 * @author Miguel J. Trujillo Alvarado 
 */
public interface IEntityDescripcion extends IEntity {

	/**
	 * Obtiene la descripción.
	 * @return Descripción del objeto
	 */
	String getDescripcion();

	/**
	 * Actualiza la descripción del objeto.
	 * @param descripcion Nueva descripción
	 */
	void setDescripcion(String descripcion);

}
