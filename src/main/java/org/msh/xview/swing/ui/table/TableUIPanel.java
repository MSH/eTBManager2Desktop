package org.msh.xview.swing.ui.table;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.jdesktop.swingx.JXLabel;
import org.msh.etbm.desktop.app.UiConstants;
import org.msh.xview.components.XTable;
import org.msh.xview.components.XTable.RowSelection;
import org.msh.xview.swing.XViewUtils;
import org.msh.xview.swing.layout.TableNormalization;
import org.msh.xview.swing.ui.ComponentUI;
import org.msh.xview.swing.ui.ViewConstants;
import org.msh.xview.swing.ui.ViewUI;



/**
 * Swing implementation of a table to be displayed according to the rows and views
 * referenced in a {@link TableUI} instance of the xview library. This class
 * updates the layout of the table according to the content of the {@link TableUI}
 * instance passed to this class. 
 *  
 * @author Ricardo Memoria
 *
 */
public class TableUIPanel extends JPanel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 7799695543719083719L;

	public static final int PADDING_RIGHT = 4;
	public static final int PADDING_LEFT = 4;
	public static final int PADDING_TOP = 2;
	public static final int PADDING_BOTTOM = 2;
	
	private TableUI table;

	private int numColumns;
	private int[] columnsWidth;
	private JXLabel[] titles;
	private JCheckBox titleCheckbox;
	private JCheckBox[] checkboxes;
	private int titleHeight;
	private RowUI rowOver;
	private RowUI selectedRow;
	private boolean updating;
	
	
	/**
	 * Default action listener of the check box in the title bar
	 */
	private final ActionListener checkboxActionListener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			handleCheckBox((JCheckBox)e.getSource());
		}
	};


	/**
	 * Default constructor
	 * @param tableui is the instance of the {@link TableUI} linked to this table
	 */
	public TableUIPanel(TableUI table) {
		this.table = table;
		setSize(300, 50);
		setLayout(null);
		addMouseListener(this);
		addMouseMotionListener(this);
	}

	/**
	 * Update the width of the panel and create or destroy components that
	 * are displayed in the table
	 */
	protected void updateWidth() {
		createTitles();
		updateCheckboxes();
		calculateWidth(0);
		setBackground(table.getBackgroundColor());
		
		// initialize checkbox content
		if (checkboxes != null) {
			int index = 0;
			for (RowUI row: table.getRows()) {
				checkboxes[index].setSelected( row.isSelected() );
				index++;
			}
		}
	}

	
	/**
	 * Change the width of the panel and position the components
	 * @param width
	 */
	protected void setNewWidth(int width) {
		width = calculateWidth(width);
		int xo = PADDING_LEFT;
		int yo = PADDING_TOP;

		// update title position
		titleHeight = 0;
		int index = 0;

		// place the title check box
		if (titleCheckbox != null) {
			titleCheckbox.setLocation(xo, yo + 1);
			titleHeight = titleCheckbox.getHeight();
			xo += columnsWidth[index] + ViewConstants.COLUMN_SPACE;
			index++;
		}
		
		// place the titles
		for (JXLabel title: titles) {
			int h = XViewUtils.adjustHeight(title, columnsWidth[index]);
			title.setLocation(xo, yo);

			xo += columnsWidth[index] + ViewConstants.COLUMN_SPACE;
			if (h > titleHeight) {
				titleHeight = h;
			}
			index++;
		}
		
		// place the rows
		yo += titleHeight + ViewConstants.ROW_SPACE;
		int innerWidth = width - PADDING_LEFT - PADDING_RIGHT;
		int rowIndex = 0;
		for (RowUI row: table.getRows()) {
			xo = PADDING_LEFT;
			// place the check box beside the row
			if (checkboxes != null) {
				checkboxes[rowIndex].setLocation(xo, yo + 1);
				xo += columnsWidth[0] + ViewConstants.COLUMN_SPACE;
			}
			int h = row.updateViewsPosition(xo, yo, innerWidth);
			yo += h + ViewConstants.ROW_SPACE;
			rowIndex++;
		}
		
		yo += PADDING_BOTTOM - ViewConstants.ROW_SPACE + 2;
		setSize(width, yo);
	}


	/**
	 * Called when user clicks on the title check box
	 */
	protected void handleCheckBox(JCheckBox chk) {
		if (updating) {
			return;
		}

		if (chk == titleCheckbox) {
			// get the row of the check box clicked
			for (RowUI row: table.getRows()) {
				row.setSelected(chk.isSelected());
			}

			for (JCheckBox chkrow: checkboxes) {
				chkrow.setSelected(chk.isSelected());
			}
		}
		else {
			for (int i = 0; i < checkboxes.length; i++) {
				if (checkboxes[i] == chk) {
					RowUI row = table.getRows().get(i);
					row.setSelected(chk.isSelected());
				}
			}
		}
		table.update();
		table.notifySizeChanged();
	}


	/**
	 * Return true if check boxes are visible
	 * @return boolean value
	 */
	public boolean isCheckboxVisible() {
		XTable tbl = table.getView();
		return tbl == null? false: tbl.getRowSelection() == RowSelection.MULTIPLE; 
	}


	/** {@inheritDoc}
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		int dy = (ViewConstants.COLUMN_SPACE / 2);

		if (columnsWidth == null)
			return;

		// draw the selected row
		if (selectedRow != null) {
			g.setColor(new Color(220,220,220));
			g.fillRect(0, 
					selectedRow.getTop() - dy, 
					getWidth(), 
					selectedRow.getHeight() + ViewConstants.ROW_SPACE);
		}
		
		if ((rowOver != null) && (rowOver != selectedRow)) {
			g.setColor(new Color(240,240,240));
			g.fillRect(1, 
					rowOver.getTop() - dy, 
					getWidth(),
					rowOver.getHeight() + ViewConstants.ROW_SPACE);
		}

		// set the color of the border to a gray line
		g.setColor(new Color(160,160,160));

		// draw left line
		int bottom = getHeight();
		g.drawLine(0, 0, 0, bottom-1);
		g.drawLine(0, titleHeight + dy + 2, getWidth(), titleHeight + dy + 2);
		// draw vertical lines
		int xo = 0;
		for (int i = 0; i < columnsWidth.length; i++) {
			g.drawLine(xo, 0, xo, getHeight());
			xo += columnsWidth[i] + ViewConstants.COLUMN_SPACE;
		}
		
		// draw horizontal lines
		int yo = titleHeight + PADDING_TOP + ViewConstants.ROW_SPACE;
		int index = 0;
		List<RowUI> rows = table.getRows();
		for (RowUI row: rows) {
			if (index == rows.size() - 1) {
				yo = getHeight() - dy - 1;
			}
			else {
				yo += row.getHeight();
			}
			g.drawLine(0, yo + dy, getWidth(), yo + dy);
			yo += ViewConstants.ROW_SPACE;
			index++;
		}
		g.drawLine(0, 0, getWidth(), 0);
		g.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight());
	}

	
	/**
	 * Calculate the sizes of columns, rows and the table
	 */
	protected int calculateWidth(int width) {
		TableNormalization tbl = new TableNormalization();
		tbl.setPadding(ViewConstants.COLUMN_SPACE);
		if (width > 0) {
			tbl.setWidth(width - PADDING_LEFT - PADDING_RIGHT);
		}
		
		// check boxes are enabled?
		if (titleCheckbox != null) {
			tbl.addColumn(titleCheckbox.getWidth(), 1, titleCheckbox.getWidth(), 0, false);
		}
		
		// add the titles to calculate best size
		if (titles == null) {
			createTitles();
		}
		for (JXLabel title: titles) {
			int w = XViewUtils.calcTextWidth(title);
			tbl.addColumn(w, 1,
					(int)title.getMinimumSize().getWidth(),
					(int)title.getMaximumSize().getWidth(),
					true);
		}
		
		// loop in each row
		if (table.getRows() != null) {
			for (RowUI row: table.getRows()) {
				
				tbl.addRow();
				// add space for the check box, if available
				if (titleCheckbox != null) {
					tbl.addColumn(titleCheckbox.getWidth(), 1, titleCheckbox.getWidth(), 0, false);
				}
				
				// get the components for each column of the row
				for (ViewUI col: row.getColumns()) {
					if (col != null)  {
						List<ComponentUI> lst = XViewUtils.createChildComponentList(col);
						if (lst.size() > 1) { 
							throw new RuntimeException("No more than 1 component per table cell is supported");
						}
						
						if (lst.size() > 0) {
							ComponentUI comp = lst.get(0);
							tbl.addColumn((int)comp.getComponent().getPreferredSize().getWidth(), comp.getColSpan(), comp.getMinWidth(), comp.getMaxWidth(), comp.isAutoGrow());
						}
						else tbl.addColumn(8, 1, 8, 50000, true);
					}
					else {
						// if the view is null in the column, so add an empty column with default values
						tbl.addColumn(8, 1, 8, 50000, true);
					}
				}
			}
		}

		// calculate columns size
		columnsWidth = tbl.normalize();
		numColumns = tbl.getColumnCount();

		width = 0;
		// calculate panel size
		for (int colWidth: columnsWidth) {
			width += colWidth;
		}
		width += (ViewConstants.COLUMN_SPACE * (numColumns - 1)) + PADDING_LEFT + PADDING_RIGHT;
		
		setSize(width, 4);
		setMinimumSize(new Dimension(tbl.getMinWidth(), 4));
		setMaximumSize(new Dimension(tbl.getMaxWidth(), 50000));
		
		return width;
	}

	
	/**
	 * Create the titles of the table
	 */
	protected void createTitles() {
		// destroy previous labels
		if (titles != null) {
			// avoid recreate it if the number of columns is the same
			if (titles.length == table.getColumns().size()) {
				// update the titles
				for (int i = 0; i < table.getColumns().size(); i++) {
					titles[i].setText(table.getColumns().get(i).getTitle());
				}
				return;
			}
			
			for (JXLabel lbl: titles) {
				remove(lbl);
			}
		}
		
		if (table.getColumns() == null) {
			return;
		}

		// create new labels
		int index = 0;
		titles = new JXLabel[table.getColumns().size()];
		for (ColumnUI col: table.getColumns()) {
			String s = col.getTitle();
			JXLabel lbl = new JXLabel(s);
			add(lbl);
			lbl.setLineWrap(true);
			lbl.setVerticalAlignment(SwingConstants.TOP);
			lbl.setFont(UiConstants.fieldLabel);
			titles[index++] = lbl;
		}
	}
	
	
	/**
	 * Update the state of the check boxes - create or destroy them according
	 * to the table row selection (must be MULTIPLE to enable the check boxes)
	 */
	protected void updateCheckboxes() {
		// check boxes are enabled ?
		if (table.getView().getRowSelection() == RowSelection.MULTIPLE) {
			if (titleCheckbox == null) {
				titleCheckbox = new JCheckBox();
				titleCheckbox.setSize(24, 18);
				titleCheckbox.addActionListener(checkboxActionListener);
				add(titleCheckbox);
			}
			
			if (checkboxes == null) {
				int rowCount = table.getRows().size();
				checkboxes = new JCheckBox[rowCount];
				for (int i = 0; i < rowCount; i++) {
					checkboxes[i] = new JCheckBox();
					checkboxes[i].setSize(24, 18);
					checkboxes[i].addActionListener(checkboxActionListener);
					add(checkboxes[i]);
				}
			}
		}
		else {
			// checkboxes shall not be displayed
			
			// remove the title check box
			if (titleCheckbox != null) {
				remove(titleCheckbox);
				titleCheckbox = null;
			}
			
			// remove all other checkboxes
			if (checkboxes != null) {
				for (JCheckBox chk: checkboxes) {
					remove(chk);
				}
				checkboxes = null;
			}
		}
	}
	
	
	
	/**
	 * Update the size information of a column. The values will be updated according to 
	 * the current values of the column.
	 * 
	 * @param index is the column index
	 * @param min is the minimum size of the column
	 * @param width is the size of the column
	 * @param max is the maximum size of the column
	 */
