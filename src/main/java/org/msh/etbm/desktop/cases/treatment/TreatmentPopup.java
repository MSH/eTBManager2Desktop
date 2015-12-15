/**
 * 
 */
package org.msh.etbm.desktop.cases.treatment;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.persistence.EntityManager;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

import org.jdesktop.swingx.JXLabel;
import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.app.AppEvent;
import org.msh.etbm.desktop.app.MainWindow;
import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.desktop.app.UiConstants;
import org.msh.etbm.desktop.common.JButtonEx;
import org.msh.etbm.desktop.components.AwesomeIcon;
import org.msh.etbm.entities.PrescribedMedicine;
import org.msh.etbm.entities.TreatmentHealthUnit;
import org.msh.etbm.services.cases.treatment.TreatmentServices;
import org.msh.eventbus.EventBusService;
import org.msh.springframework.persistence.ActionCallback;
import org.msh.springframework.persistence.EntityManagerUtils;
import org.msh.timeline.TimelinePeriod;
import org.msh.utils.date.LocaleDateConverter;
import org.msh.utils.date.Period;
import org.msh.xview.swing.XViewUtils;
import org.pushingpixels.substance.api.SubstanceLookAndFeel;
/**
 * Contains the code that initialize a panel for various popup windows of the treatment
 * @author Ricardo Memoria
 *
 */
public class TreatmentPopup {

	/**
	 * Initialize the popup according to the data linked in the {@link TimelinePeriod} argument
	 * @param period instance of the {@link TimelinePeriod} object
	 * @return instance of the {@link JPanel} representing the popup content
	 */
	public JPanel initialize(TimelinePeriod period) {
		if (period.getData() instanceof TreatmentHealthUnit)
			return initializeHealthUnitPopup(period);
		
		if (period.getData() instanceof PrescribedMedicine)
			return initializePrescMedicinePopup(period);
		
		return initializePeriodPopup(createBasePanel(), period, period.getLabel());
	}
	
