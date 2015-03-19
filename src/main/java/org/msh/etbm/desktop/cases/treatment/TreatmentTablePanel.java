package org.msh.etbm.desktop.cases.treatment;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.desktop.app.UiConstants;
import org.msh.etbm.desktop.components.AwesomeIcon;
import org.msh.etbm.desktop.components.PopupPanel;
import org.msh.etbm.entities.Medicine;
import org.msh.etbm.entities.PrescribedMedicine;
import org.msh.etbm.entities.TbCase;
import org.msh.etbm.entities.TreatmentHealthUnit;
import org.msh.timeline.PeriodEvent;
import org.msh.timeline.PeriodEvent.PeriodEventType;
import org.msh.timeline.PeriodListener;
import org.msh.timeline.PeriodStyle;
import org.msh.timeline.TimelinePanel;
import org.msh.timeline.TimelinePeriod;
import org.msh.timeline.TimelineRow;
import org.msh.utils.date.DateUtils;

/**
 * Display the treatment table panel in a time line chart
 * 
 * @author Ricardo Memoria
 *
 */
public class TreatmentTablePanel extends TimelinePanel {
	private static final long serialVersionUID = 8391086219735280053L;

	private TbCase tbcase;
	
	private PeriodStyle medicineBarStyle;
	private PeriodStyle intPhaseBarStyle;
	private PeriodStyle contPhaseBarStyle;
	private AwesomeIcon iconMedicine = new AwesomeIcon(AwesomeIcon.ICON_CARET_RIGHT, Color.BLACK, 14);
	private PopupPanel popup;

	/**
	 * Default listeners to events that happens in the period bars
	 */
	private PeriodListener periodListener = new PeriodListener() {
		@Override
		public void onTimelinePeriodEvent(PeriodEvent event) {
			if (event.getType() == PeriodEventType.CLICK)
				showPopupPeriod(event.getPeriod());
		}
	};
	
	/**
	 * Default constructors
	 */
	public TreatmentTablePanel() {
		super();
//		setSize(300, 200);
//		Dimension d = getPreferredSize();
//		d.setSize(getWidth(), 200);
//		setPreferredSize(d);

		// style used in the time line periods
		Font font = UiConstants.defaultFont.deriveFont(Font.BOLD).deriveFont(10.0f);
		medicineBarStyle = new PeriodStyle(new Color(51, 102, 153), new Color(134,203,98), Color.WHITE, font, 16);
		intPhaseBarStyle = new PeriodStyle(Color.RED, Color.RED, Color.WHITE, font, 0);
		contPhaseBarStyle = new PeriodStyle(Color.ORANGE, Color.ORANGE, Color.WHITE, font, 0);

		addPeriodListener(periodListener);
	}



	/**
	 * Called when the user clicks on a period of the time line
	 * @param period instance of the {@link TimelinePeriod}
	 */
	protected void showPopupPeriod(TimelinePeriod period) {
		if (popup == null) {
			popup = new PopupPanel();
			popup.setSize(200, 100);
			popup.setPreferredSize(new Dimension(350,200));
			popup.setMinimumSize(new Dimension(350,200));
			popup.setBorder(new LineBorder(Color.BLACK));
			popup.setLayout(new BorderLayout(2, 2));
			add(popup);
		}
		
		popup.removeAll();
		TreatmentPopup tp = new TreatmentPopup();
		JPanel pnl = tp.initialize(period);
		if (pnl != null) {
			popup.add(pnl, BorderLayout.CENTER);
			popup.setPreferredSize(pnl.getPreferredSize());
		}

		popup.show(this, (int)period.getRectangle().getX(), (int)period.getRectangle().getY() + 20);
	}



	/**
	 * Update the table size according to the number of medicines in the case
	 */
	@Override
	public void refresh() {
		if (tbcase == null) {
//			setSize(getWidth(), 2);
		}

		setIniDate(tbcase.getTreatmentPeriod().getIniDate());
		setEndDate(tbcase.getTreatmentPeriod().getEndDate());
		
		removeRows();
		
		// avoid lazy initialization problem
		for (TreatmentHealthUnit hu: tbcase.getHealthUnits())
			hu.getTbunit().getName();

		// add periods for the health units
		TimelineRow rowhu = addRow(Messages.getString("TbCase.healthUnits"));
		for (TreatmentHealthUnit hu: tbcase.getHealthUnits()) {
			TimelinePeriod period = rowhu.addPeriod(hu.getPeriod().getIniDate(), 
					hu.getPeriod().getEndDate(), 
					hu.getTbunit().getName().toString());
			period.setStyle(medicineBarStyle);
			period.setData(hu);
		}

		// add periods for the regimen phases
		TimelineRow rowphase = addRow(Messages.getString("RegimenPhase"));
	
		// add intensive phase
		rowphase.addPeriod(tbcase.getTreatmentPeriod().getIniDate(), 
				DateUtils.incDays(tbcase.getIniContinuousPhase(), -1), 
				Messages.getString("RegimenPhase.INTENSIVE"))
				.setStyle(intPhaseBarStyle);

		// add continuous phase
		rowphase.addPeriod(tbcase.getIniContinuousPhase(), 
				tbcase.getTreatmentPeriod().getEndDate(), 
				Messages.getString("RegimenPhase.CONTINUOUS"))
				.setStyle(contPhaseBarStyle);

		// add prescribed medicines
		for (PrescribedMedicine pm: tbcase.getPrescribedMedicines()) {
			TimelineRow row = findMedicineRow(pm.getMedicine());
			row.setIcon(iconMedicine);
			TimelinePeriod period = row.addPeriod(pm.getPeriod().getIniDate(), 
					pm.getPeriod().getEndDate(), 
					pm.getDoseUnit() + " (" + pm.getFrequency() + "/7)");
			
			period.setStyle(medicineBarStyle);
			period.setData(pm);
		}

		setRowLabelFont(UiConstants.defaultFont);
		int h = getPreferredHeight();
		setSize(getWidth(), h);
		Dimension d = getPreferredSize();
		d.setSize(d.getWidth(), h);
		setPreferredSize(d);

		super.refresh();
	}

	
	/**
	 * Find a row of the medicine
	 * @param med
	 * @return
	 */
	protected TimelineRow findMedicineRow(Medicine med) {
		String s = med.toString();
		for (TimelineRow row: getRows()) {
			if (row.getLabel().equals(s))
				return row;
		}
		
		return addRow(s);
	}
	

	/**
	 * @return the tbcase
	 */
	public TbCase getTbcase() {
		return tbcase;
	}

	/**
	 * @param tbcase the tbcase to set
	 */
	public void setTbcase(TbCase tbcase) {
		this.tbcase = tbcase;
	}
}