/*	private void updateSizes(int index, int min, int width, int max) {
		if (width > columnsWidth[index])
			columnsWidth[index] = width;
		if (min > minWidths[index])
			minWidths[index] = min;
		if ((maxWidths[index] == 0) || (max < maxWidths[index]))
			maxWidths[index] = max;
	}
*/

	/**
	 * Update the components that display the titles of the table 
	 */
/*	protected void updateTitles(int width) {
		if ((titles == null) || (titles.length != numColumns)) {
			if (titles != null) {
				for (JXLabel lbl: titles) {
					remove(lbl);
				}
			}
			// create label for titles
			List<ColumnUI> cols = table.getColumns();

			titles = new JXLabel[ cols.size() ];
			for (int i = 0; i < titles.length; i++) {
				String s = getTitleString(i);
				titles[i] = new JXLabel();
				titles[i].setLineWrap(true);
				titles[i].setText(s);
				titles[i].setFont( UiConstants.fieldLabel );
				add(titles[i]);
			}
		}

		int xo;
		// is the initial index of the column widths
		int index;
		// update the check box in the title bar
		if (isSelectableRows()) {
			if (titleCheckbox == null) {
				titleCheckbox = new JCheckBox();
				add(titleCheckbox);
				titleCheckbox.setSize(titleCheckbox.getPreferredSize());
				titleCheckbox.addActionListener(checkboxActionListener);
			}
			titleCheckbox.setLocation(PADDING_LEFT, PADDING_TOP);
			xo = columnsWidth[0];
			index = 1;
		}
		else {
			if (titleCheckbox != null) {
				remove(titleCheckbox);
				titleCheckbox = null;
			}
			xo = 0;
			index = 0;
		}
		
		// position the titles
		int yo = PADDING_TOP;
		
		// calculate the height of the titles
		int h = 22;
		for (int i = 0; i < titles.length; i++) {
			int colwidth = columnsWidth[i + index] - PADDING_LEFT - PADDING_RIGHT;
			int th = XViewUtils.calcTextHeight(titles[i], colwidth);
			titles[i].setBounds(xo + PADDING_LEFT, yo, colwidth, th);
			xo += columnsWidth[i + index];
			if (th > h)
				h = th;
		}
		titleHeight = h;
	}
*/
	/**
	 * Change the table width setting a new width
	 * @param width is the new width of the table
	 */
