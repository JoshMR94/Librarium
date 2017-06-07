package com.joshmr94.librarium.rbac.model;



/**
 * Interfaz para indicar que el registro admite baja lógica.
 * @author Miguel J. Trujillo Alvarado 
 */
public interface IEntityBaja extends IEntity {

	/**
	 * Obtiene el estado respecto a la baja lógica.
	 * @return Estado actual
	 */
	Boolean getBaja();

	/**
	 * Actualiza el estado respecto a la baja lógica.
	 * @param baja Nuevo estado
	 */
	void setBaja(Boolean baja);

}
