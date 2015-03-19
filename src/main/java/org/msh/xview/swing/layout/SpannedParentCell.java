package org.msh.xview.swing.layout;


/**
 * Represents a spanned cell inside a table. A master cell controls
 * the width of its cells
 * 
 * @author Ricardo Memoria
 *
 */
public class SpannedParentCell {

	private int size;
	private int minSize;
	private int maxSize;
	private TableCell[] cells;
//	private boolean updatingCellSize;

	public SpannedParentCell(int size, int span, int minSize, int maxSize) {
		this.size = size;
		this.minSize = minSize;
		this.maxSize = maxSize;
		cells = new TableCell[span];

		// initialize the child cells
		int csize = size / span;
		for (int i = 0; i < span; i++)
			cells[i] = new TableCell(csize, i+1, this);
	}


	/**
	 * Return the index of the cell of the table in the spanned cell
	 * @param cell
	 * @return the index position of the {@link TableCell} cell object in the list of columns
	 */
	protected int getCellIndex(TableCell cell) {
		for (int i = 0; i < cells.length; i++)
			if (cells[i] == cell)
				return i;
		return -1;
	}


	/**
	 * Check if cell of the table is the last in the cell span
	 * @param cell reference to the {@link TableCell} instance of the spanned row
	 * and controlled by this object
	 * @return true if cell is the last one
	 */
	protected boolean isLastCell(TableCell cell) {
		return getCellIndex(cell) == (cells.length - 1);
	}
	
	/**
	 * Check if the given cell is the first in the row of spanned cells
	 * @param cell reference to the {@link TableCell} instance of the spanned row
	 * @return
	 */
	protected boolean isFirstCell(TableCell cell) {
		return getCellIndex(cell) == 0;
	}
	
	/**
	 * Handle change of the size in a child cell. Take the sum of all children
	 * up to the given cell and redistribute among all remaining cells
	 * @param cell
	 */
/*	protected void cellSizeChanged(TableCell cell) {
		// update was already called by another method?
		if (updatingCellSize)
			return;
		
		updatingCellSize = true;
		try {
			int index = getCellIndex(cell);
			// cells to be adjusted
			int ca = cells.length - index - 1;
			
			int sum = 0;
			int partsum = 0;

			// calculate the sum up to index and 
			// update the size of the remaining cells
			for (int i = 0; i < cells.length; i++) {
				TableCell aux = cells[i];
				if (i <= index)
					partsum += aux.getSize();
				else {
					int w = (size - partsum) / ca;
					if (w < 0)
						w = 0;
					aux.setSize(w);
				}
				sum += aux.getSize();
			}

			// if there is no adjustment, so the last cell changed its size
			if (ca == 0)
				size = sum;
		}
		finally {
			updatingCellSize = false;
		}
	}
*/

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @return the cells
	 */
	public TableCell[] getCells() {
		return cells;
	}


	/**
	 * @return the minSize
	 */
	public int getMinSize() {
		return minSize;
	}


	/**
	 * @param minSize the minSize to set
	 */
	public void setMinSize(int minSize) {
		this.minSize = minSize;
	}


	/**
	 * @return the maxSize
	 */
	public int getMaxSize() {
		return maxSize;
	}


	/**
	 * @param maxSize the maxSize to set
	 */
	public void setMaxSize(int maxSize) {
		this.maxSize = maxSize;
	}
}
