package org.msh.springframework.seam;

import java.io.Serializable;

import javax.persistence.EntityManager;

import org.msh.etbm.desktop.app.App;

/**
 * Base controller class for business implementation in Swing application, making available some simple
 * components, like {@link EntityManager}
 * 
 * @author Ricardo Memoria
 *
 */
public class Controller implements Serializable {
	private static final long serialVersionUID = -4057575350503343609L;

	public EntityManager getEntityManager() {
		return (EntityManager)App.getComponent("entityManager");
	}
}
