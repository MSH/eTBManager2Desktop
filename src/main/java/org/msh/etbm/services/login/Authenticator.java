package org.msh.etbm.services.login;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.msh.etbm.entities.User;
import org.msh.etbm.entities.UserWorkspace;
import org.msh.etbm.entities.enums.UserState;
import org.msh.utils.Passwords;
import org.springframework.transaction.annotation.Transactional;

public class Authenticator {

	private EntityManager entityManager;


	/**
	 * Execute login
	 * @param username
	 * @param password
	 * @param workspaceId
	 * @return
	 */
	@Transactional
	public boolean login(String username, String password, Integer workspaceId) {
    	String pwd = Passwords.hashPassword(password);

        try {
        	User user = (User)entityManager.createQuery("from User u where u.login = :login " +
        						"and upper(u.password) = :pwd")
        						.setParameter("login", username.toUpperCase())
        						.setParameter("pwd", pwd.toUpperCase())
        						.getSingleResult();
        	System.out.println("Login de " + user.getLogin());
        	
        	if (user.getState() == UserState.BLOCKED)
        		return false;

        	UserWorkspace userWorkspace = selectWorkspace(user, workspaceId);
            if (userWorkspace.getHealthSystem() != null) {
                userWorkspace.getHealthSystem().getId();
            }

        	if (userWorkspace == null)
        		return false;
        	
        	UserSession.instance().registerLogin(userWorkspace);
        	UserSession.instance().setPassword(password);

        	// check user workspace
/*        	loadUserWorkspace();
        	if (userWorkspaces.size() == 0) 
        		return false;

        	UserWorkspace userWorkspace = selectWorkspace();
        	if (userWorkspace == null)
        		return false;

        	if (userWorkspace.getProfile().getPermissions().size() == 0) {
            	facesMessages.addFromResourceBundle("login.norole");
        		return false;
        	}

        	// avoid lazy initialization problem
        	userWorkspace.getTbunit().getAdminUnit().getParentsTreeList(true);
        	if (userWorkspace.getAdminUnit() != null)
        		userWorkspace.getAdminUnit().getId();
        	if (userWorkspace.getHealthSystem() != null)
        		userWorkspace.getHealthSystem().getId();
        			
            // adjust time zone
        	String tm = user.getTimeZone();
        	if ((tm == null || (tm.isEmpty())))
        		tm = user.getDefaultWorkspace().getWorkspace().getDefaultTimeZone();

        	if (tm != null) {
            	timeZoneSelector.setTimeZoneId(tm);
            	timeZoneSelector.select();
            }
            
        	// adjust the language
        	user.setLanguage(localeSelector.getLanguage());
        	localeSelector.select();
      
            // registra o usuário
        	UserSession userSession = getUserSession();

        	userSession.setUserWorkspace(userWorkspace);
            userSession.changeUserWorkspace();
            
            if (user.isPasswordExpired()) {
            	Redirect.instance().setViewId(null);
            }
*/
        	return true;
        }
        catch (NoResultException e) {
            return false;        	
        }
	}

	   
    /**
     * Select the user workspace for using during the session
     */
    private UserWorkspace selectWorkspace(User user, Integer workspaceId) {
    	if (workspaceId == null) {
    		if (user.getDefaultWorkspace() != null)
    			return user.getDefaultWorkspace();
    		
    		List<UserWorkspace> lst = entityManager.createQuery("from UserWorkspace u "
    				+ "join fetch u.workspace "
    				+ "join fetch u.tbunit "
    				+ "where u.user.id = :id")
    			.setParameter("id", user.getId())
    			.getResultList();
    		
    		return lst.size() > 0? lst.get(0): null;
    	}

    	List<UserWorkspace> lst = entityManager.createQuery("from UserWorkspace u " +
    			"join fetch u.workspace " +
    			"join fetch u.tbunit " +
    			"where u.user.id = :userid and u.workspace.id = :wsid")
    			.setParameter("userid", user.getId())
    			.setParameter("wsid", workspaceId)
    			.getResultList();
    	
    	if (lst.size() == 0)
    		return null;

    	return (UserWorkspace)lst.get(0);
    }

	/**
	 * @return the entityManager
	 */
	public EntityManager getEntityManager() {
		return entityManager;
	}

	/**
	 * @param entityManager the entityManager to set
	 */
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
