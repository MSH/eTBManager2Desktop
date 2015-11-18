/**
 * 
 */
package org.msh.etbm.desktop.cases.treatment;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.msh.etbm.desktop.app.App;
import org.msh.etbm.desktop.app.Messages;
import org.msh.etbm.desktop.app.UiConstants;
import org.msh.etbm.desktop.common.GuiUtils;
import org.msh.etbm.desktop.common.MigLayoutPanel;
import org.msh.etbm.entities.TbCase;
import org.msh.etbm.entities.TreatmentMonitoring;
import org.msh.etbm.entities.enums.RegimenPhase;
import org.msh.etbm.entities.enums.TreatmentDayOption;
import org.msh.etbm.services.cases.treatment.TreatmentFollowupData;
import org.msh.etbm.services.cases.treatment.TreatmentFollowupServices;
import org.msh.springframework.persistence.ActionCallback;
import org.msh.springframework.persistence.EntityManagerUtils;
import org.msh.utils.date.DateUtils;
import org.msh.utils.date.Period;


/**
 * @author Ricardo Memoria
 *
 */
public class TreatFollowupController {

	private TbCase tbcase;
	private TreatmentFollowupData data;
	private JTable tblIntensive;
	
	public void addComponents(MigLayoutPanel pnl) {
		data = getService().loadTreatmentFollowup(tbcase);

		pnl.addLabel("@cases.treat.intphase", "span3").setFont(UiConstants.h3Font);
		pnl.add(createTitles());
		pnl.add(createIntakeTable(tbcase.getIntensivePhasePeriod(), RegimenPhase.INTENSIVE), "span4,grow");

		pnl.addLabel("@cases.treat.contphase", "span3").setFont(UiConstants.h3Font);
		pnl.add(createTitles());
		pnl.add(createIntakeTable(tbcase.getContinuousPhasePeriod(), RegimenPhase.CONTINUOUS), "span4,grow");
	}
	
	/**
	 * Create the titles of the icons
	 * @return
	 */
	private MigLayoutPanel createTitles() {
		MigLayoutPanel pnlTitles = new MigLayoutPanel("wrap 4", "[]20[]20[]");
		pnlTitles.add(newLabel(TreatmentDayOption.DOTS));
		pnlTitles.add(newLabel(TreatmentDayOption.SELF_ADMIN));
		return pnlTitles;
	}
	
	/**
	 * Create the label to be displayed in the treatment follow-up table
	 * @param value type of treatment followup
	 * @return instance of JLabel configured to be displayed as a title
	 */
	private JLabel newLabel(TreatmentDayOption value) {
		return new JLabel(App.getMessage(value.getMessageKey()), TreatTableCellRenderer.getIcon(value), JLabel.LEFT);
	}
	