	/**
	 * Initialize the panel with the layout to display information about the prescribed medicine
	 * @param period instance of the {@link TimelinePeriod} containing a link to an instance of 
	 * {@link PrescribedMedicine} in its {@link TimelinePeriod#getData()} property
	 * @return {@link JPanel} object with the prescrined medicine layout
	 */
	private JPanel initializePrescMedicinePopup(TimelinePeriod period) {
		PrescribedMedicine pm = (PrescribedMedicine)period.getData();

		final JPanel pnl = createBasePanel();
		final TimelinePeriod tmper = period;
		EntityManagerUtils.doInTransaction(new ActionCallback<Integer>(pm.getId()) {
			@Override
			public void execute(Integer data) {
				EntityManager em = App.getEntityManager();
				PrescribedMedicine pm = em.find(PrescribedMedicine.class, data);
				pm.getTbcase();

				addTitle(pnl, pm.getMedicine().toString(), new AwesomeIcon(AwesomeIcon.ICON_H_SIGN, Color.BLACK, 20.0f));
				addPeriodLabels(pnl, tmper);

				addFieldLabel(pnl, Messages.getString("PrescribedMedicine.doseUnit") + ":");
				addFieldValue(pnl, Integer.toString( pm.getDoseUnit() ));

				addFieldLabel(pnl, Messages.getString("PrescribedMedicine.frequency") + ":");
				addFieldValue(pnl, Integer.toString( pm.getFrequency() ) + "/7");

				addFieldLabel(pnl, Messages.getString("Source") + ":");
				addFieldValue(pnl, pm.getSource().getName().toString());
				
				// include buttons if the case is open
				if (pm.getTbcase().isOpen()) {
					JButtonEx btn = new JButtonEx(Messages.getString("form.edit") + "...");
					btn.setData(pm);
					btn.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							PrescribedMedicine pm = (PrescribedMedicine)((JButtonEx)e.getSource()).getData();
							PrescribedMedicineDlg dlg = new PrescribedMedicineDlg();
							// tells the treatment panel to refresh itself
							if (dlg.openEditingForm(pm.getId()))
								refreshTreatment(pm.getTbcase().getId());
						}
					});
					btn.setIcon(new AwesomeIcon(AwesomeIcon.ICON_EDIT, btn));
					pnl.add(btn);
					
					// add delete button to remove a treatment period 
					JButtonEx btn2 = new JButtonEx(Messages.getString("form.remove"));
					btn2.setData(pm);
					btn2.setIcon(new AwesomeIcon(AwesomeIcon.ICON_REMOVE, btn));
					pnl.add(btn2, "align right");
					// code to remove a treatment period
					btn2.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							PrescribedMedicine pm = (PrescribedMedicine)((JButtonEx)e.getSource()).getData();
							removePrescribedMedicine(pm);
						}
					});
				}
			}
		});
		
		return pnl;
	}

	
	/**
	 * Called when the user clicks on the delete button to remove a prescribed medicine period
	 * @param pm instance of the {@link PrescribedMedicine} class
	 */
	protected void removePrescribedMedicine(PrescribedMedicine pm) {
		if (JOptionPane.showConfirmDialog(MainWindow.instance().getFrame(), 
				Messages.getString("form.confirm_remove"), 
				Messages.getString("cases.regimens.remmed"), JOptionPane.YES_NO_OPTION) == JOptionPane.NO_OPTION)
			return;

		// remove the prescription inside a transaction
		EntityManagerUtils.doInTransaction(new ActionCallback<PrescribedMedicine>(pm) {
			@Override
			public void execute(PrescribedMedicine pm) {
				pm = App.getEntityManager().merge(pm);
				TreatmentServices srv = App.getComponent(TreatmentServices.class);
				srv.removePrescribedMedicine(pm);
			}
		});
		refreshTreatment(pm.getTbcase().getId());
		EventBusService.raiseEvent(AppEvent.CASES_REFRESH);
	}
	
	/**
	 * Notify in the event bus that the treatment panel of the given case must be refreshed  
	 */
	protected void refreshTreatment(Integer caseid) {
		EventBusService.raiseEvent(AppEvent.TREATMENT_REFRESH, caseid);
	}
	
	/**
	 * Initialize the panel to display information about the health unit
	 * @param period instance of the {@link TimelinePeriod} class
	 * @return the instance of the {@link JPanel} containing the layout
	 */
	private JPanel initializeHealthUnitPopup(TimelinePeriod period) {
		TreatmentHealthUnit hu = (TreatmentHealthUnit)period.getData();

		final JPanel pnl = createBasePanel();
		final TimelinePeriod tmper = period;

		EntityManagerUtils.doInTransaction(new ActionCallback<Integer>(hu.getId()) {
			@Override
			public void execute(Integer data) {
				EntityManager em = App.getEntityManager();
				TreatmentHealthUnit hu = em.find(TreatmentHealthUnit.class, data);
				
				addTitle(pnl, hu.getTbunit().getName().toString(), new AwesomeIcon(AwesomeIcon.ICON_HOSPITAL, Color.BLACK, 20.0f));
				JXLabel lblx = new JXLabel(hu.getTbunit().getAdminUnit().getFullDisplayName());
				pnl.add(lblx, "span 2");

				addPeriodLabels(pnl, tmper);
			}
		});
		return pnl;
	}


	/**
	 * Initialize the panel with a title and its period information
	 * @param pnl the instance of the JPanel to initialize
	 * @param period the instance of {@link TimelinePeriod} with period information
	 * @param label the String containing the label to display
	 * @return the same panel given as parameter
	 */
	protected JPanel initializePeriodPopup(JPanel pnl, TimelinePeriod period, String label) {
		addTitle(pnl, label, null);
		addPeriodLabels(pnl, period);
		return pnl;
	}


	/**
	 * Create the base panel
	 * @return
	 */
	protected JPanel createBasePanel() {
		JPanel pnl = new JPanel();
		pnl.setLayout(new MigLayout("wrap 2", "[grow][grow]"));
		pnl.setBackground(Color.WHITE);
		pnl.putClientProperty(SubstanceLookAndFeel.COLORIZATION_FACTOR, new Double(1));
		return pnl;
	}
	

	/**
	 * Add the labels containing information about the period of the time line
	 * @param pnl panel to add the fields to
	 * @param period instance of the {@link TimelinePeriod} containing the period
	 */
	protected void addPeriodLabels(JPanel pnl, TimelinePeriod period) {
		JLabel lbl = new JLabel(LocaleDateConverter.getDisplayDate(period.getIniDate(), false));
		pnl.add(lbl);

		lbl = new JLabel(LocaleDateConverter.getDisplayDate(period.getEndDate(), false));
		pnl.add(lbl, "align right");

		lbl = new JLabel(LocaleDateConverter.getAsLength(new Period(period.getIniDate(), period.getEndDate())));
		pnl.add(lbl, "align center,span 2,wrap");
	}


	/**
	 * Add a title to the panel
	 * @param pnl
	 * @param title
	 * @param icon
	 */
	protected void addTitle(JPanel pnl, String title, ImageIcon icon) {
		JXLabel txt = new JXLabel(title);
		txt.setIcon(icon);
		txt.setLineWrap(true);
		txt.setFont(UiConstants.h3Font);
		int h = XViewUtils.calcTextHeight(txt, 350);
		txt.setPreferredSize(new Dimension(350, h));
		pnl.add(txt, "grow,span 2,width :350:350");
	}
	
	/**
	 * Add the label of a field to the panel
	 * @param pnl instance of the {@link JPanel} to add the label
	 * @param label is the label to display
	 */
	protected void addFieldLabel(JPanel pnl, String label) {
		JXLabel lbl = new JXLabel(label);
		lbl.setFont(UiConstants.fieldLabel);
		pnl.add(lbl);
	}
	
	/**
	 * Add the value of a field to the panel
	 * @param pnl instance of the {@link JPanel} to add the label
	 * @param label is the label to display
	 */
	protected void addFieldValue(JPanel pnl, String label) {
		JXLabel lbl = new JXLabel(label);
		lbl.setFont(UiConstants.fieldValue);
		pnl.add(lbl);
	}
}
