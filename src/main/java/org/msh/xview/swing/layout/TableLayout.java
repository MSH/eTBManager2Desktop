package org.msh.xview.swing.layout;

import java.util.ArrayList;
import java.util.List;

import org.msh.xview.components.XView;
import org.msh.xview.swing.ui.ComponentUI;
import org.msh.xview.swing.ui.ViewConstants;

/**
 * Position the view elements of the container in a table layout.
 * The container where layout will be adjusted will be passed as
 * argument in the constructor. 
 * @author Ricardo Memoria
 *
 */
public class TableLayout {

	private static final int DEFAULT_PADDING = 4;

	/**
	 * The view component to have layout applied to its children
	 */
	private ComponentUI<XView> view;
	/**
	 * The width of the layout
	 */
	private int width;
	private int columnSpace = ViewConstants.COLUMN_SPACE;
	private int rowSpace = ViewConstants.ROW_SPACE;
	private int paddingLeft = DEFAULT_PADDING;
	private int paddingTop = DEFAULT_PADDING;
	private int paddingRight = DEFAULT_PADDING;
	private int paddingBottom = DEFAULT_PADDING;

	private int columnCount;

	// list of the views to be adjusted in the layout (mounted inside the class)
	private List<ComponentUI> views = new ArrayList<ComponentUI>();
	
	// temporary variables used inside the class
	private int[] cols;
	private int left, top;
	private int colindex;
	private int rowheight;

	
	public TableLayout(ComponentUI view, int columnCount) {
		this.view = view;
		this.columnCount = columnCount;
	}

	
	/**
	 * Calculate the width of the table considering the column widths and its padding
	 * @return
	 */
	public int calcWidth() {
		calcColumnSizes();
		// update the total width, if changed
		if (cols.length == 0)
			return paddingLeft + paddingRight;
	
		int colsum = 0;
		for (int size: cols) {
			if (colsum > 0)
				colsum += columnSpace;
			colsum += size;
		}
		int w = paddingLeft + paddingRight + colsum;
		return w;
	}


	/**
	 * Update the position of the children of the view in a table layout
	 * 
	 * @return the height of the view
	 */
	public int updateLayout() {
		// create the list of views to fit in the layout
		views.clear();
		views = view.createChildComponentList();
		
		if (views.size() == 0)
			return paddingTop + paddingBottom;

		width = calcWidth();

		int h = updateComponentPosition();

		// there is any column?
		if (cols.length == 0)
			return h;

		return h;
	}



	/**
	 * Calculate the width of the column sizes based on the child views of the view.
	 * The property <code>cols</code> is update with the width of each column
	 */
	protected void calcColumnSizes() {
		TableNormalization tbl = new TableNormalization(columnCount);
		
		// calculate the number of columns and the view sizes for each column
		for (ComponentUI comp: views) {
			// component shall start in a new row ?
			if (comp.isForceNewRow()) {
				tbl.addRow();
			}
			// add view in the table
			int w = comp.getWidth();
			int span = comp.getColSpan();
			
			tbl.addColumn(w, span, comp.getMinWidth(), comp.getMaxWidth(), comp.isAutoGrow());
		}

		// set the width of the client area
		if (width > 0) {
			int dx = paddingLeft + paddingRight;
			if (columnCount > 1)
				dx += (columnCount - 1) * columnSpace;
			tbl.setWidth( width - dx);
		}
		
		tbl.setPadding(columnSpace);

		// calculate the column sizes
		cols = tbl.normalize();
	}

