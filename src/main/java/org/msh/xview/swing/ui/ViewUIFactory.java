package org.msh.xview.swing.ui;

import org.msh.xview.components.XButton;
import org.msh.xview.components.XColumn;
import org.msh.xview.components.XField;
import org.msh.xview.components.XForm;
import org.msh.xview.components.XHeader1;
import org.msh.xview.components.XHeader2;
import org.msh.xview.components.XHeader3;
import org.msh.xview.components.XLabel;
import org.msh.xview.components.XParagraph;
import org.msh.xview.components.XRegion;
import org.msh.xview.components.XRepeat;
import org.msh.xview.components.XSection;
import org.msh.xview.components.XTable;
import org.msh.xview.components.XView;
import org.msh.xview.swing.ui.table.ColumnUI;
import org.msh.xview.swing.ui.table.TableUI;

/**
 * Factory to create new UI components to display the content of the form
 * in a swing panel
 * 
 * @author Ricardo Memoria
 *
 */
public class ViewUIFactory {

	public static ViewUI createUI(XView view) {
		ViewUI viewui = defaultFactory(view);
		viewui.setView(view);
		return viewui;
	}
	
	
	private static ViewUI defaultFactory(XView view) {
		if (view instanceof XField)
			return new FieldUI();

		if (view instanceof XForm)
			return new FormUI();

		if (view instanceof XSection)
			return new SectionUI();
		
		if (view instanceof XRegion)
			return new RegionUI();
		
		if (view instanceof XButton)
			return new ButtonUI();
		
		if (view instanceof XRepeat)
			return new RepeatUI();
		
		if (view instanceof XHeader1)
			return new Header1UI();
		
		if (view instanceof XHeader2)
			return new Header2UI();
		
		if (view instanceof XHeader3)
			return new Header3UI();

		if (view instanceof XLabel)
			return new LabelUI();
		
		if (view instanceof XParagraph)
			return new ParagraphUI();
		
		if (view instanceof XTable)
			return new TableUI();
		
		if (view instanceof XColumn)
			return new ColumnUI();
		
		throw new IllegalArgumentException("ViewUIFactory not supported for view " + view.getClass().toString());
	}
}
