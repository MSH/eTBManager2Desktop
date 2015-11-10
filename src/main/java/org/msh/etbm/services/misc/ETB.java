package org.msh.etbm.services.misc;

import org.msh.etbm.entities.Workspace;
import org.msh.etbm.services.login.UserSession;

/**
 * A group of utilities functions of e-TB Manager
 * @author Ricardo Memoria
 *
 */
public class ETB {

	/**
	 * Return the specific subclass of the given class for the workspace
	 * @param clazz
	 * @return
	 */
	public static Class getWorkspaceClass(Class clazz) {
		Workspace workspace = UserSession.getWorkspace();
		if (workspace == null)
			return clazz;

		return getWorkspaceClass(clazz, workspace);
	}

	/**
	 * Return the specific subclass of the given class for the workspace
	 * @param clazz
	 * @return
	 */
	public static Class getWorkspaceClass(Class clazz, Workspace workspace) {
		if (workspace == null)
			return clazz;

		String ext = workspace.getExtension();
		if (ext != null) {
			String className = clazz.getName();
			className = className.replace("org.msh.etbm", "org.msh.etbm.custom." + ext);
			className += ext.toUpperCase();

			try {
				return Class.forName(className);
			} catch (ClassNotFoundException e) {
				return clazz;
			}
		}
		else return clazz;
	}

	/**
	 * Create a new object using a customized version in the current workspace, if available
	 * @param clazz point to the class to create an object
	 * @param <E> the base class to create an object from
	 * @return instance of the class, but may be a custom object
	 */
	public static <E> E newWorkspaceObject(Class<E> clazz, Workspace workspace) {
		try {
			clazz = getWorkspaceClass(clazz, workspace);
			return clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Create a new object using a customized version in the current workspace, if available
	 * @param clazz point to the class to create an object
	 * @param <E> the base class to create an object from
	 * @return instance of the class, but may be a custom object
	 */
	public static <E> E newWorkspaceObject(Class<E> clazz) {
		try {
			clazz = getWorkspaceClass(clazz);
			return clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Return the display name of the sub class implemented for the given class 
	 * of the current workspace
	 * @param clazz
	 * @return
	 */
	public static String getWsClassName(Class clazz) {
		return ETB.getWorkspaceClass(clazz).getName();
	}
}
