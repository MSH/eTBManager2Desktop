/**
 * 
 */
package org.msh.core;

import java.lang.reflect.Method;

/**
 * @author Ricardo Memoria
 *
 */
public class ClassUtils {

	
	public static Class getPropertyType(Class clazz, String property) {
		String[] props = property.split("\\.");
		String s = props[0];

		char c = s.charAt(0);
		c = Character.toUpperCase(c);
		s = c + s.substring(1);
		Method met = findMethod(clazz, "get" + s);
		if (met == null) {
			met = findMethod(clazz, "is" + s);
		}
		
		if (met == null) {
			throw new RuntimeException("Method not found for property " + s + " in class " + clazz.getName());
		}
		
		Class propType = met.getReturnType();
		if (props.length > 1) {		
			s = property.substring(s.length() + 1);
			propType = getPropertyType(propType, s);
		}
		return propType;
	
/*		try {
			PropertyDescriptor propDesc = new PropertyDescriptor(s, clazz);
			Class propType = propDesc.getPropertyType();
			if (props.length > 0) {
				s = property.substring(s.length() + 1);
				propType = getPropertyType(propType, s);
			}
			return propType;
	
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
*/	}
	
	
	public static Method findMethod(Class clazz, String methodName) {
		Method[] methods = clazz.getMethods();
		for (Method method: methods) {
			if ((method.getName().equals(methodName)) && (method.getParameterTypes().length == 0)) {
				return method;
			}
		}
		
		clazz = clazz.getSuperclass();
		if (clazz == null) {
			return null;
		}
		return findMethod(clazz, methodName);
	}
}
