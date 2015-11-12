package org.msh.etbm.services.login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.entities.Tbunit;
import org.msh.etbm.entities.User;
import org.msh.etbm.entities.UserProfile;
import org.msh.etbm.entities.UserWorkspace;
import org.msh.etbm.entities.Workspace;
import org.msh.etbm.entities.enums.CaseClassification;
import org.msh.etbm.sync.WorkspaceInfo;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class UserSession {

	private boolean loggedIn;
	private String password;
	private UserWorkspace userWorkspace;
	private List<String> roles;
	private boolean offline;
	// store in memory the last token used to get connected to the server (save time)
	private String lastToken;
	private Tbunit tbunit;
	private Map<String, Object> values = new HashMap<String, Object>();
	//To be used only during the initialization
	private WorkspaceInfo workspaceInfo;

	/**
	 * Register data about the user logged in
	 * @param userWorkspace the user and its workspace
	 * @param password the user password
	 */
	@Transactional(propagation=Propagation.REQUIRED)
	public void registerLogin(UserWorkspace userWorkspace) {
		this.userWorkspace = userWorkspace;
		
		// avoid lazy initialization
		userWorkspace.getUser();
		userWorkspace.getWorkspace();
		userWorkspace.getAdminUnit();
		userWorkspace.getTbunit().getAdminUnit().getName();

		updateUserRoleList();
	}
    
    /**
     * Mount the user permission list
     */
    public void updateUserRoleList() {
    	roles = new ArrayList<String>();

    	UserProfile prof = userWorkspace.getProfile();

    	List<Object[]> lst = App.getEntityManager().createQuery("select u.userRole.name, u.canChange, u.caseClassification " +
    			"from UserPermission u where u.userProfile.id = :id and u.canExecute = true")
    			.setParameter("id", prof.getId())
    			.getResultList();
    	
    	for (Object[] vals: lst) {
    		String roleName = (String)vals[0];
    		
    		CaseClassification classification = (CaseClassification)vals[2];

    		if (classification != null)
    			roleName = classification.toString() + "_" + roleName;
    		roles.add(roleName);
    		
    		boolean change = (Boolean)vals[1];
    		if (change) {
    			roles.add(roleName + "_EDT");
    		}
    	}
    }

  
	/**
	 * Check if user is logged into the system
	 * @return
	 */
	public boolean isLoggedIn() {
		return loggedIn;
	}

	
	protected UserWorkspace getUserWorkspaceRef() {
		return userWorkspace;
	}
	
	
	protected Workspace getWorkspaceRef() {
		return userWorkspace.getWorkspace();
	}
	
	/**
	 * @param rolename
	 * @return
	 */
	public boolean hasRole(String rolename) {
		return true;
//		return roles != null && roles.contains(rolename);
	}


	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the roles
	 */
	public List<String> getRoles() {
		return roles;
	}

	/**
	 * @param roles the roles to set
	 */
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	/**
	 * @return the offline
	 */
	public boolean isOffline() {
		return offline;
	}

	/**
	 * @return the userWorkspace
	 */
	public static UserWorkspace getUserWorkspace() {
		return instance().getUserWorkspaceRef();
	}

	/**
	 * Check if TB unit has started medicine management 
	 * @return
	 */
	public boolean isMedicineManagementStarted() {
		return (getTbunit().isMedicineManagementStarted());
	}

	public boolean isCanCheckReceiving() {
		return (UserSession.instance().hasRole("RECEIV") && (getTbunit().isReceivingFromSource()));
	}


	public boolean isCanCheckDispensing() {
		return UserSession.instance().hasRole("DISP_PAC") && (getTbunit().isMedicineStorage()) && (getTbunit().isTreatmentHealthUnit());
	}


	public boolean isCanCheckOrders() {
		return (UserSession.instance().hasRole("ORDERS")) && (getTbunit().isMedicineManagementStarted());
	}


	/**
	 * Check if user can view the estimated position report
	 * @return
	 */
	public boolean isCanViewEstPositionReport() {
		return (UserSession.instance().hasRole("REP_ESTPOS")) && (getTbunit().isMedicineManagementStarted());		
	}

	
	/**
	 * Check if user can view the stock evolution report
	 * @return
	 */
	public boolean  isCanViewStockEvolutionReport() {
		return (UserSession.instance().hasRole("REP_STOCKEVOL")) && (getTbunit().isMedicineManagementStarted());				
	}
	
	/**
	 * Check if user can view the cost of treatment per patient report
	 * @return
	 */
	public boolean  isCanViewCostPatientReport() {
		return (UserSession.instance().hasRole("REP_COSTPAT")) && (getTbunit().isMedicineManagementStarted());				
	}

	public boolean isCanCheckMovements() {
		return (UserSession.instance().hasRole("MOVS") && (getTbunit().isMedicineStorage())) && (getTbunit().isMedicineManagementStarted());
	}


	public boolean isCanCheckTransfers() {
		return ((UserSession.instance().hasRole("TRANSFER")) && (getTbunit().isMedicineManagementStarted()));
	}


	public boolean isCanCheckForecasting() {
		return true;
	}


	public boolean isCanAdjustStock() {
		return UserSession.instance().hasRole("STOCKPOS") && (getTbunit().isMedicineStorage()) && (getWorkingTbunit().equals(tbunit));
	}


	public boolean isCanSetupUnit() {
		return UserSession.instance().hasRole("UNITSETUP") && (getTbunit().isMedicineStorage()) && (getWorkingTbunit().equals(tbunit));
	}


	public boolean isCanCreateOrder() {
		Tbunit u = getTbunit(); 
		return UserSession.instance().hasRole("NEW_ORDER") && u.isMedicineStorage() && (getWorkingTbunit().equals(u)) && 
				((u.getFirstLineSupplier() != null) || (u.getSecondLineSupplier() != null));
	}


	/**
	 * Return the working unit of the current user
	 * @return
	 */
	public Tbunit getWorkingTbunit() {
		UserWorkspace uw = getUser().getDefaultWorkspace();
		if (uw.isPlayOtherUnits())
			 return tbunit;
		else return uw.getTbunit();
	}

	/**
	 * Return true if the user can open TB or MDR cases
	 * @return
	 */
	public boolean isCanOpenCases() {
		return checkRoleAnyClassification("CASE_VIEW");
	}


	/**
	 * Check if a specific role name is allowed to the current user for any classification
	 * @param roleName
	 * @return
	 */
	public boolean checkRoleAnyClassification(String roleName) {
		for (CaseClassification cla: CaseClassification.values()) {
			if (UserSession.instance().hasRole(cla.toString() + "_" + roleName))
				return true;
		}
		return false;
	}


	/**
	 * Return true if the user can edit TB or MDR cases
	 * @return
	 */
	public boolean isCanEditCases() {
		return checkRoleAnyClassification("CASE_DATA_EDT");
	}


	/**
	 * Check if user can notify or change data of more than one case classification
	 * @return true if so
	 */
	public boolean isCanNotifySeveralClassifs() {
		UserSession id = UserSession.instance();
		int count = 0;
		for (CaseClassification classif: CaseClassification.values()) {
			String roleName = classif.toString() + "_CASE_DATA_EDT";
			if (id.hasRole(roleName))
				count++;
		}
		return count > 1;
	}


	/**
	 * Check if user can notify or change data of a specific classification
	 * @param classif
	 * @return true if user can notify more than one classification
	 */
	public boolean isCanEditCaseByClassification(CaseClassification classif) {
		return true;
//		return hasRole(classif.toString() + "_CASE_DATA_EDT");
	}

	
	/**
	 * Check if a specific case classification can be opened by the user 
	 * @param classif
	 * @return
	 */
	public boolean isCanOpenCaseByClassification(CaseClassification classif) {
		return hasRole(classif.toString() + "_CASE_DATA");
	}

	/**
	 * @return the tbunit
	 */
	public Tbunit getTbunit() {
		return tbunit;
	}

	/**
	 * @param tbunit the tbunit to set
	 */
	public void setTbunit(Tbunit tbunit) {
		this.tbunit = tbunit;
	}



	public static User getUser() {
		return instance().getUserWorkspaceRef().getUser();
	}


	public static Workspace getWorkspace() {
		return instance().getUserWorkspaceRef().getWorkspace();
	}
	
	
	public static UserSession instance() { 
		return App.getComponent(UserSession.class);
	}

	/**
	 * @return the lastToken
	 */
	public String getLastToken() {
		return lastToken;
	}

	/**
	 * @param lastToken the lastToken to set
	 */
	public void setLastToken(String lastToken) {
		this.lastToken = lastToken;
	}
	
	/**
	 * Store a value in the user session
	 * @param key
	 * @param value
	 */
	public void setValue(String key, Object value) {
		values.put(key, value);
	}
	
	/**
	 * Restore a value from the user session
	 * @param key
	 * @return
	 */
	public Object getValue(String key) {
		return values.get(key);
	}

	public WorkspaceInfo getWorkspaceInfo() {
		return workspaceInfo;
	}

	public void setWorkspaceInfo(WorkspaceInfo workspaceInfo) {
		this.workspaceInfo = workspaceInfo;
	}
}
