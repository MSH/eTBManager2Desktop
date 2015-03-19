/**
 * 
 */
package org.msh.xview.swing.ui.fields;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.jdesktop.swingx.JXLabel;
import org.msh.etbm.desktop.app.UiConstants;
import org.msh.etbm.entities.AdministrativeUnit;
import org.msh.etbm.services.adminunit.AdminUnitsUtils;
import org.msh.xview.components.XField;
import org.msh.xview.swing.ui.ComponentUI;
import org.msh.xview.swing.XViewUtils;


/**
 * @author Ricardo Memoria
 *
 */
public class AdminUnitLabelUI extends ComponentUI<XField> {
	
	private AdministrativeUnit adminUnit;
	private String fieldName;
	
	/**
	 * Default constructor
	 * @param field
	 */
	public AdminUnitLabelUI(String fieldName) {
		super();
		this.fieldName = fieldName;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	protected JComponent createComponent() {
		JPanel pnl = new JPanel();
		pnl.setLayout(null);
		String[] names = AdminUnitsUtils.getCountryStructureLabels();

		for (String name: names) {
			JXLabel lb = new JXLabel(name + ":");
			lb.setLineWrap(true);
			lb.setFont( UiConstants.fieldLabel );
			pnl.add(lb);
		}
		return pnl;
	}

	
	/**
	 * Set the label position and return its height according to
	 * the width of the container
	 * @param index
	 * @param xo
	 * @param yo
	 * @return
	 */
	public int setLabel(int index, int xo, int yo) {
		JComponent comp = getComponent();
		JXLabel lb = (JXLabel)comp.getComponent(index);
		int width = comp.getWidth();
		int height = XViewUtils.calcTextHeight(lb, width);
		lb.setBounds(xo, yo, width, height);
		return height;
	}
	
	/** {@inheritDoc}
	 */
	@Override
	public void setWidth(int width) {
		getComponent().setPreferredSize(new Dimension(width, 20));
		getComponent().setSize(width, 20);

/*		int dy = getFormContext().getFormUI().getFieldVerticalSpace();
		int yo = 0;
		int xo = 0;

		for (JXLabel lbl: getLabels()) {
			System.out.println(lbl.getText());
			int h = XViewUtils.calcTextHeight(lbl, width);
			lbl.setBounds(xo, yo, width, h);

			yo += h + dy;
			if (!lbl.isVisible()) {
				break;
			}
		}
		yo -= dy;
		getComponent().setPreferredSize(new Dimension(width, yo));
		getComponent().setSize(width, yo);
*/	}


	/** {@inheritDoc}
	 */
	@Override
	protected void doComponentUpdate() {
		getComponent().setBackground( getBackgroundColor() );
		AdministrativeUnit adm = (AdministrativeUnit)getDataModel().getValue(fieldName);
		if (adm == null) {
			hideLabels(0);
			return;
		}
		
		hideLabels(adm.getLevel());
	}

	
	/**
	 * Return the label y-axis positions
	 * @return array of int values
	 */
	public int[] getLabelPositions() {
		JXLabel[] labels = getLabels();
		int[] posy = new int[labels.length];

		int index = 0;
		for (JXLabel lbl: getLabels()) {
			posy[index++] = lbl.getY();
		}
		
		return posy;
	}
	
	/**
	 * Hide labels that are bellow a certain level and
	 * show labels that are within the given level
	 * @param level is the administrative unit level to display
	 */
	protected void hideLabels(int level) {
		int index = 1;
		for (JXLabel lb: getLabels()) {
			lb.setVisible(index <= level + 1);
			index++;
		}
	}
	
	/**
	 * Return the list of labels being displayed in the component
	 * @return
	 */
	protected JXLabel[] getLabels() {
		JPanel pnl = (JPanel)getComponent();
		JXLabel[] labels = new JXLabel[pnl.getComponentCount()];
		
		for (int i = 0; i < pnl.getComponentCount(); i++) {
			labels[i] = (JXLabel)pnl.getComponent(i);
		}
		
		return labels;
	}
	
	
	/**
	 * @return the adminUnit
	 */
	public AdministrativeUnit getAdminUnit() {
		return adminUnit;
	}

	/**
	 * @param adminUnit the adminUnit to set
	 */
	public void setAdminUnit(AdministrativeUnit adminUnit) {
		this.adminUnit = adminUnit;
//		update();
	}


}
