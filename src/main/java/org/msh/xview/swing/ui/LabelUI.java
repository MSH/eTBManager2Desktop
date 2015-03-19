/**
 * 
 */
package org.msh.xview.swing.ui;

import javax.swing.JComponent;

import org.jdesktop.swingx.JXLabel;
import org.msh.etbm.desktop.app.UiConstants;
import org.msh.xview.components.XLabel;
import org.msh.xview.swing.XViewUtils;


/**
 * @author Ricardo Memoria
 *
 */
public class LabelUI extends AbstractTextUI<XLabel>{

	
	/** {@inheritDoc}
	 */
	@Override
	protected JComponent createComponent() {
		JComponent comp = super.createComponent();
		comp.setFont( UiConstants.fieldLabel );
		return comp;
	}

	/** {@inheritDoc}
	 */
	@Override
	public String getText() {
		String s = super.getText();
		return s != null? s + ":": null;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public boolean isAutoGrow() {
		// labels, initially, should be as close as possible of the
		// right control, so it should not be resized
		return false;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void doComponentUpdate() {
		super.doComponentUpdate();
		
		// calculate the best width to the component
		JXLabel lb = getLabelComponent();
		int width = XViewUtils.calcTextWidth(lb);
		if (width > ViewConstants.MAX_LABEL_WIDTH) {
			width = ViewConstants.MAX_LABEL_WIDTH;
		}
		else {
			if (width < ViewConstants.MIN_LABEL_WIDTH) {
				width = ViewConstants.MIN_LABEL_WIDTH;
			}
		}
		
		// doesn't care about height right now, because it'll be 
		// adjusted when setWidth is called 
		lb.setSize(width + 2, 20);
//		lb.setPreferredSize(new Dimension(150, (int)lb.getPreferredSize().getHeight()));
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public int getPreferredWidth() {
		int w = super.getPreferredWidth();
		if (w > ViewConstants.MAX_LABEL_WIDTH) {
			w = ViewConstants.MAX_LABEL_WIDTH;
		}
		return w;
	}
}
