/**
 * 
 */
package org.msh.xview.swing.ui;

import javax.swing.JComponent;

import org.msh.etbm.desktop.app.UiConstants;
import org.msh.xview.components.XHeader1;

/**
 * Represents a label with a H1 (header) style
 * 
 * @author Ricardo Memoria
 *
 */
public class Header1UI extends AbstractTextUI<XHeader1> {

	/** {@inheritDoc}
	 */
	@Override
	protected JComponent createComponent() {
		JComponent comp = super.createComponent();
		comp.setFont( UiConstants.h1Font );
		return comp;
	}

}
