package org.msh.etbm.desktop.cases;

import java.awt.Container;

import javax.swing.JPanel;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.common.Refreshable;
import org.msh.etbm.entities.TbCase;
import org.msh.springframework.persistence.ActionCallback;
import org.msh.springframework.persistence.EntityManagerUtils;

/**
 * Class that a panel must implement in order to display 
 * data about a case in the tab of the CaseDetailPanel
 * 
 * @author Ricardo Memoria
 *
 */
public abstract class CaseTabPanel extends JPanel implements Refreshable {
	private static final long serialVersionUID = -6566468804932866749L;

	/**
	 * ID of the case in use
	 */
	private Integer caseId;
	
	/**
	 * Indicate that the content of this panel must be refreshed
	 */
	private boolean refreshRequired;


	/** {@inheritDoc}
	 */
	@Override
	public void refresh() {
		// check if case Id was set
		if (caseId == null)
			setVisible(false);
		else {
			setVisible(true);
			// run refresh inside a transaction
			EntityManagerUtils.doInTransaction(new ActionCallback() {
				@Override
				public void execute(Object data) {
					TbCase tbcase = App.getEntityManager().find(TbCase.class, caseId);
					refreshInTransaction(tbcase);
				}
			});
		}
		// set to false, since it's refreshed
		refreshRequired = false;
	}
	
	/**
	 * Called when the content of the panel must be updated. This method is 
	 * executed inside a database transaction, making it easier to handle 
	 * with lazy-initialized instances in Hibernate
	 * 
	 * @param tbcase is the instance of the {@link TbCase} class containing data of the case
	 */
	protected abstract void refreshInTransaction(TbCase tbcase);

	/**
	 * Called to force the {@link CaseDetailPanel} to update the whole case data.
	 * Each panel will have its refresh method called once when displayed to the user.
	 */
	protected void requestCaseRefresh() {
		Container parent = getParent();
		while ((parent != null) && (!(parent instanceof CaseDetailPanel)))
			parent = parent.getParent();
		
		if (parent == null)
			throw new IllegalAccessError("parent as instance of CaseDetailPanel not found");

		((CaseDetailPanel)parent).refresh();
	}
	
	/**
	 * Set the ID of the case in use
	 * @param id int value of the case ID
	 */
	public void setCaseId(int id) {
		this.caseId = id;
	}
	
	/**
	 * Return the ID of the case is use, or null if its not set
	 * @return {@link Integer} value
	 */
	public Integer getCaseId() {
		return caseId;
	}

	/**
	 * @return the refreshRequired
	 */
	public boolean isRefreshRequired() {
		return refreshRequired;
	}

	/**
	 * @param refreshRequired the refreshRequired to set
	 */
	public void setRefreshRequired(boolean refreshRequired) {
		this.refreshRequired = refreshRequired;
	}
}
