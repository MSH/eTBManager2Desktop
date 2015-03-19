/**
 * 
 */
package org.msh.xview.swing.ui;

import javax.swing.JComponent;

import org.msh.etbm.desktop.app.UiConstants;
import org.msh.xview.components.XHeader2;

/**
 * @author Ricardo Memoria
 *
 */
public class Header2UI extends AbstractTextUI<XHeader2> {

	/** {@inheritDoc}
	 */
	@Override
	protected JComponent createComponent() {
		JComponent comp = super.createComponent();
		comp.setFont( UiConstants.h2Font );
		return comp;
	}
}
