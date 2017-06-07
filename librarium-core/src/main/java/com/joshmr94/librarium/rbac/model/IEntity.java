package com.joshmr94.librarium.rbac.model;



/**
 * Interfaz base para todas las entidades del sistema.
 * @author Miguel J. Trujillo Alvarado 
 */
public interface IEntity {

	/**
	 * Obtiene el ID del objeto.
	 * @return ID del objeto
	 */
	Long getId();

	/**
	 * Actualiza el ID del objeto.
	 * @param id ID del objeto
	 */
	void setId(Long id);

	/**
	 * Indica si el objeto ha sido persistido o es nuevo.
	 * @return Verdadero si no ha sido persistido
	 */
	boolean isNew();

}
