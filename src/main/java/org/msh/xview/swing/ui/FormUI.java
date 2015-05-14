/**
 * 
 */
package org.msh.xview.swing.ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.msh.etbm.desktop.common.GuiUtils;
import org.msh.xview.FormDataModel;
import org.msh.xview.components.XForm;
import org.msh.xview.swing.SwingFormContext;
import org.msh.xview.swing.layout.TableLayout;

/**
 * @author Ricardo Memoria
 *
 */
public class FormUI extends ComponentUI<XForm> {

	private SwingFormContext formContext;
	// preferred width of a form
	private int preferredWidth = 0;
	// minimum width of a label
//	private int minLabelWidth = 150;
	// maximum width of a label
//	private int maxLabelWidth = 300;
	// padding of the form
	private int formPadding = 10;
	
	// true if the update method was ever called
	private boolean initialized;
	
	private boolean layoutRefreshNeeded;
	

	/**
	 * Change the background color of the form. This is the safer way to change,
	 * since it'll also notify the containers about that
	 * @param color is the background color
	 */
	public void setBackgroundColor(Color color) {
		if (getComponent() == null)
			throw new RuntimeException("Panel was not created yet");
		GuiUtils.setBackground(getComponent(), color);
		notifyParentBGColorChange(color);
	}
	
	/**
	 * Initialize the form. This method must be called just once and when
	 * the form is created
	 */
	public void initialize(SwingFormContext context){
		createChildren();
		this.formContext = context;
	}

	/** {@inheritDoc}
	 */
	@Override
	public FormDataModel getDataModel() {
		return formContext.getDataModel();
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void setWidth(int width) {
		this.preferredWidth = width;
		updateLayout();
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public SwingFormContext getFormContext() {
		return formContext;
	}

	/** {@inheritDoc}
	 * @see org.msh.xview.swing_old.ui.ContainerUI#update()
	 */
	@Override
	public void doComponentUpdate() {
		// call interceptors before update the form
		getFormContext().callBeforeUpdateInterceptors();
		try {
			updateLayout();
			initialized = true;
		} finally {
			getFormContext().callAfterUpdateInterceptors();
		}
	}


	/** {@inheritDoc}
	 * @see org.msh.xview.swing_old.ui.ContainerUI#updateLayout()
	 */
	public void updateLayout() {
		super.updateLayout();

		TableLayout layout = new TableLayout(this, getView().getColumnCount());
		layout.setWidth(preferredWidth);
		int h = layout.updateLayout();
		int width = layout.getWidth();
		
		JPanel panel = (JPanel)getComponent();
		panel.setSize(new Dimension(width, h));
		panel.setPreferredSize(new Dimension(width, h));
		panel.setMaximumSize(new Dimension(3000, h));
		layoutRefreshNeeded = false;
	}

	/**
	 * @return the preferredWidth
	 */
	public int getPreferredWidth() {
		return preferredWidth;
	}

	/**
	 * @param preferredWidth the preferredWidth to set
	 */
	public void setPreferredWidth(int preferredWidth) {
		this.preferredWidth = preferredWidth;
	}

	/**
	 * @return the maxLabelWidth
	 */
/*	public int getMaxLabelWidth() {
		return maxLabelWidth;
	}
*/
	/**
	 * @param maxLabelWidth the maxLabelWidth to set
	 */
/*	public void setMaxLabelWidth(int maxLabelWidth) {
		this.maxLabelWidth = maxLabelWidth;
	}
*/
	/**
	 * @return the formPadding
	 */
	public int getFormPadding() {
		return formPadding;
	}

	/**
	 * @param formPadding the formPadding to set
	 */
	public void setFormPadding(int formPadding) {
		this.formPadding = formPadding;
	}

	/**
	 * Check if the update() method was called for this form
	 * @return true if the update method was called
	 */
	public boolean isInitialized() {
		return initialized;
	}

	/** {@inheritDoc}
	 */
	@Override
	protected JComponent createComponent() {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		return panel;
	}

	/**
	 * @return the layoutRefreshNeeded
	 */
	public boolean isLayoutRefreshNeeded() {
		return layoutRefreshNeeded;
	}

	@Override
	public void notifyStateChanged() {
		layoutRefreshNeeded = true;
	}

}
