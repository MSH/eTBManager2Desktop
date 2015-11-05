/**
 * 
 */
package org.msh.xview.swing.ui.table;

import org.msh.xview.components.XColumn;
import org.msh.xview.components.XContainer;
import org.msh.xview.swing.ui.ExpressionWrapper;
import org.msh.xview.swing.ui.ViewUI;
import org.msh.xview.swing.ui.ViewUIFactory;

/**
 * Represents a column of the {@link TableUI} view.
 * 
 * @author Ricardo Memoria
 *
 */
public class ColumnUI extends ViewUI<XColumn> {

	private ExpressionWrapper<String> expTitle;
	
	
	/**
	 * Return the title of the column
	 * @return
	 */
	public String getTitle() {
		if (expTitle == null) {
			String s = getView().getTitle();
			
			if (s == null) {
				return null;
			}
			
			expTitle = new ExpressionWrapper<String>(this, s, String.class);
		}
		
		return expTitle.getValue();
	}

	
	/** {@inheritDoc}
	 */
	@Override
	protected void doUpdate() {
		// do nothing by now
	}

    @Override
    protected boolean isComponentVisible() {
        return true;
    }

    /** {@inheritDoc}
	 */
	@Override
	public void createChildren() {
		// do nothing, because the children will be created when the table create its rows
	}


	/**
	 * Create the component of the row relative to this column
	 * @return
	 */
	public ViewUI createRowComponent() {
		XContainer container = (XContainer)getView();
		
		ViewUI view = ViewUIFactory.createUI(container.getViews().get(0));
		return view;
		
/*		if (view instanceof ComponentUI) {
			return (ComponentUI)view;
		}

		List<ComponentUI> lst = XViewUtils.createChildComponentList(view);

		if (lst.size() == 0) {
			throw new RuntimeException("No component found to render column in " + getParent().getId());
		}
		
		return lst.get(0);
*/	}
}
