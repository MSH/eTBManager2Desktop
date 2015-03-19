/**
 * 
 */
package org.msh.xview.swing.ui.fields;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.common.GuiUtils;
import org.msh.etbm.entities.AdministrativeUnit;
import org.msh.etbm.services.adminunit.AdminUnitsQuery;

/**
 * @author Ricardo Memoria
 *
 */
public abstract class BaseAdminUnitFieldUI extends FieldComponentUI {


	/**
	 * Return the list of items to be displayed according to the selected administrative unit
	 * @param selectedAU instance of the {@link AdministrativeUnit} class or null if none is selected
	 * @return list of objects
	 */
	protected abstract List getItems(AdministrativeUnit selectedAU);

	
	/**
	 * Return the administrative unit assigned to the object value that is part of the
	 * items in the combo box
	 * @param value an object that contains a link to the administrative unit
	 * @return instance of {@link AdministrativeUnit} class
	 */
	protected abstract AdministrativeUnit getAdminUnit(Object value);


	/** {@inheritDoc}
	 */
	@Override
	protected JComponent createEditComponent() {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		
		JComboBox cbAdminUnits = new JComboBox();
		JComboBox cbItems = new JComboBox();
		
		panel.add(cbAdminUnits);
		panel.add(cbItems);

		int h = (int)cbAdminUnits.getPreferredSize().getHeight();
		
		cbAdminUnits.setBounds(0, 0, 250, h);
		cbItems.setBounds(0, h + 4, 250, h);
		cbItems.setVisible(false);
		panel.setSize(255, 50);
		
		fillAdminUnits(cbAdminUnits);
		
		cbAdminUnits.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				adminUnitChanged();
			}
		});
		
		cbItems.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedItemChanged();
			}
		});
		return panel;
	}

	
	/**
	 * Return the combo box that store the list of administrative units
	 * @return instance of {@link JComboBox}
	 */
	protected JComboBox getComboAdminUnits() {
		JPanel pnl = (JPanel)getComponent();
		return (JComboBox)pnl.getComponent(0);
	}
	
	
	/**
	 * Return the combo box that store the list of items
	 * @return instance of {@link JComboBox}
	 */
	protected JComboBox getComboItems() {
		JPanel pnl = (JPanel)getComponent();
		return (JComboBox)pnl.getComponent(1);
	}
	
	/**
	 * Receives notification when the combo box selection changes
	 */
	protected void selectedItemChanged() {
		Object item = getSelectedItem();
		setValue( item );
	}

	
	/**
	 * Return the selected object in the combo box of items 
	 * @return object value
	 */
	public Object getSelectedItem() {
		JComboBox cbItems = getComboItems();
		if (!cbItems.isVisible()) {
			return null;
		}

		int index = cbItems.getSelectedIndex();
		Object item = index > 0? cbItems.getItemAt(index): null;
		
		return item;
	}

	
	/**
	 * Called when the selected administrative unit is changed in the combo box
	 */
	protected void adminUnitChanged() {
		JComboBox cbAdminUnits = getComboAdminUnits();
		JComboBox cbItems = getComboItems();

		if (cbAdminUnits.getSelectedIndex() <= 0) {
			cbItems.setVisible(false);
			updateWidth();
		}
		else {
			cbItems.setVisible(true);

			AdministrativeUnit adm = (AdministrativeUnit)cbAdminUnits.getItemAt(cbAdminUnits.getSelectedIndex());
			cbItems.removeAll();
			
			if (adm == null)
				return;

			List items = getItems(adm);
			
			GuiUtils.initComboBox(cbItems, items);
			updateWidth();
		}

		// notify about change of the size
		notifySizeChanged();
	}



	/**
	 * Fill the drop down list of administrative units
	 */
	private void fillAdminUnits(JComboBox cb) {
		AdminUnitsQuery qry = App.getComponent(AdminUnitsQuery.class);
		qry.setParentId(null);
		qry.refresh();
		List<AdministrativeUnit> lst = qry.getResultList();
		
		GuiUtils.initComboBox(cb, lst);
		
		qry.refresh();
	}


	/**
	 * Update the width of the panel according to the width of the combo boxes
	 */
	private void updateWidth() {
		JComboBox cbAdminUnits = getComboAdminUnits();
		JComboBox cbItems = getComboItems();

		Container parent = cbAdminUnits.getParent();

		int w = cbAdminUnits.getWidth();
		int h = cbAdminUnits.getHeight();
		if (cbItems.isVisible()) {
			if (cbItems.getWidth() > w)
				w = cbItems.getWidth();
			h += cbItems.getHeight() + 4;
		}
		
		parent.setSize(w + 10, h);
		parent.setPreferredSize(new Dimension(w + 10, h));
	}




	/** {@inheritDoc}
	 */
	@Override
	protected void updateEditComponent() {
		JComboBox cbAdminUnits = getComboAdminUnits();

		Object value = getValue();

		// if it's null, clear the selected unit
		if (value == null) {
			cbAdminUnits.setSelectedIndex(0);
			cbAdminUnits.getParent().setSize(cbAdminUnits.getPreferredSize());
			cbAdminUnits.setPreferredSize(cbAdminUnits.getPreferredSize());
			return;
		}
		
		// check if it's the same selection
		Object selectedItem = getSelectedItem();
		if ((value == selectedItem) ||
			((value != null) && (value.equals(selectedItem))) ||
			((selectedItem != null) && (selectedItem.equals(value))))
			return;

		// prevent notifying the view about change of unit (at this moment that admin unit is changed)
		selectedItem = null;

		AdministrativeUnit adm = getAdminUnit(value);
		adm = adm.getParentLevel1();
		cbAdminUnits.setSelectedItem(adm);

		JComboBox cbItems = getComboItems();
		cbItems.setSelectedItem(value);
	}


}
