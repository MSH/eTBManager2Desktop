/**
 * 
 */
package org.msh.etbm.desktop.common;

import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXLabel;
import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.desktop.app.UiConstants;

/**
 * Standard panel with the {@link MigLayout} as the default layout manager. It also
 * contains methods that make it easier to compound form layouts.
 * 
 * @author Ricardo Memoria
 *
 */
public class MigLayoutPanel extends JPanel{
	private static final long serialVersionUID = 7084921783808229928L;

	/**
	 * Default constructor
	 */
	public MigLayoutPanel() {
		super();
		setLayout(new MigLayout());
	}

	/**
	 * Constructor with layout constraints
	 * @param layoutConstraints are layout constraints passed to the {@link MigLayout}
	 */
	public MigLayoutPanel(String layoutConstraints) {
		super();
		setLayout(new MigLayout(layoutConstraints));
	}
	
	/**
	 * Constructor with layout, column and row constraints passed to the {@link MigLayout} constructor
	 * @param layoutConstraints layout constraints of the {@link MigLayout}
	 * @param columnConstraints column constraints of the {@link MigLayout}
	 */
	public MigLayoutPanel(String layoutConstraints, String columnConstraints) {
		super();
		setLayout(new MigLayout(layoutConstraints, columnConstraints));
	}
	
	/**
	 * Constructor with layout, column and row constraints passed to the {@link MigLayout} constructor
	 * @param layoutConstraints layout constraints of the {@link MigLayout}
	 * @param columnConstraints column constraints of the {@link MigLayout}
	 * @param rowConstraints row constraints of the {@link MigLayout}
	 */
	public MigLayoutPanel(String layoutConstraints, String columnConstraints, String rowConstraints) {
		super();
		setLayout(new MigLayout(layoutConstraints, columnConstraints, rowConstraints));
	}
	
	
	/**
	 * Add a label to the panel using the default column and row constraints
	 * @param label the string label to be displayed in the panel
	 * @return instance of the {@link JXLabel} inserted in the panel
	 */
	public JXLabel addLabel(String label) {
		return addLabel(label, "");
	}

	/**
	 * Add a label to the panel using specific column constraints
	 * @param label the string label to be displayed in the panel
	 * @param constraints constraints sent to the MigLayout
	 * @return instance of the {@link JXLabel}
	 */
	public JXLabel addLabel(String label, String constraints) {
		JXLabel lbl = new JXLabel(Messages.translateMessage(label));
		add(lbl, constraints);
		return lbl;
	}

	/**
	 * Add a field label to the panel specifying column constraints for the {@link MigLayout}
	 * @param label the String label to be displayed
	 * @param constraints String constraints of the {@link MigLayout}
	 * @return instance of the {@link JXLabel} containing the field label
	 */
	public JXLabel addFieldLabel(String label, String constraints) {
		JXLabel lbl = addLabel(label, constraints);
		lbl.setFont(UiConstants.fieldLabel);
		return lbl;
	}

	/**
	 * Add a field label to the panel using default column constraints of the {@link MigLayout}
	 * @param label the String label to be displayed
	 * @return instance of the {@link JXLabel} containing the field label
	 */
	public JXLabel addFieldLabel(String label) {
		JXLabel lbl = addLabel(Messages.translateMessage(label) + ":");
		lbl.setFont(UiConstants.fieldLabel);
		return lbl;
	}
}