/*	protected void updateWidth(int width) {
		if (columnsWidth == null) {
			setSize(width, 1);
			return;
		}

		// adjust the new width
		int mw = (int)getMinimumSize().getWidth();
		int xw = (int)getMaximumSize().getWidth();
		if (width > xw)
			width = xw;
		if (width < mw)
			width = mw;
		int currWidth = getWidth();
		int dx = width - currWidth;

		// adjust the size of the columns to the new width
		// is reduction of the size ?
		if (dx < 0) {
			// reduction
			double k = (double)(-dx) / (double)(currWidth - mw);
			for (int i = 0; i < columnsWidth.length; i++)
				columnsWidth[i] -= k * (columnsWidth[i] - minWidths[i]);
		}
		else {
			// increasing
			double k = (double)(dx) / (double)(xw - currWidth);
			for (int i = 0; i < columnsWidth.length; i++)
				columnsWidth[i] += k * (maxWidths[i] - columnsWidth[i]);
		}

		// update title components
		updateTitles(width);

		// position the components
		int xo = 0;
		int yo = titleHeight + 4;
		for (RowUI row: tableui.getRows()) {
			row.setPosition(xo, yo);
			row.setWidth(width);
			yo += row.getHeight();
		}
		
		setSize(width, yo);
		setBackground(getParent().getBackground());

	}
*/
	
	/**
	 * Return the title to be displayed in the column
	 * @param index is the index of the column
	 * @return the displayable text in the title
	 */
