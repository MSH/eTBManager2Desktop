/**
 * 
 */
package org.msh.xview.swing.ui.table;

import java.util.List;

import org.msh.xview.swing.XViewUtils;
import org.msh.xview.swing.ui.ComponentUI;
import org.msh.xview.swing.ui.LocalModelViewUI;
import org.msh.xview.swing.ui.ViewConstants;
import org.msh.xview.swing.ui.ViewUI;

/**
 * Represents a row of the table
 * @author Ricardo Memoria
 *
 */
public class RowUI extends LocalModelViewUI {

	private int left;
	private int top;
	private int width;
	private int height;
	private int index;
	private ViewUI[] columns;
	
	
	public RowUI(TableUI table) {
		super();
		setView(table.getView());
		getDataModel().setValue("row", this);
	}
	

	/**
	 * Set the view of the corresponding column
	 * @param col is the column index starting at 0
	 * @param view is the instance of {@link ViewUI}
	 */
	public void setColumnView(int col, ViewUI view) {
		columns[col] = view;
	}
	
	/**
	 * Set the number of columns
	 * @param num is the number of columns
	 */
	public void setColumnCount(int num) {
		columns = new ViewUI[num];
	}
	
	/**
	 * Return the views that are part of each column
	 * @return
	 */
	public ViewUI[] getColumns() {
		return columns;
	}
	
	/**
	 * Return the number of columns of the table
	 * @return int value
	 */
	public int getColumnCount() {
		return columns.length;
	}
	
	
	/**
	 * Return the instance of the {@link TableUI} that this row belongs to
	 * @return
	 */
	public TableUI getTable() {
		return (TableUI)getParent();
	}
	
	
	/**
	 * Update the component bounds. The height is calculated when the position
	 * @param left
	 * @param top
	 * @param width
	 */
	protected int updateViewsPosition(int left, int top, int width) {
		this.left = left;
		this.top = top;
		this.width = width;

		int h = 0;
		int index = getTable().isCheckBoxVisible()? 1: 0;
		int[] colwidth = getTable().getTablePanel().getColumnsWidth();

		
		// set position of the components
		for (ViewUI view: columns) {
			if (view != null) {
				// by now, just one component per view is supported to be displayed in the
				// cell of the table
				List<ComponentUI> comps = XViewUtils.createChildComponentList(view);
				if (comps.size() > 0) {
					ComponentUI comp = comps.get(0);
					comp.setComponentLocation(left, top);
					comp.setWidth(colwidth[index]);
					int comph = comp.getHeight();
					if (comph > h) {
						h = comph;
					}
				}
			}

			left += colwidth[index] + ViewConstants.COLUMN_SPACE;
			index++;
		}
		
		this.height = h;
		
		return h;
	}

	
	/**
	 * Return true if the given coordinates is over the row
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isPointInRow(int x, int y) {
		return (x > left) && (y > top) && (x - left < getWidth()) && (y - top < getHeight());
	}
	

	/**
	 * @return the left
	 */
	public int getLeft() {
		return left;
	}

	/**
	 * @return the top
	 */
	public int getTop() {
		return top;
	}

	/**
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}


	/** {@inheritDoc}
	 */
	@Override
	protected void doUpdate() {
		getTable().getTablePanel().setCheckBoxState(index, isSelected());
	}


	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}


	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	
	
	/**
	 * Get the field value in the data model
	 * @return
	 */
	public boolean isSelected() {
		String fld = getTable().getView().getRowCheckBoxField();
		
		if (fld == null)
			return false;

		Object value = getDataModel().getValue(fld);
		return value != null ? (Boolean)value: false;
	}

	
	/**
	 * Set the value of the field in the data model assigned to this component
	 * @param value is the field value to be set 
	 */
	public void setSelected(boolean value) {
		beginUpdate();
		try {
			String fld = getTable().getView().getRowCheckBoxField();
			
			if (fld == null) {
				throw new RuntimeException("Property 'checkbox-column-field' not set in table " + getId());
			}
			
			getDataModel().setValue(fld, value);
		} finally {
			finishUpdate();
		}
	}

}
