/**
 * 
 */
package org.msh.xview.swing.ui.fields;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.jdesktop.swingx.JXLabel;
import org.msh.etbm.desktop.common.GuiUtils;
import org.msh.etbm.entities.AdministrativeUnit;
import org.msh.etbm.services.adminunit.AdminUnitsUtils;
import org.msh.xview.swing.XViewUtils;
import org.msh.xview.swing.ui.ComponentUI;
import org.msh.xview.swing.ui.ViewConstants;


/**
 * A component to select an administrative unit
 * 
 * @author Ricardo Memoria
 *
 */
public class AdminUnitFieldUI extends FieldComponentUI {

	private AdminUnitLabelUI label;
	private boolean updating;


	private final ActionListener actionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			JComboBox cb = (JComboBox)e.getSource();
			adminUnitChanged(cb);
		}
	};
	

	/** {@inheritDoc}
	 */
	@Override
	protected JComponent createEditComponent() {
		updating = true;
		JPanel pnl = new JPanel();
		pnl.setLayout(null);

		try {
			String[] names = AdminUnitsUtils.getCountryStructureLabels();
			int num = names.length;

			// create combos and labels
			for (int i = 0; i < num; i++) {
				JComboBox cb = new JComboBox();
				cb.addActionListener(actionListener);
				pnl.add(cb);
			}
			List<AdministrativeUnit> lst = AdminUnitsUtils.getAdminUnits(null);
			GuiUtils.initComboBox((JComboBox)pnl.getComponent(0), lst);
		} finally {
			updating = false;
		}

		return pnl;
	}

	
	/**
	 * Return the list of combo boxes inside this component
	 * @return Array of {@link JComboBox} components
	 */
	protected JComboBox[] getCombos() {
		JComponent comp = getComponent();
		JComboBox[] combos = new JComboBox[comp.getComponentCount()];
		for (int i = 0; i < comp.getComponentCount(); i++) {
			combos[i] = (JComboBox)comp.getComponent(i);
		}
		return combos;
	}

	
	/**
	 * Receives notification when the combo box is changed
	 * @param cb
	 */
	protected void adminUnitChanged(JComboBox cb) {
		if (updating) {
			return;
		}

		// update the controls under that
		JComboBox[] combos = getCombos();
		// get the index of the combo among the others
		int index = 0;
		for (JComboBox combo: combos) {
			if (combo == cb) {
				break;
			}
			index++;
		}
		
		// get selected item
		AdministrativeUnit adm;
		// check if a item was selected in the combo
		if (cb.getSelectedIndex() > 0) {
			adm = (AdministrativeUnit)cb.getSelectedItem();
		}
		else {
			// user selected the blank item
			adm = index == 0? null: (AdministrativeUnit)combos[index - 1].getSelectedItem();
		}

		// update value in the data model
		setValue(adm);
		
		if (label != null) {
			label.setAdminUnit(adm);
		}

		updating = true;
		try {
			// if is the last combo, so do nothing
			if (index == combos.length - 1) {
				return;
			}

			// admin unit was selected?
			if (cb.getSelectedIndex() > 0) {
				// fill the combo of the next one
				fillComboBox(combos[index + 1], adm);
				// hide the other combos
				if (combos[index + 1].getItemCount() == 1) {
					hideComponents(index + 1);
				}
				else {
					hideComponents(index + 2);
				}
			}
			else {
				hideComponents(index + 1);
			}
			notifySizeChanged();
		}
		finally {
			updating = false;
		}
	}

	
	/** {@inheritDoc}
	 */
	@Override
	protected void updateEditComponent() {
		AdministrativeUnit adm = (AdministrativeUnit)getValue();
		updateSelection(adm);
		getComponent().setBackground( getBackgroundColor() );
	}

	/** {@inheritDoc}
	 */
	@Override
	public ComponentUI createLabelDelegator() {
		label = new AdminUnitLabelUI(getView().getField().getFieldName());
		return label;
	}


	/** {@inheritDoc}
	 */
	@Override
	protected void updateEditWidth(int width) {
		JComboBox[] combos = getCombos();
		int dy = ViewConstants.ROW_SPACE;
		int yo = 0;

		int index = 0;
		for (JComboBox combo: combos) {
			if (!combo.isVisible()) {
				break;
			}
			combo.setLocation(0, yo);

			// update the label position
			int lh = label != null? label.setLabel(index, 0, yo): 0;
			
			if (combo.getHeight() > lh) {
				lh = combo.getHeight();
			}
			
			yo += lh + dy;
			index++;
		}
		
		yo -= dy;

		// update the size of the 
		JComponent comp = getComponent();
		comp.setSize(width, yo);
		comp.setPreferredSize(new Dimension(width, yo));

		// update the label height, if available
		if (label != null) {
			JComponent c = label.getComponent();
			c.setSize(c.getWidth(), yo);
		}
	
		super.updateEditWidth(width);
	}

	
	/**
	 * Hide the components that display the combo boxes after a level and
	 * show the others within the level, starting in 1. For example, if is
	 * given level 3, the combos 4 and 5 will be hidden, while the 
	 * others will be displayed
	 * 
	 * @param level is the selected administrative unit level
	 */
	protected void hideComponents(int level) {
		int index = 1;
		if (isReadOnly()) {
			for (JXLabel txt: getLabels()) {
				txt.setVisible( index <= level );
				index++;
			}
		}
		else {
			for (JComboBox combo: getCombos()) {
				combo.setVisible( index <= level );
				if (!combo.isVisible()) {
					combo.removeAllItems();
				}
				index++;
			}
		}
	}

	/**
	 * Update the administrative unit being displayed
	 * @param au
	 */
	private void updateSelection(AdministrativeUnit au) {
		JComboBox[] combos = getCombos();

		// is admin unit null ?
		if (au == null) {
			hideComponents(1);
			combos[0].setSelectedIndex(0);
			return;
		}

		hideComponents(au.getLevel() + 1);

		List<AdministrativeUnit> lst = au.getParents();

		updating = true;
		try {
			for (int index = 0; index < combos.length; index++) {
				// is visible ?
				if (index < lst.size()) {
					AdministrativeUnit adm = lst.get(index);
					// is not the administrative unit selected?
					if (!combos[index].getSelectedItem().equals(adm)) {
						combos[index].setSelectedItem(adm);
						if (index < combos.length - 1) {
							// update items of the next combo
							fillComboBox(combos[index + 1], adm);
						}
					}
				}
				else {
					if (index > lst.size()) {
						combos[index].removeAllItems();
					}
				}
			}
		} finally {
			updating = false;
		}
	}


	/**
	 * @param jComboBox
	 * @param adm
	 */
	private void fillComboBox(JComboBox cb, AdministrativeUnit adm) {
		List<AdministrativeUnit> lst = AdminUnitsUtils.getAdminUnits(adm);
		GuiUtils.initComboBox(cb, lst);
	}

	
	/**
	 * Return an array of labels of the read only view
	 * @return array of {@link JXLabel}
	 */
	protected JXLabel[] getLabels() {
		JComponent comp = getComponent();
		JXLabel[] lbs = new JXLabel[comp.getComponentCount()];
		for (int i = 0; i < comp.getComponentCount(); i++) {
			lbs[i] = (JXLabel)comp.getComponent(i);
		}
		return lbs;
	}

	/** {@inheritDoc}
	 */
	@Override
	protected JComponent createReadOnlyComponent() {
		JPanel pnl = new JPanel();
        pnl.setBorder(new LineBorder(Color.RED));
		pnl.setLayout(null);

		String[] names = AdminUnitsUtils.getCountryStructureLabels();

		// create combos and labels
		for (int i = 0; i < names.length; i++) {
			JXLabel lb = new JXLabel();
			pnl.add(lb);
		}
		return pnl;
	}


	/** {@inheritDoc}
	 */
	@Override
	protected void updateReadOnlyComponent() {
		getComponent().setBackground( getBackgroundColor() );

		AdministrativeUnit adm = (AdministrativeUnit)getValue();
		JXLabel[] lbs = getLabels();
		if (adm == null) {
			hideComponents(1);
			lbs[0].setText("-");
			getComponent().setSize(50, 16);
			return;
		}
		
		List<AdministrativeUnit> lst = adm.getParents();

		hideComponents(adm.getLevel());
		int index = 0;
		int w = 0;
		for (AdministrativeUnit au: lst) {
			lbs[index].setText(au.getName().toString());
			int pw = (int)lbs[index].getPreferredSize().getWidth();
			if (pw > w) {
				w = pw;
			}
			index++;
		}
		// height doesn't matter now
		getComponent().setSize(w, 16);
	}


	/** {@inheritDoc}
	 */
	@Override
	protected void updateReadOnlyWidth(int width) {
		JXLabel[] lbs = getLabels();
		int yo = 0;
		int dy = ViewConstants.ROW_SPACE;
		int index = 0;

		for (JXLabel lb: lbs) {
			if (lb.isVisible()) {
				int h = XViewUtils.adjustHeight(lb, width);
				lb.setLocation(0, yo);
				int lh = label != null? label.setLabel(index, 0, yo): 0;
				if (lh > h) {
					h = lh;
				}
				
				yo += h + dy;
			}
			else {
				break;
			}
			index++;
		}
		
		// update the size of the 
		JComponent comp = getComponent();
		comp.setSize(width, yo);
		comp.setPreferredSize(new Dimension(width, yo));

		// update the label height, if available
		if (label != null) {
			JComponent c = label.getComponent();
			c.setSize(c.getWidth(), yo);
		}
	}

	
}
