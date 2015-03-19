/**
 * 
 */
package org.msh.xview.swing.ui;

import java.util.Arrays;
import java.util.Collection;

import javax.el.ValueExpression;

import org.msh.xview.components.XRepeat;
import org.msh.xview.components.XView;

/**
 * @author Ricardo Memoria
 *
 */
public class RepeatUI extends ViewUI<XRepeat>{

	private ValueExpression expValues;
	
	
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
		
		// Expression returns a collection ?
		if (vals instanceof Collection) {
			return (Collection)vals;
		}
		
		if (vals.getClass().isArray()) {
			return Arrays.asList((Object[])vals);
		}
		
		throw new RuntimeException("Unexpected return value to list ");
	}
	

	/** {@inheritDoc}
	 */
	@Override
	protected void doUpdate() {
		// check if there is anything to render
		if (getView().getViews().size() == 0) {
			return;
		}
		
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
		
		for (Object obj: lst) {
			// create data model for this loop
			LocalModelViewUI parentview = new LocalModelViewUI();
			// use the same view of the view
			parentview.setView(getView());
			parentview.getDataModel().setValue(varname, obj);
			addChild(parentview);

			int index = 0;
			for (XView view: getView().getViews()) {
				ViewUI vchild = ViewUIFactory.createUI(view);
				vchild.setId(getId() + index++); 
				vchild.createChildren();
				parentview.addChild(vchild);
				vchild.update();
			}
		}
	}


	/** {@inheritDoc}
	 */
	@Override
	public void createChildren() {
		// do nothing, because the children will be created when updated
	}

}
