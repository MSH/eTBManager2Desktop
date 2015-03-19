/**
 * 
 */
package org.msh.timeline;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.SystemColor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedString;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.msh.timeline.PeriodEvent.PeriodEventType;
import org.msh.utils.date.DateUtils;

/**
 * Draw a time line in the are of the panel
 * 
 * @author Ricardo Memoria
 *
 */
public class TimelinePanel extends JPanel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1007909874608894942L;

	// list of the rows in the time line
	private List<TimelineRow> rows = new ArrayList<TimelineRow>();
	
	// initial date of the time line
	private Date iniDate;

	// final date of the time line
	private Date endDate;

	// padding of the area
	private int padding = 4;

	// minimum width of a month label in pixels
	private int monthLabelMinWidth = 40;

	// the default style to be applied to the bars
	private PeriodStyle defaultStyle;

	// the size of the left column that displays the labels
	private int leftColumnWidth = 200;
	
	private int headerHeight = 30;
	
	private int minRowHeight = 25;
	
	// number of days in the period
	private int numDays;

	// font used in the row
	private Font rowLabelFont;
	
	// left origin of the vertical guide line
	private int labelGuideX;
	private int labelGuideY;
	
	private Color timelineBkColor = new Color(251,251,243);
	
	private Color rowVerticalLineColor = new Color(230, 150, 100); 

	// space between rows in the left column of the time line
	private int rowVerticalSpace = 4;

	// indicate the period that has the mouse pointer over it
	private TimelinePeriod hoverPeriod;

	// event listeners to time line periods  
	private List<PeriodListener> periodListeners;
	
	/**
	 * Default constructor
	 */
	public TimelinePanel() {
		super();
		setLayout(null);
//		setSize(300, 200);
//		Dimension d = getPreferredSize();
//		d.setSize(getWidth(), 200);
//		setPreferredSize(d);
		setBackground(Color.WHITE);
		defaultStyle = new PeriodStyle(SystemColor.activeCaptionBorder, SystemColor.window, SystemColor.activeCaption, getFont(), 16);
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	
	/**
	 * Update the whole time line recalculating boundaries and redrawing it
	 */
	public void refresh() {
		if ((iniDate == null) || (endDate == null))
			throw new IllegalAccessError("Initial and final date must be defined");
		initialize();
		repaint();
	}

	/**
	 * Initialize the variables used for the drawing of the time line
	 */
	protected void initialize() {
		numDays = DateUtils.daysBetween(iniDate, endDate);
		if (rowLabelFont == null)
			rowLabelFont = getFont();
	}

	
	/**
	 * Add an instance of {@link PeriodListener} to receive notifications
	 * about events related to {@link TimelinePeriod} objects inside the time line
	 * @param listener instance of the {@link PeriodListener}
	 */
	public void addPeriodListener(PeriodListener listener) {
		if (periodListeners == null)
			periodListeners = new ArrayList<PeriodListener>();
		periodListeners.add(listener);
	}

	
	/**
	 * Remove an instance of {@link PeriodListener} added previously in the {@link TimelinePanel#addPeriodListener(PeriodListener)}
	 * @param listener instance of the {@link PeriodListener}
	 */
	public void removePeriodListener(PeriodListener listener) {
		if (periodListeners == null)
			return;
		periodListeners.add(listener);
	}
	
	/**
	 * Remove all rows of the time line
	 */
	public void removeRows() {
		rows.clear();
	}
	
	/**
	 * Add a row to the time line
	 * @param label the label of the row
	 * @param icon the optional icon to be displayed at the left side of the label
	 * @return instance of {@link TimelineRow}
	 */
	public TimelineRow addRow(String label, ImageIcon icon) {
		TimelineRow row = new TimelineRow(label, icon);
		rows.add(row);
		return row;
	}
	
	/**
	 * Add a row to the time line. A wrapper to {@link TimelinePanel#addRow(String, ImageIcon)}
	 * @param label the label of the row
	 * @return instance of {@link TimelineRow}
	 */
	public TimelineRow addRow(String label) {
		return addRow(label, null);
	}

	/** {@inheritDoc}
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if ((iniDate == null) || (endDate == null))
			return;
		
		if (numDays == 0)
			initialize();
		
		Graphics2D g2 = (Graphics2D)g;
		setAntialiasingGraphics(g2);

		drawBackground(g2);
		drawRows(g2);
	}

	
	/**
	 * Set the graphic drawing as antialiasing
	 */
	private void setAntialiasingGraphics(Graphics2D g2) {
		g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
	}


	/**
	 * Draw the background of the time line
	 * @param g2
	 */
	protected void drawBackground(Graphics2D g2) {
		Color bkColor = new Color(51, 102, 153);
		Color fontColor = new Color(255,255,255);
		Color lineColor = new Color(141, 192, 243);
		Color lineAltColor = new Color(230,230,230);
		// draw the header
		g2.setColor(bkColor);
		int w = getWidth() - (padding * 2);
		int h = getHeight() - (padding * 2);
		g2.fillRect(padding, padding, w, headerHeight);

		// draw the background area
		g2.setColor(timelineBkColor);
		g2.fillRect(leftColumnWidth + padding, headerHeight + padding, w - leftColumnWidth, h - headerHeight);
		
		// calculate the proper size of the months
		int months = DateUtils.monthsBetween(iniDate, endDate);
		float monthWidth = (float)(w - leftColumnWidth) / (float)months;
		int monthInc;
		if (monthWidth < (float)monthLabelMinWidth) {
			monthInc = (int)Math.ceil((float)monthLabelMinWidth / monthWidth);
		}
		else monthInc = 1;
		
		// draw vertical lines and month/year titles
		int i = 0;
		float fxo = leftColumnWidth + padding;
		String[] monthNames = new DateFormatSymbols().getShortMonths();
		int year = DateUtils.yearOf(iniDate);
		int month = DateUtils.monthOf(iniDate);

		int yearini = year;
		int xini = (int)fxo;
		while (i < months) {
			int xo = (int)fxo;
			if (i % monthInc == 0) {
				String s = monthNames[month];
				g2.setColor(fontColor);
				g2.setFont(g2.getFont().deriveFont(10.0f).deriveFont(Font.PLAIN));
				g2.drawString(s, xo + 4, 30);
				g2.setColor(lineColor);
				g2.drawLine(xo, 34, xo, h);
			}
			else {
				g2.setColor(lineAltColor);
				g2.drawLine(xo, 34, xo, h);
			}
			
			if (month == 0) {
				g2.setFont(g2.getFont().deriveFont(11.0f).deriveFont(Font.BOLD));
				g2.setColor(fontColor);
				g2.drawString(Integer.toString(year), xo + 4, 17);
			}

			month++;
			if (month == 12) {
				month = 0;
				year++;
			}
			fxo += monthWidth;
			i++;
		}

		// draw year if not rendered in the panel
		if (yearini == year) {
			g2.setFont(g2.getFont().deriveFont(11.0f).deriveFont(Font.BOLD));
			g2.setColor(fontColor);
			g2.drawString(Integer.toString(year), xini + 4, 17);
		}
	}

	
	/**
	 * Draw the rows and its time line bars
	 * @param g2
	 */
	private void drawRows(Graphics2D g2) {
		g2.setFont(g2.getFont().deriveFont(11.0f).deriveFont(Font.PLAIN));
		int yo = headerHeight + 10;
		for (TimelineRow row: rows) {
			int h = drawRowLabel(g2, row, yo);
			for (TimelinePeriod period: row.getPeriods())
				drawPeriod(g2, period, hoverPeriod == period);
			yo += h + rowVerticalSpace;
		}
	}

	/**
	 * Draw the bar in the time line panel
	 * @param bar
	 */
	protected void drawPeriod(Graphics2D g2, TimelinePeriod bar, boolean hover) {
		// calculate the width in days
		int xo = DateUtils.daysBetween(iniDate, bar.getIniDate());
		int x1 = numDays - DateUtils.daysBetween(bar.getEndDate(), endDate);

		int barWidth = getWidth() - (padding * 2) - leftColumnWidth;
		
		// normalize to the proportion of the table
		xo = leftColumnWidth + padding + ( xo * barWidth / numDays );
		x1 = leftColumnWidth + padding + ( x1 * barWidth / numDays );
		
		PeriodStyle style = bar.getStyle();
		if (style == null)
			style = defaultStyle;

		int yo = bar.getRow().getY() - 3;
		int y1 = yo + minRowHeight - 7;

		if (hover)
			 g2.setColor(style.getBackgroundColor().darker());
		else g2.setColor(style.getBackgroundColor());
		int w = x1 - xo;
		int h = y1 - yo;
		Rectangle r = new Rectangle(xo, yo, w, h);
		bar.setRectangle(r);
		if (style.getArcSize() == 0)
			 g2.fillRect(xo, yo, w, h);
		else g2.fillRoundRect(xo, yo, w, h, style.getArcSize(), style.getArcSize());

		if (hover)
		 	 g2.setColor(style.getBorderColor().darker());
		else g2.setColor(style.getBorderColor());
		g2.setStroke(new BasicStroke(2.0f));
		if (style.getArcSize() == 0)
			 g2.drawRect(xo, yo, w, h);
		else g2.drawRoundRect(xo, yo, w, h, style.getArcSize(), style.getArcSize());
		
		Rectangle rect = new Rectangle(xo + 4, yo + 1, w - 8, h - 4);
		g2.setFont(style.getFont());
		g2.setColor(style.getTextColor());
		drawTextRect(g2, bar.getLabel(), rect);
	}

	
	/**
	 * Draw the row label and returns its height
	 * @param row
	 * @return
	 */
	protected int drawRowLabel(Graphics2D g2, TimelineRow row, int y) {
		int width = leftColumnWidth - padding - 4;
		int xo = padding + 4;

		// is there any icon in the row ?
		if (row.getIcon() != null) {
			row.getIcon().paintIcon(this, g2, xo, y);
			xo += row.getIcon().getIconWidth();
			width -= row.getIcon().getIconWidth();
		}
		
		String s = row.getLabel();
		row.setY(y);
		g2.setColor(Color.BLACK);
		g2.setFont(rowLabelFont);
		int h = drawText(s, g2, xo, y, width);
		
		// draw the vertical guide line
		if (row.isDrawHorizontalLine()) {
			float[] dash1 = {4.0f};
			g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f));
			g2.setColor(rowVerticalLineColor);
			g2.drawLine(labelGuideX + 16, labelGuideY, getWidth() - padding - 4, labelGuideY);
		}
		
		if (h > minRowHeight)
			return h;
		else return minRowHeight;
	}

	
	/**
	 * Draw a text in the given position. If the text doesn't fit in the width,
	 * the line is broken
	 * @param s
	 * @param g
	 * @param x
	 * @param y
	 * @param width
	 * @return
	 */
	protected int drawText(String s, Graphics2D g, int x, int y, int width) {
		FontRenderContext frc = g.getFontRenderContext();
		AttributedString as = new AttributedString(s);
		as.addAttribute(TextAttribute.FONT, g.getFont());
		LineBreakMeasurer measurer = new LineBreakMeasurer(as.getIterator(), frc);
		int yo = y;
		while (measurer.getPosition() < s.length()) {
			TextLayout layout = measurer.nextLayout(width);
			// is the first line ?
			if (yo == y) {
				labelGuideX = (int)layout.getBounds().getWidth() + x;
				labelGuideY = (int)(layout.getAscent()/2) + y;
			}
			y += layout.getAscent();
			layout.draw(g, x, y);
			y += layout.getDescent() + layout.getLeading();
		}
		return y - yo;
	}
	
	
	/**
	 * @param s
	 * @param g
	 * @param width
	 * @return
	 */
	protected int calcTextHeight(String s, Font font, int width) {
		FontRenderContext frc = new FontRenderContext(null, true, true);
		AttributedString as = new AttributedString(s);
		as.addAttribute(TextAttribute.FONT, font);
		LineBreakMeasurer measurer = new LineBreakMeasurer(as.getIterator(), frc);
		int h = 0;
		while (measurer.getPosition() < s.length()) {
			TextLayout layout = measurer.nextLayout(width);
			h += layout.getAscent() + layout.getDescent() + layout.getLeading() + rowVerticalSpace;
		}
		return h;
	}

	
	
	/**
	 * Return the preferred height of the time line
	 * @return preferred height in pixels
	 */
	public int getPreferredHeight() {
		int h = (padding * 2) + headerHeight + 16;
		for (TimelineRow row: getRows()) {
			int w = leftColumnWidth;
			if (row.getIcon() != null)
				w -= row.getIcon().getIconWidth();
			int rh = calcTextHeight(row.getLabel(), rowLabelFont, w);
			if (rh > minRowHeight)
				 h += rh;
			else h += minRowHeight;
		}
		return h;
	}
	
	/**
	 * Draw a text in a clipping area
	 * @param g
	 * @param text
	 * @param r
	 */
	protected void drawTextRect(Graphics g, String text, Rectangle r) {
		Shape clipArea = g.getClip();
		g.setClip((int)r.getX(), (int)r.getY(), (int)r.getWidth(), (int)r.getHeight());
		g.drawString(text, (int)r.getX() + 4, (int)r.getY() + 13);
		g.setClip(clipArea);
	}

	/**
	 * @return the iniDate
	 */
	public Date getIniDate() {
		return iniDate;
	}

	/**
	 * @param iniDate the iniDate to set
	 */
	public void setIniDate(Date iniDate) {
		this.iniDate = iniDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}


	/**
	 * @return the padding
	 */
	public int getPadding() {
		return padding;
	}


	/**
	 * @param padding the padding to set
	 */
	public void setPadding(int padding) {
		this.padding = padding;
	}


	/**
	 * @return the rows
	 */
	public List<TimelineRow> getRows() {
		return rows;
	}


	/**
	 * @return the defaultStyle
	 */
	public PeriodStyle getDefaultStyle() {
		return defaultStyle;
	}


	/**
	 * @param defaultStyle the defaultStyle to set
	 */
	public void setDefaultStyle(PeriodStyle defaultStyle) {
		this.defaultStyle = defaultStyle;
	}


	/**
	 * @return the timelineBkColor
	 */
	public Color getTimelineBkColor() {
		return timelineBkColor;
	}


	/**
	 * @param timelineBkColor the timelineBkColor to set
	 */
	public void setTimelineBkColor(Color timelineBkColor) {
		this.timelineBkColor = timelineBkColor;
	}


	/**
	 * @return the rowLabelFonSt
	 */
	public Font getRowLabelFont() {
		return rowLabelFont;
	}


	/**
	 * @param rowLabelFonSt the rowLabelFonSt to set
	 */
	public void setRowLabelFont(Font rowLabelFonSt) {
		this.rowLabelFont = rowLabelFonSt;
	}

	
	/**
	 * Find a period by the mouse position
	 * @param x is the left position according to the panel coordinates
	 * @param y is the top position according to the panel coordinates
	 * @return instance of {@link TimelinePeriod} where mouse is over
	 */
	public TimelinePeriod periodByPoint(int x, int y) {
		if (x < leftColumnWidth + padding)
			return null;

		for (TimelineRow row: getRows()) {
			for (TimelinePeriod period: row.getPeriods()) {
				Rectangle r = period.getRectangle();
				if ((r != null) && (r.contains(x, y)))
					return period;
			}
		}
		return null;
	}

	
	/** {@inheritDoc}
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		// nothing to do
	}


	/** {@inheritDoc}
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		TimelinePeriod period = periodByPoint(e.getX(), e.getY());
		if (period != null) {
			raisePeriodEvent(period, PeriodEventType.CLICK);
		}
	}
	
	
	/**
	 * Notify all listeners about an event in a time line period
	 * @param period is the instance of the {@link TimelinePeriod} where event happened
	 * @param type is the {@link PeriodEventType} identifying the event type
	 */
	protected void raisePeriodEvent(TimelinePeriod period, PeriodEventType type) {
		if (periodListeners == null)
			return;

		PeriodEvent event = new PeriodEvent(period, type);
		for (PeriodListener listener: periodListeners)
			listener.onTimelinePeriodEvent(event);
	}


	/** {@inheritDoc}
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
	}


	/** {@inheritDoc}
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
	}


	/** {@inheritDoc}
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		if (hoverPeriod != null) {
			drawPeriod((Graphics2D)getGraphics(), hoverPeriod, false);
			hoverPeriod = null;
		}
	}


	/** {@inheritDoc}
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
	}


	/** {@inheritDoc}
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		// get the period where mouse is on
		TimelinePeriod period = periodByPoint(e.getX(), e.getY());
	
		// hover period and period are the same ? If so, there is nothing to do
		if (hoverPeriod == period)
			return;

		// previous hover period exists ?
		if (hoverPeriod != null) {
			// redraw period with no hover effect
			Graphics2D g2 = (Graphics2D)getGraphics();
			setAntialiasingGraphics(g2);
			drawPeriod(g2, hoverPeriod, false);
			if (period == null)
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			hoverPeriod = null;
		}

		// exists new period with mouse over it ?
		if ((period != null) && (period.isClickable())) {
			// draw period with hover effect
			Graphics2D g2 = (Graphics2D)getGraphics();
			setAntialiasingGraphics(g2);
			drawPeriod(g2, period, true);
			if (getCursor().getType() != Cursor.HAND_CURSOR)
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			hoverPeriod = period;
		}
	}


}
