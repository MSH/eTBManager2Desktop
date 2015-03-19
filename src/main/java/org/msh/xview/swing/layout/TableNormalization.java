package org.msh.xview.swing.layout;

import java.util.ArrayList;
import java.util.List;


/**
 * Normalize the size of columns in a table by the size of each individual
 * cell and its span.
 * <p/>
 * <b>How to use it?</b>
 * <p/>
 * 1. Add as many columns to a row using the method <code>addColumn</code>;<br/>
 * 2. Add new rows you want using the method <code>addRow</code> (It's not necessary
 * to include the first row);<br/>
 * 3. Call the method <code>normalize()</code> and get the ideal column sizes;<br/>
 * 4. If you want to reuse the object, you may call <code>clear()</code> to
 * clear all data and start it over again;
 * <p/>
 * If you want your table to have a fixed width, specify a value greater than 0
 * in the <code>setWidth</code> method, otherwise, leave it equals to 0 and the
 * table width will vary according to the width of the columns. The width of the
 * table will be stretched if the sum of column widths is bigger than the table
 * width.
 * <p/>
 * 
 * @author Ricardo Memoria
 *
 */
public class TableNormalization {

	/**
	 * the fixed width of the table. The value of 0 means it will be ignored
	 */
	private int width;

	private List<List<TableCell>> rows = new ArrayList<List<TableCell>>();
	private int numCols;
	private int maxColumns;
	private List<TableCell> currentRow;

	// the minimum, maximum and current size of the columns 
	// (calculated by the normalize method)
	private int[] mins;
	private int[] maxs;
	private int[] vals;
	private boolean[] autoGrow;

	// store the sum of the minimum, maximum and size of the columns
	private int sumMin;
	private int sumMax;
	private int sumVal;
	
	// the space between the columns
	private int padding;

	
	/**
	 * Default constructor
	 */
	public TableNormalization() {
		super();
		maxColumns = 0;
	}
	
	
	/**
	 * Constructor where the max number of columns is specified
	 * @param maxColumns
	 */
	public TableNormalization(int maxColumns) {
		super();
		this.maxColumns = maxColumns;
	}


	/**
	 * Return the number of columns left for the row, considering the property <code>maxColumns</code>.
	 * <p/>
	 * If the property <code>maxColumns</code> was not initialized with a positive value, the
	 * method raises an {@link IllegalArgumentException} 
	 * 
	 * @return the number of columns available to be included in the current row
	 */
	public int getColumnsLeft() {
		if (maxColumns == 0)
			throw new IllegalArgumentException("The maximum number of columns must be specified");
		return (currentRow == null? maxColumns: maxColumns - currentRow.size());
	}
	
	/**
	 * Return the number of columns included in the current row
	 * @return
	 */
	public int getColumnCount() {
		return currentRow != null? currentRow.size(): 0;
	}

	/**
	 * Add a new column to the current row with no span
	 * @param size is the width in pixels of the column
	 */
	public void addColumn(int size, boolean autoGrow) {
		addColumn(size, 1, 0, 0, autoGrow);
	}

	
	/**
	 * Add a new column size to be calculated in the current row
	 * @param size is the width of the column in the row
	 * @param span is the number of spanned columns. 1 means it's just one column wide,
	 * while 2 means this cell will use the space of two columns
	 */
	public void addColumn(int size, int span, boolean autoGrow) {
		addColumn(size, span, 0, 0, autoGrow);
	}
	
	/**
	 * Add a new column size in the current row. The column size will be normalized
	 * when the method <code>normalize</code> is called.
	 * @param size is the size in pixels of the column
	 * @param span is the number of spanned columns. 1 means it's just one column wide
	 * @param minSize is the minimum size of the column. If minSize is 0, the minSize will be ignored
	 * @param maxSize is the maximum size of the column. If minSize is 0, the maxSize will be ignored
	 * @param autoGrow indicate if the column can grow (true)
	 */
	public void addColumn(int size, int span, int minSize, int maxSize, boolean autoGrow) {
		if (maxSize < size) {
			maxSize = size;
		}

		// get the current row
		List<TableCell> row;

		if (currentRow == null) {
			addRow();
		}

		row = currentRow;

		// maximum number of columns was set ?
		if (maxColumns > 0) {
			// there is space for new columns? if no, move to next row
			if (getColumnsLeft() == 0)
				addRow();

			// check if there is enough room for the row
			if (span > getColumnsLeft())
				span = getColumnsLeft();
		}

		// add a column to the row
		if (span > 1) {
			SpannedParentCell master = new SpannedParentCell(size, span, minSize, maxSize);

			// create child columns
			for (TableCell cell: master.getCells())
				row.add( cell );
		}
		else row.add( new TableCell(size, minSize, maxSize, autoGrow) );

		// update the number of columns of the table
		if (row.size() > numCols)
			numCols = row.size();

		// check if it's in the end of the row
		if ((maxColumns > 0) && (getColumnsLeft() == 0))
			addRow();
	}


