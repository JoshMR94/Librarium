package com.joshmr94.librarium.rbac.model;



/**
 * Interfaz base para las entidades con código.
 * @author Miguel J. Trujillo Alvarado 
 */
public interface IEntityCodigoDescripcion extends IEntityDescripcion {

	/**
	 * Obtiene el código del registro.
	 * @return Código del registro
	 */
	String getCodigo();

	/**
	 * Actualiza el código del registro.
	 * @param codigo Código del registro
	 */
	void setCodigo(String codigo);

}