	/**
     * Create medicine intake follow-up panel to be displayed to the user
	 * @param period the period in dates
	 * @param phase The phase of the regimen
     * @return Panel to be displayed with information about intake medicine follow-up
	 */
	@SuppressWarnings("serial")
	public JScrollPane createIntakeTable(Period period, RegimenPhase phase) {
		JTable table = new JTable();
		String[] titles = new String[34];
		titles[0] = Messages.getString("global.month") + "/" + Messages.getString("global.year");
		for (int i = 1; i <= 31; i++) 
			titles[i] = Integer.toString(i);
		titles[32] = Messages.getString("cases.treat.presc");
		titles[33] = Messages.getString("cases.treat.disp");

		table.setModel(new DefaultTableModel(new Object[][] { }, titles) 
		{
				@Override
				public Class getColumnClass(int columnIndex) {
					return String.class;
				}
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			});

		table.getColumnModel().getColumn(0).setPreferredWidth(90);
		TreatTableCellRenderer renderer = new TreatTableCellRenderer(tbcase, phase, data);
		for (int i = 1; i <= 31; i++) {
			table.getColumnModel().getColumn(i).setPreferredWidth(20);
			table.getColumnModel().getColumn(i).setCellRenderer(renderer);
		}
		table.getColumnModel().getColumn(32).setPreferredWidth(130);
		table.getColumnModel().getColumn(33).setPreferredWidth(130);
		
		DefaultTableModel model = (DefaultTableModel)table.getModel();
		model.setRowCount(0);
		SimpleDateFormat sdf = new SimpleDateFormat("MMM-yy");
		Date dt = period.getIniDate();
		//while (dt.before(period.getEndDate())) {
		while (dt.compareTo(DateUtils.incDays(period.getEndDate(),1)) <= 0) {
			Object vals[] = new Object[34];
			vals[0] = sdf.format(dt);

            // set information about treatment for every day
            int month = DateUtils.monthOf(dt);
            int year = DateUtils.yearOf(dt);
            int planned = 0;
            int executed = 0;
            for (int day = 1; day <= DateUtils.daysInAMonth(year, month); day++) {
                Date dt2 = DateUtils.newDate(year, month, day);
                TreatmentDayOption opt = data.getTreatmentDay(dt2);

                if (opt == TreatmentDayOption.DOTS || opt == TreatmentDayOption.SELF_ADMIN) {
                    executed++;
                }

                if (data.isDayPrescription(dt2)) {
                    planned++;
                }
            }
            vals[32] = planned == 0? "-": Integer.toString(planned);
            vals[33] = executed == 0? "-": Integer.toString(executed);

            model.addRow(vals);
			dt = DateUtils.incMonths(dt, 1);
		}

		table.setPreferredScrollableViewportSize(new Dimension(500, 100));
		table.setFillsViewportHeight(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		GuiUtils.setBackground(table, Color.WHITE);
		table.setBorder(new EmptyBorder(0,0,0,0));
		JScrollPane pnl = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		GuiUtils.setBackground(pnl, Color.WHITE);
		pnl.setAutoscrolls(true);
		
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					JTable table = (JTable)e.getSource();
					handleCellClick(table);
				}
			}
		});
		
		if (phase == RegimenPhase.INTENSIVE)
			 tblIntensive = table;
		
		return pnl;
	}

	/**
	 * @param table
	 */
	protected void handleCellClick(JTable table) {
		if (!tbcase.isOpen())
			return;

		int col = table.getSelectedColumn();
		int row = table.getSelectedRow();
		if ((col < 1) || (col > 31))
			return;

        // get the treatment period
		Period p;
		if (table == tblIntensive)
			 p = tbcase.getIntensivePhasePeriod();
		else p = tbcase.getContinuousPhasePeriod();

		Date dt = p.getIniDate();
		if (row > 0)
			dt = DateUtils.incMonths(dt, row);

        // calculate the date of the selected cell in the table
		int month = DateUtils.monthOf(dt);
		int year = DateUtils.yearOf(dt);
		int mdays = DateUtils.daysInAMonth(year, month);
		if (col > mdays)
			return;

		dt = DateUtils.newDate(year, month, col);
		
		if (!p.isDateInside(dt))
			return;

		TreatmentDayOption status = data.getTreatmentDay(dt);
		if ((status == null) || (status == TreatmentDayOption.NOT_TAKEN)) {
            status = TreatmentDayOption.SELF_ADMIN;
        }
		else {
            if (status == TreatmentDayOption.SELF_ADMIN)
                status = TreatmentDayOption.DOTS;
            else if (status == TreatmentDayOption.DOTS)
                status = TreatmentDayOption.NOT_TAKEN;
        }

		data.setTreatmentDay(dt, status);
		EntityManagerUtils.doInTransaction(new ActionCallback<TreatmentFollowupData>(data) {
			@Override
			public void execute(TreatmentFollowupData treatData) {
				getService().saveTreatmentFollowup(treatData, tbcase);
			}
		});

        // update executed number of days
        int tot = 0;
        for (int i = 1; i <= mdays; i++) {
            Date d = DateUtils.newDate(year, month, i);
            TreatmentDayOption opt = data.getTreatmentDay(d);
            if (opt == TreatmentDayOption.SELF_ADMIN || opt == TreatmentDayOption.DOTS) {
                tot++;
            }
        }
        String s = tot == 0? "-": Integer.toString(tot);
        DefaultTableModel model = (DefaultTableModel)table.getModel();
        model.setValueAt(s, row, 33);
        model.fireTableDataChanged();
	}
	
	/**
	 * Return the service to handle treatment follow-up
	 * @return instance of {@link TreatmentFollowupServices}
	 */
	protected TreatmentFollowupServices getService() {
		return App.getComponent(TreatmentFollowupServices.class);
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