/*	private String getTitleString(int index) {
		return tableui.getTitle(index);
		XView view = tableui.getView().getViews().get(index);
		if (view instanceof XField)
			return ((XField) view).getLabel();
		if (view instanceof XSection)
			return ((XSection)view).getTitle();
		
		return "Undefined";
	}
*/	
	
	/**
	 * Return true if the rows will contain a check box to be selected
	 * @return boolean value
	 */
/*	protected boolean isSelectableRows() {
		return tableui.getView().getRowSelection() == RowSelection.MULTIPLE;
	}
*/

	/**
	 * Return the width of each column in the table, including the 
	 * selector column if available
	 * @return the columnsWidth
	 */
	public int[] getColumnsWidth() {
		return columnsWidth;
	}

	
	/**
	 * Search for the row where the given point in relation to the table coordinate is over
	 * @param x is the left position
	 * @param y is the top position
	 * @return the instance of {@link RowUI} object where point is over
	 * or null if point is not over the row.
	 */
	protected RowUI findRowByPoint(int x, int y) {
		for (RowUI row: table.getRows()) {
			if (row.isPointInRow(x, y)) {
				return row;
			}
		}
		return null;
	}


	/** {@inheritDoc}
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}


	/** {@inheritDoc}
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		if (table.getView().getRowSelection() == RowSelection.SINGLE) {
			RowUI row = findRowByPoint(e.getX(), e.getY());
			if (row != null) {
				selectedRow = row;
				row.raiseEvent("row-select", row.getDataModel().getValue( table.getView().getVar() ));
				repaint();
			}
		}
	}


	/** {@inheritDoc}
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	/** {@inheritDoc}
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	/** {@inheritDoc}
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(MouseEvent e) {
		updateMouseOver(e);
	}


	/** {@inheritDoc}
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}


	/**
	 * Update the hover row of the table. The hover row is the one that the mouse is over
	 * @param e the instance of {@link MouseEvent} containing the mouse position
	 */
	protected void updateMouseOver(MouseEvent e) {
		if (table.getView().getRowSelection() == RowSelection.SINGLE) {
			RowUI row = findRowByPoint(e.getX(), e.getY());
			if (row != null)
				 setCursor(new Cursor(Cursor.HAND_CURSOR));
			else setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

			if (row != rowOver) {
				rowOver = row;
				repaint();
			}
		}
	}
	
	/** {@inheritDoc}
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		updateMouseOver(e);
	}

	protected void setCheckBoxState(int index, boolean value) {
		if ((checkboxes == null) || (updating)) {
			return;
		}

		updating = true;
		try {
			checkboxes[index].setSelected(value);
		} finally {
			updating = false;
		}
	}
}
