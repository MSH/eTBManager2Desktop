package org.msh.etbm.desktop.xview;

import org.msh.xview.FormManager;
import org.msh.xview.swing.SwingFormManager;
import org.springframework.stereotype.Component;

/**
 * Factory to create the local implementation of {@link FormManager} interface
 * 
 * @author Ricardo Memoria
 *
 */
@Component("formManagerFactory")
public class FormManagerFactory {

	public FormManager createFormManager() {
		SwingFormManager frmManager = new SwingFormManager("/xview/");
//		frmManager.addInterceptor(new TransactionalFormInterceptor());
		return frmManager;
	}

}