	/**
	 * Add a new row. A new row will just be included if the current
	 * row has columns, otherwise, if the row is empty, nothing will
	 * be done
	 */
	public void addRow() {
		if ((currentRow == null) || (currentRow.size() > 0)) {
			currentRow = new ArrayList<TableCell>();
			rows.add(currentRow);
		}
	}
	
	/**
	 * Clear all rows. This method is called to normalize a new table
	 */
	public void clear() {
		rows.clear();
		numCols = 0;
		maxColumns = 0;
		currentRow = null;
		autoGrow = null;
		setWidth(0);
	}
	
	/**
	 * Normalize the table return the size of the columns. The normalization
	 * calculate the optimal value for each column considering the minimum, 
	 * maximum, current size and the column span of each column
	 * @return array of int values with the size of each column
	 */
	public int[] normalize() {
		if (numCols == 0)
			throw new RuntimeException("No column defined to be calculated");

		mins = new int[numCols];
		maxs = new int[numCols];
		vals = new int[numCols];
		autoGrow = new boolean[numCols];

		// store the sum of column sizes
		sumMin = 0;
		sumMax = 0;
		sumVal = 0;

		// 1st stage - loop all columns with no span to calculate min, max and size 
		calcSizesWithoutSpan();

		// 2nd stage - consider the min, max and size of the spanned columns
		for (List<TableCell> row: rows) {
			int i = 0;
			while (i < row.size()) {
				TableCell cell = row.get(i);
				if (cell.getParent() != null) {
					SpannedParentCell parent = cell.getParent();
					int span = parent.getCells().length;
					int ini = i;
					int end = i + span - 1;
					// adjust the current size
					adjustSpannedSize(parent.getSize(), i, end);
					// adjust minimum sizes
					if (parent.getMinSize() > sumArray(mins, ini, end)) {
						increaseSpannedMinimum(parent.getMinSize(), ini, end);
					}
					// adjust maximum sizes
					if ((parent.getMaxSize() > 0) && (parent.getMaxSize() < sumArray(maxs, ini, end))) {
						decreaseSpannedMaximum(parent.getMaxSize(), ini, end);
					}
					i = end + 1;
				}
				else i++;
			}
		}
		
		int totalPadding = (vals.length - 1) * padding;
		sumVal = sumArray(vals);
		sumMax = sumArray(maxs);
		sumMin = sumArray(mins);

		// a fixed width was set ?
		if (width > 0) {
			// check if sum of all columns is different from the fixed width
			if (sumVal != width - totalPadding)
				adjustSize(width - totalPadding);
		}
		else {
			width = sumVal;
		}

		return vals;
	}

	
	/**
	 * @return
	 */
	public int getMinWidth() {
		return sumMin;
	}
	
	
	/**
	 * @return
	 */
	public int getMaxWidth() {
		return sumMax;
	}
	
	
	/**
	 * Calculate the min, max and size of columns with no span 
	 */
	protected void calcSizesWithoutSpan() {
		for (int col = 0; col < numCols; col++) {
			// 1st stage - calculate column widths
			autoGrow[col] = true;

			for (List<TableCell> row: rows) {
				if (col < row.size()) {
					TableCell cell = row.get(col);
					// is a cell with no span, or is a cell with span but the first column of the spans
					if (cell.getParent() == null) {
						int minw, maxw, val;
						minw = cell.getMinSize();
						maxw = cell.getMaxSize();
						val = cell.getSize();
						
						// limit the maximum size
						if (maxw > 20000) {
							maxw = 20000;
						}
						
						if (val < minw)
							val = minw;

						if (minw > mins[col]) {
							mins[col] = minw;
						}
						if ((maxw < maxs[col]) || (maxs[col] == 0))
							maxs[col] = maxw;
						if (val > vals[col])
							vals[col] = val;

						// check if the cell cannot grow
						if (!cell.isAutoGrow()) {
							autoGrow[col] = false;
						}
					}
				}
			}
			sumMin += mins[col];
			sumMax += maxs[col];
			sumVal += vals[col];
		}
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


	/**
	 * @return the maxColumns
	 */
	public int getMaxColumns() {
		return maxColumns;
	}


	/**
	 * @param maxColumns the maxColumns to set
	 */
	public void setMaxColumns(int maxColumns) {
		this.maxColumns = maxColumns;
	}

	
	/**
	 * Increase the minimum values of a range of columns
	 * @param newMin the total minimum value (sum of all values)
	 * @param indexIni the index of the first column (0 is the first)  
	 * @param indexEnd the index of the last column
	 */
	protected void increaseSpannedMinimum(int newMin, int indexIni, int indexEnd) {
		// calculate reference values
		int m = sumArray(mins, indexIni, indexEnd); // sum of minimum values
		int x = sumArray(maxs, indexIni, indexEnd); // sum of maximum values
		int v = sumArray(vals, indexIni, indexEnd); // sum of current values

		if (newMin < m)
			throw new IllegalArgumentException("The minimum value cannot be reduced");

		if ((x > 0) && (newMin > x))
			newMin = x;

		double k = (double)(newMin - m)/(double)(v - m);

		// use this variable to avoid different values when rounding 
		int last = newMin;
		for (int i = indexIni; i <= indexEnd; i++) {
			if (i < indexEnd)
				 mins[i] += Math.round(k * ((double)(vals[i] - mins[i])));
			else mins[i] = last;
			// is the new minimum bigger then the size of the column ?
			if (mins[i] > vals[i])
				vals[i] = mins[i];

			last -= mins[i];
		}
	}

	
	/**
	 * Adjust the size when the spanned size is bigger than the sum of the columns
	 * @param size
	 * @param ini
	 * @param end
	 */
	protected void adjustSpannedSize(int size, int ini, int end) {
		// calculate number of auto-size columns
		int numAutoSize = 0;
		for (int i = ini; i <= end; i++) {
			numAutoSize += autoGrow[i]? 1: 0;
		}

		int numCols = end - ini + 1;
		int pad = padding * (numCols - 1);
		int sum = sumArray(vals, ini, end);
		if (size - pad > sum) {
			// if numAutoSize == 0, then adjust all columns
			double d = numAutoSize == 0? numCols: numAutoSize;
			double dx = (size - sum - pad) / d;
			for (int i = ini; i <= end; i++) {
				if ((numAutoSize == 0) || (autoGrow[i])) {
					vals[i] += dx;
				}
			}
		}
	}
	
	
	/**
	 * Decrease the total maximum size of a range of columns. The new maximum cannot be
	 * lower than the minimum allowed. The maximum of each column is decreased
	 * proportionally to the size of each column 
	 * @param newMax
	 * @param indexIni
	 * @param indexEnd
	 */
	protected void decreaseSpannedMaximum(int newMax, int indexIni, int indexEnd) {
		// calculate reference values
		int m = sumArray(mins, indexIni, indexEnd); // sum of minimum values
		int x = sumArray(maxs, indexIni, indexEnd); // sum of maximum values
		int v = sumArray(vals, indexIni, indexEnd); // sum of current values

		if (newMax > x)
			throw new IllegalArgumentException("The maximum value cannot be increased");

		if (newMax < m)
			newMax = m;
		
		double k = (double)(x - newMax)/(double)(x - v);

		// use this variable to avoid errors of rounding values
		int last = newMax;
		for (int i = indexIni; i <= indexEnd; i++) {
			if (i < indexEnd)
				 maxs[i] -= Math.round( k * ((double)(maxs[i] - vals[i])));
			else maxs[i] = last;
			// is the size of the column bigger than the maximum value ?
			if (vals[i] > maxs[i])
				vals[i] = maxs[i];
			last -= maxs[i];
		}
	}

	
	/**
	 * Change the size of the columns to a new size, respecting the minimum and 
	 * maximum size allowed, i.e, the value of the new size will be truncated
	 * if out of the range of the minimum or maximum size.
	 * @param newSize the new size of the sum of all columns
	 */
	protected void adjustSize(int newSize) {
		if (sumVal == newSize) {
			return;
		}

		// is increasing of the size ?
		if (newSize > sumVal) {
			// increasing of the size
			
			// check if maximum is not defined
			if (sumMax == 0) {
				sumMax = newSize * maxs.length;
				for (int i = 0; i < maxs.length; i++)
					maxs[i] = newSize;
			}

			// count number of auto size columns. Just the auto size columns
			// will stretch. The fixed size won't change its width
			int numAutoSize = 0;
			for (boolean b: autoGrow) {
				numAutoSize += b? 1: 0;
			}

			double k = (double)(newSize - sumVal)/(double)numAutoSize;
			int i2 = 0;
			int last = newSize - sumVal;
			for (int i = 0; i < vals.length; i++) {
				// this column can grow up?
				if (autoGrow[i]) {
					// is not the last item in the array ?
					if (i2 < numAutoSize - 1)
						 vals[i] += Math.round(k);
					else vals[i] += last;
					last -= Math.round(k);
					i2++;
				}
			}
		}
		else {
			// use this variable to avoid different values when rounding double numbers
			int last = newSize;

			// decreasing of the size
			
			// limit to the minimum size
			if (newSize < sumMin)
				sumMin = newSize;

//			double k = (double)(sumVal - newSize)/(double)vals.length;
			double k = (double)(sumVal - newSize)/(double)(sumVal - sumMin);
			for (int i = 0; i < vals.length; i++) {
				// is not the last item in the array ?
				if (i < vals.length - 1)
//					 vals[i] -= k;
					 vals[i] -= k * (vals[i] - mins[i]);
				else vals[i] = last;
				last -= vals[i];
			}
		}
	}
	
	/**
	 * Return the sum of an entire array of integer values
	 * @param values the array containing the values
	 * @return integer value 
	 */
	protected int sumArray(int[] values) {
		return sumArray(values, 0, values.length - 1);
	}

	/**
	 * Return the sum of an integer array from an initial 
	 * index to an ending index
	 * @param values the array containing the values
	 * @param ini the initial index in the array
	 * @param end the final index in the array
	 * @return int value
	 */
	protected int sumArray(int[] values, int ini, int end) {
		int sum = 0;
		for (int i = ini; i <= end; i++) {
			sum += values[i];
		}
		return sum;
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
}
