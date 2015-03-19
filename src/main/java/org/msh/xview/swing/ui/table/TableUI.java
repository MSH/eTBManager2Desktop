/**
 * 
 */
package org.msh.xview.swing.ui.table;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.el.ValueExpression;
import javax.swing.JComponent;

import org.msh.xview.components.XTable;
import org.msh.xview.components.XTable.RowSelection;
import org.msh.xview.swing.ui.ComponentUI;
import org.msh.xview.swing.ui.ViewUI;


/**
 * A visual component that displays the views in a table format
 * @author Ricardo Memoria
 *
 */
public class TableUI extends ComponentUI<XTable>{

	private ValueExpression expValues;
	private List<ColumnUI> columns;
	private List<RowUI> rows;
	
	
	/** {@inheritDoc}
	 */
	@Override
	public void update() {
		// remove all children when update is called to avoid
		// updating children that will be recreated
		if (!isUpdating()) {
			removeAllChildren();
			createChildren();
		}
		super.update();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setWidth(int width) {
		getTablePanel().setNewWidth(width);
	}

	/** {@inheritDoc}
	 */
	@Override
	protected void doComponentUpdate() {
		// check if there is anything to render
		if (getView().getViews().size() == 0) {
			return;
		}

		// create the column definitions
		createColumnList();
		
		createRowList();
		
		getTablePanel().updateWidth();
	}
	
	/**
	 * Return the list of values
	 * @return
	 */
	public Collection getValues() {
		if (expValues == null) {
			String s = getView().getValues();
			if (s == null) {
				return null;
			}

			expValues = getDataModel().createValueExpression(s, Object.class);
		}
		
		Object vals = expValues.getValue(getELContext());
		if (vals == null) {
			return null;
		}
		
		// Expression returns a collection ?
		if (vals instanceof Collection) {
			return (Collection)vals;
		}
		
		if (vals.getClass().isArray()) {
			return Arrays.asList((Object[])vals);
		}
		
		throw new RuntimeException("Unexpected return value to list ");
	}

	
	/**
	 * Create the list of columns of the table
	 */
	protected void createColumnList() {
		columns = new ArrayList<ColumnUI>();
		searchColumns(this);
		if (columns.size() == 0) {
			throw new RuntimeException("No column found to render in table " + getId());
		}
	}

	
	/**
	 * Create the list of rows of the table
	 */
	protected void createRowList() {
		rows = new ArrayList<RowUI>();

		// get the local variable name to use
		String varname = getView().getVar();
		if (varname == null) {
			return;
		}

		// get the collection to show
		Collection lst = getValues();
		if (lst == null) {
			return;
		}
		
		int index = 0;
		for (Object value: lst) {
			RowUI row = new RowUI(this);
			row.getDataModel().setValue(varname, value);
			row.setIndex(index);
			row.setColumnCount(columns.size());
			rows.add(row);
			addChild(row);

			int colindex = 0;
			for (ColumnUI col: columns) {
				ViewUI comp = col.createRowComponent();
				comp.setMode(ViewMode.COMPACT);
				row.addChild(comp);
				row.setColumnView(colindex, comp);
				comp.setId( comp.getId() + ":" + index);
				comp.createChildren();
				comp.update();
			
				colindex++;
			}
		}
	}
	
	
	/**
	 * Search for views of type {@link ColumnUI} in the children of the view
	 * @param view instance of {@link ViewUI} to search children from
	 */
	private void searchColumns(ViewUI view) {
		if (!view.isContainer()) {
			return;
		}
		
		for (Object child: view.getChildren()) {
			if (child instanceof ColumnUI) {
				columns.add((ColumnUI)child);
			}
			else {
				if (!(child instanceof ComponentUI)) {
					searchColumns((ViewUI)child);
				}
			}
		}
	}
	
	/**
	 * Return the panel that renders the table
	 * @return
	 */
	public TableUIPanel getTablePanel() {
		return (TableUIPanel)getComponent();
	}


	/** {@inheritDoc}
	 */
/*	@Override
	public void createChildren() {
		// do nothing, because the children will be created when updated
	}
*/
	/** {@inheritDoc}
	 */
	@Override
	protected JComponent createComponent() {
		return new TableUIPanel(this);
	}


	/**
	 * @return the columns
	 */
	public List<ColumnUI> getColumns() {
		return columns;
	}


	/**
	 * @return the rows
	 */
	public List<RowUI> getRows() {
		return rows;
	}

	/**
	 * Return true if the check boxes of each row are visible
	 * @return boolean value
	 */
	public boolean isCheckBoxVisible() {
		return getView().getRowSelection() == RowSelection.MULTIPLE;
	}
}
