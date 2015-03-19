/**
 * 
 */
package org.msh.xview.swing.ui;

import javax.swing.JComponent;

import org.msh.etbm.desktop.app.UiConstants;
import org.msh.xview.components.XHeader3;

/**
 * @author Ricardo Memoria
 *
 */
public class Header3UI extends AbstractTextUI<XHeader3> {

	/** {@inheritDoc}
	 */
	@Override
	protected JComponent createComponent() {
		JComponent comp = super.createComponent();
		comp.setFont( UiConstants.h3Font );
		return comp;
	}
}
