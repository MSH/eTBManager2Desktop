/**
 * 
 */
package org.msh.etbm.services.login;

import javax.persistence.EntityManager;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.entities.ServerSignature;
import org.msh.springframework.persistence.ActionCallback;
import org.msh.springframework.persistence.EntityManagerUtils;
import org.springframework.stereotype.Service;

/**
 * Handle services related to the server signature
 * @author Ricardo Memoria
 *
 */
@Service
public class ServerSignatureServices {
	
	/**
	 * Return information about the server in a {@link ServerSignature} object
	 * @return instance of {@link ServerSignature} class
	 */
	public ServerSignature getServerSignature() {
		return App.getEntityManager().find(ServerSignature.class, ServerSignature.SERVER_ID);
	}
	
	/**
	 * Persist information about the server signature
	 * @param server
	 */
	public void updateServerSignature(ServerSignature server) {
		EntityManagerUtils.doInTransaction(new ActionCallback<ServerSignature>(server) {

			@Override
			public void execute(ServerSignature server) {
				if (server.getId() == null) {
					server.setId(ServerSignature.SERVER_ID);
				}
				EntityManager em = App.getEntityManager();
				server = em.merge(server);
				em.persist(server);
			}
		});
	}

}
