/**
 * 
 */
package org.msh.customdata;

/**
 * @author Ricardo Memoria
 *
 */
public interface TypeHandler<E> {

	String getId();
	
	E getTypeClass();
	
	/**
	 * Return the SQL primitive type to be saved in the table
	 * @param value
	 * @return
	 */
	Object getSqlPrimitiveType(E value);
	
	/**
	 * From the primitive type saved in the database, return the
	 * original value
	 * @param value
	 * @return
	 */
	E getTypeFromPrimitiveType(Object value);
}
