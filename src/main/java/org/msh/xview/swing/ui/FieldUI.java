/**
 * 
 */
package org.msh.xview.swing.ui;

import java.awt.Dimension;

import org.jdesktop.swingx.JXLabel;
import org.msh.xview.components.XField;
import org.msh.xview.swing.ui.fields.FieldComponentFactory;
import org.msh.xview.swing.ui.fields.FieldComponentUI;

/**
 * @author Ricardo Memoria
 *
 */
public class FieldUI extends ViewUI<XField> {
	
	private LabelUI label;
	private ComponentUI delegLabel;
	private FieldComponentUI fieldComponent;

	/** {@inheritDoc}
	 */
	@Override
	public void update() {
		checkComponent();
		checkLabel();
		super.update();
	}

	
	/** {@inheritDoc}
	 */
	@Override
	protected void doUpdate() {
		// DO NOTHING
	}

	
	/**
	 * Create the field component that will handle the value to
	 * be displayed/edited
	 */
	protected void checkComponent() {
		if (fieldComponent != null) {
			return;
		}
		
		String field = getView().getField().getFieldName();
		Class type = getDataModel().getValueType(field);
		fieldComponent = FieldComponentFactory.instance().createFieldComponentUI(type, getView().getHandler());
		fieldComponent.setView(getView());
		if (fieldComponent.isLabelVisible()) {
			fieldComponent.setForceNewRow(Boolean.FALSE);
		}
		addChild(fieldComponent);
	}


	/**
	 * Check the current status of the label
	 */
	protected void checkLabel() {
		boolean showLabel = (fieldComponent.isLabelVisible()) && (getMode() == ViewMode.REGULAR);

		// label must be displayed ?
		if (!showLabel) {
			// remove the label, if exists
			if (label != null) {
				removeChild(label);
				label = null;
			}
			if (delegLabel != null) {
				removeChild(delegLabel);
				fieldComponent.disposeLabelDelegator(delegLabel);
				delegLabel = null;
			}
		}
		else {
			// is label available ?
			if ((label == null) && (delegLabel == null)) {
				// is there anything to display in the label
				String txt = getView().getLabel();
				if (txt != null) {
					delegLabel = fieldComponent.createLabelDelegator();
					// delegated label was created ?
					if (delegLabel != null) {
						addChild(0, delegLabel);
						delegLabel.setForceNewRow(getView().isForceNewRow());
					}
					else {
						// if delegated label was not created, create a default
						label = new LabelUI();
						label.setText(getView().getLabel());
						label.setForceNewRow(getView().isForceNewRow());
						addChild(0, label);
						((JXLabel)label.getComponent()).setMaximumSize(new Dimension(280, 1000));
					}
				}
			}
		}
	}
}
