package org.msh.xview.swing.layout;



/**
 * Store the cell size of the table. To be temporarily used inside {@link TableNormalization} class.
 * The properties <code>minSize</code> and <code>maxSize</code> are ignored if values are 0.
 * 
 * @author Ricardo Memoria
 *
 */
public class TableCell {

	private int size;
	private int spanIndex;
	private SpannedParentCell parent;
	private int minSize;
	private int maxSize;
	private boolean autoGrow;


	public TableCell(int size, int minSize, int maxSize, boolean autoGrow) {
		super();
		this.size = size;
		this.minSize = minSize;
		this.maxSize = maxSize;
		this.autoGrow = autoGrow;
	}
	
	
	public TableCell(int size, int spanIndex, SpannedParentCell parent) {
		super();
		this.size = size;
		this.spanIndex = spanIndex;
		this.parent = parent;
	}

	/**
	 * Return true if the column width is flexible, i.e, size can vary
	 * @return true if column can be resized
	 */
	public boolean isLastSpannedCell() {
		if (parent == null)
			return false;

		return parent.isLastCell(this);
	}

	
	/**
	 * Calculate the minimum size a cell can take, considering its span and minimum size
	 * @return minimum size
	 */
	public int calcMinSize() {
		if (parent != null) {
			if (parent.isLastCell(this))
				return getSize();
			else return 1;
		}

		return getMinSize();
	}
	
	
	/**
	 * Calculate the maximum size a cell can take, considering its span and maximum size
	 * @return
	 */
	public int calcMaxSize() {
		if (parent != null) {
			if (parent.isFirstCell(this))
				return getMaxSize();
			else return parent.getSize() - parent.getCells().length;
		}
		
		return getMaxSize();
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}
	/**
	 * @param size the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

	/**
	 * @return the spanIndex
	 */
	public int getSpanIndex() {
		return spanIndex;
	}
	/**
	 * @param spanIndex the spanIndex to set
	 */
	public void setSpanIndex(int spanIndex) {
		this.spanIndex = spanIndex;
	}


	/**
	 * @return the parent
	 */
	public SpannedParentCell getParent() {
		return parent;
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


	/**
	 * @return the autoGrow
	 */
	public boolean isAutoGrow() {
		return autoGrow;
	}


	/**
	 * @param autoGrow the autoGrow to set
	 */
	public void setAutoGrow(boolean autoGrow) {
		this.autoGrow = autoGrow;
	}

}