	/**
	 * update the component positions
	 */
	protected int updateComponentPosition() {
		// initialize variables for component position
		initialize();

//		System.out.println("*** BEGIN: Updating layout of " + view.getId());
		
		// update component positions
		for (ComponentUI child: views) {
			// component must start in a new row ?
			if (child.isForceNewRow()) {
				startNewRow();
			}

			// calculate width of the component
			int span = child.getColSpan();
			if (span < 1)
				span = 1;
			int w;
			if (span > 1)
				 w = getColumnSize(span);
			else w = getColumnSize();
	
			// update position and return its height
			child.setComponentLocation(left, top);
			child.setWidth(w);
			
//			System.out.println("comp=" + child.getId() + ", x=" + left + ", y=" + top + ", w=" + w);

			addColumn(w, child.getHeight(), span);
		}
//		System.out.println("*** END: Updating layout of " + view.getId());

		// update total height with row height plus padding bottom
		top += rowheight + paddingBottom;
		
		return top;
	}

	
	/**
	 * Initialize the variables to position the views in a table layout
	 */
	protected void initialize() {
		left = paddingLeft;
		top = paddingTop;
		colindex = 0;
		rowheight = 0;
	}
	
	/**
	 * Start a new row in the table layout
	 */
	protected void startNewRow() {
		colindex = 0;
		top += rowheight + rowSpace;
		left = paddingLeft;
		rowheight = 0;
	}

	/**
	 * Return the size of the current column
	 * @return width in pixels of the current column
	 */
	protected int getColumnSize() {
		return cols[colindex];
	}

	/**
	 * Return the number of remaining columns in the current row
	 * @return number of columns left
	 */
	protected int getRemainingCols() {
		return columnCount - colindex;
	}
	
	/**
	 * Move the index to the next column. If it's at the end of the
	 * row, a new row is included
	 * @return true if moved to a new row
	 */
	protected boolean addColumn(int width, int height, int span) {
		if (height > rowheight)
			rowheight = height;

		if (getRemainingCols() > span) {
			colindex += span;
			left += width + columnSpace;
			return false;
		}
		
		startNewRow();
		return true;
	}
	
	/**
	 * Return the total width of a group of spanned columns from
	 * the current column
	 * @param span
	 * @return
	 */
	protected int getColumnSize(int span) {
		int end = span;
		if (end > getRemainingCols())
			end = getRemainingCols();

		int w = 0;
		for (int i = 0; i < end; i++) {
			if (i < end - 1)
				w += columnSpace;
			w += cols[colindex + i];
		}
		return w;
	}
	
	/**
	 * @return the columnPadding
	 */
	public int getColumnSpae() {
		return columnSpace;
	}

	/**
	 * @param columnPadding the columnPadding to set
	 */
	public void setColumnSpace(int columnSpace) {
		this.columnSpace = columnSpace;
	}

	/**
	 * @return the paddingLeft
	 */
	public int getPaddingLeft() {
		return paddingLeft;
	}

	/**
	 * @param paddingLeft the paddingLeft to set
	 */
	public void setPaddingLeft(int paddingLeft) {
		this.paddingLeft = paddingLeft;
	}

	/**
	 * @return the paddingTop
	 */
	public int getPaddingTop() {
		return paddingTop;
	}

	/**
	 * @param paddingTop the paddingTop to set
	 */
	public void setPaddingTop(int paddingTop) {
		this.paddingTop = paddingTop;
	}

	/**
	 * @return the paddingRight
	 */
	public int getPaddingRight() {
		return paddingRight;
	}

	/**
	 * @param paddingRight the paddingRight to set
	 */
	public void setPaddingRight(int paddingRight) {
		this.paddingRight = paddingRight;
	}

	/**
	 * @return the paddingBottom
	 */
	public int getPaddingBottom() {
		return paddingBottom;
	}

	/**
	 * @param paddingBottom the paddingBottom to set
	 */
	public void setPaddingBottom(int paddingBottom) {
		this.paddingBottom = paddingBottom;
	}

	/**
	 * @return the columnCount
	 */
	public int getColumnCount() {
		return columnCount;
	}

	/**
	 * @param columnCount the columnCount to set
	 */
	public void setColumnCount(int columnCount) {
		this.columnCount = columnCount;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}
}
