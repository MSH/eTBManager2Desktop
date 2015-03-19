/**
 * 
 */
package org.msh.etbm.desktop;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;
import org.msh.xview.swing.layout.TableNormalization;


/**
 * Test case for {@link TableNormalization} class
 * 
 * @author Ricardo Memoria
 *
 */
public class TableNormalizationTest {

	private TableNormalization tbl = new TableNormalization();

	public static void main(String[] args) {
		TableNormalizationTest tbl = new TableNormalizationTest();
		tbl.testSpan();
	}

	
	public void testSpan() {
		tbl.setMaxColumns(4);
		tbl.setWidth(100);
		tbl.addColumn(20, 1, 10, 100, false);
		tbl.addColumn(20, 1, 10, 100, true);
		tbl.addColumn(20, 2, 10, 100, true);
		tbl.addRow();
		tbl.addColumn(20, 1, 10, 100, false);
		tbl.addColumn(20, 2, 10, 100, true);
		tbl.addColumn(20, 1, 10, 100, true);
		int[] cols = tbl.normalize();
		for (int i = 0; i < cols.length; i++) {
			System.out.println("col[" + i + "] = " + cols[i]);
		}
	}
	
	
	/**
	 * Test if normalization values are ok
	 */
	@Test
	public void testNormalization() {
/*		tbl.clear();

		// 1ST TEST - Simple scenario
		// add a column with fixed lenght and span of 2
		tbl.addColumn(300);
		tbl.addRow();
		tbl.addColumn(100);
		tbl.addColumn(150);

		int[] cols = tbl.normalize();

		// expect 2 columns
		assertEquals(2, cols.length);

		// test column sizes
		assertEquals(300, cols[0]);
		assertEquals(150, cols[1]);

		// 2ND TEST - MORE COMPLEX SCENARIO WITH COLUMN SPAN
		tbl.clear();
		tbl.addColumn(300, 3);
		tbl.addRow();
		tbl.addColumn(200, 2);
		tbl.addColumn(50);
		tbl.addRow();
		tbl.addColumn(150);
		tbl.addColumn(50);
		tbl.addColumn(60);
		tbl.addRow();
		tbl.addColumn(50);
		tbl.addColumn(100, 2);
		
		cols = tbl.normalize();

		assertEquals(3, cols.length);
		assertEquals(150, cols[0]);
		assertEquals(50,  cols[1]);
		assertEquals(100, cols[2]);
*/	}
	
	
	/**
	 * Test the column lengths when table has a fixed size
	 */
	@Test
	public void testTableFixedWidth(){
		tbl.clear();

		
		// FIXED WIDTH BIGGER THAN TABLE
		int width = 1000;
		tbl.setWidth(width);
		tbl.addColumn(300, 1, 300, 300, false);
		tbl.addColumn(400, 1, 200, 600, true);
		tbl.addColumn(200, 1, 200, 600, true);
		
		int[] cols = tbl.normalize();

		// check number of columns
		assertEquals(3, cols.length);

		assertEquals(cols[0] + cols[1] + cols[2], width);
		
		// check column sizes
		assertEquals(300, cols[0]);
		assertTrue((cols[1] >= 400) && (cols[1] < 600));
		assertTrue((cols[2] >= 200) && (cols[2] < 600));
		System.out.println(Arrays.toString(cols));
		
		
		// FIXED WIDTH LOWER THAN TABLE
		tbl.clear();
		width = 1000;
		tbl.setWidth(width);
		tbl.addColumn(400, 1, 400, 400, true);
		tbl.addColumn(500, 1, 200, 600, true);
		tbl.addColumn(400, 1, 200, 600, true);
		
		cols = tbl.normalize();

		// check number of columns
		assertEquals(3, cols.length);

		assertEquals(cols[0] + cols[1] + cols[2], width);
		
		// check column sizes
		assertEquals(400, cols[0]);
		assertTrue((cols[1] >= 200) && (cols[1] <= 600));
		assertTrue((cols[2] >= 200) && (cols[2] <= 600));
		System.out.println(Arrays.toString(cols));
	}


	@Test
	public void testSpannedColumns() {
		tbl.clear();

		tbl.addColumn(1000, 3, 800, 1200, true);
		tbl.addRow();
		tbl.addColumn(200, 1, 100, 400, false);
		tbl.addColumn(200, 1, 100, 400, true);
		tbl.addColumn(200, 1, 100, 400, true);
		
		int[] cols = tbl.normalize();
		assertEquals(3, cols.length);
		System.out.println(Arrays.toString(cols));
		assertEquals(cols[0] + cols[1] + cols[2], 1000);
	}
}
