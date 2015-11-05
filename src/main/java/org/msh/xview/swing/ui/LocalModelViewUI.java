/**
 * 
 */
package org.msh.xview.swing.ui;

import org.msh.xview.FormDataModel;
import org.msh.xview.components.XView;
import org.msh.xview.impl.DefaultFormDataModel;

/**
 * @author Ricardo Memoria
 *
 */
public class LocalModelViewUI extends ViewUI<XView>{

	private DefaultFormDataModel dataModel;
	
	
	/** {@inheritDoc}
	 */
	@Override
	public FormDataModel getDataModel() {
		if (dataModel == null) {
			dataModel = new DefaultFormDataModel(super.getDataModel());
		}
		return dataModel;
	}


	/** {@inheritDoc}
	 */
	@Override
	protected void doUpdate() {
		// do nothing
	}

    @Override
    protected boolean isComponentVisible() {
        return true;
    }
}
