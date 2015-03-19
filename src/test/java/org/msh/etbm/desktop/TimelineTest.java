/**
 * 
 */
package org.msh.etbm.desktop;

import java.awt.Color;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;

import org.msh.timeline.TimelinePanel;
import org.msh.timeline.TimelineRow;
import org.msh.utils.date.DateUtils;

/**
 * @author Ricardo Memoria
 *
 */
public class TimelineTest extends JFrame{
	private static final long serialVersionUID = 8831922317189153642L;

	private TimelinePanel pnl;
	
	public TimelineTest() {
		super();
		setSize(750, 450);
		
		getContentPane().setLayout(null);
		pnl = new TimelinePanel();
		pnl.setIniDate(DateUtils.newDate(2011, 0, 1));
		pnl.setEndDate(DateUtils.newDate(2013, 6, 1));
		
		pnl.getDefaultStyle().setBackgroundColor(new Color(134,203,98));
		pnl.getDefaultStyle().setBorderColor(new Color(51, 102, 153));
		pnl.getDefaultStyle().setTextColor(Color.WHITE);
		
		TimelineRow row = pnl.addRow("My row number 1");
		row.addPeriod(DateUtils.newDate(2011, 1, 1), DateUtils.newDate(2011, 10, 15), "Test 1");
		row.addPeriod(DateUtils.newDate(2012, 5, 1), DateUtils.newDate(2013, 2, 1), "Test 2");
		
		row = pnl.addRow("My row number 2 is a very long row label");
		row.addPeriod(DateUtils.newDate(2011, 9, 1), DateUtils.newDate(2011, 11, 15), "Test 3");
		row.addPeriod(DateUtils.newDate(2012, 6, 1), DateUtils.newDate(2013, 2, 1), "Test 4");
		
		row = pnl.addRow("row 3");
		row.addPeriod(DateUtils.newDate(2011, 3, 1), DateUtils.newDate(2012, 11, 1), "Test 5");
		
		pnl.addRow("This is the header").setDrawHorizontalLine(false);

		row = pnl.addRow("Item 4");
		row.addPeriod(DateUtils.newDate(2011, 0, 1), DateUtils.newDate(2013, 6, 1), "Test 6");
		
		getContentPane().add(pnl);
		pnl.setBounds(5, 5, 700, 400);
		
		getContentPane().addComponentListener(new ComponentListener() {
			
			@Override
			public void componentShown(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentResized(ComponentEvent e) {
				updateSize();
			}
			
			@Override
			public void componentMoved(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void componentHidden(ComponentEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	/**
	 * 
	 */
	protected void updateSize() {
		pnl.setBounds(4, 4, getWidth() - 30, getHeight() - 50);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TimelineTest test = new TimelineTest();
		test.setVisible(true);
	}
}
