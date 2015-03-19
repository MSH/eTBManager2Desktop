/**
 * 
 */
package org.msh.xview.swing.ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import org.msh.xview.components.XSection;
import org.msh.xview.swing.layout.TableLayout;


/**
 * @author Ricardo Memoria
 *
 */
public class SectionUI extends ComponentUI<XSection> {

	private static final int TITLE_HEIGHT = 20;
	private static final int MIN_HEIGHT = 22;
	private static final int PADDING_BORDER = 8;

	int width;
	
	// the title of the section
	private ExpressionWrapper<String> expTitle;

	
	/**
	 * Return the title to be displayed in the section
	 * @return
	 */
	public String getTitle() {
		if (expTitle == null) {
			expTitle = new ExpressionWrapper(this, getView().getTitle(), String.class);
		}
		
		return expTitle.getValue();
	}

	
	/** {@inheritDoc}
	 */
	@Override
	protected JComponent createComponent() {
		JPanel panel = new JPanel();
		panel.setLayout(null);
		if (getView().isShowBorder()) {
			String s = getTitle();
			panel.setBorder(new TitledBorder(new LineBorder(new Color(180,180,180), 1, true), s));
		}
		panel.setMinimumSize(new Dimension(50, 20));
		return panel;
	}

	/** {@inheritDoc}
	 */
	@Override
	public void setWidth(int width) {
		this.width = width;
		updateLayout();
	}

	/** {@inheritDoc}
	 */
	@Override
	public void doComponentUpdate() {
		width = 0;
		updateLayout();
		getComponent().setBackground( getBackgroundColor() );
	}

	
	@Override
	protected void updateLayout() {
		String s = getTitle();
		if (s != null) {
			TitledBorder border = (TitledBorder)((JPanel)getComponent()).getBorder();
			border.setTitle(s);
		}

		TableLayout layout = new TableLayout(this, getView().getColumnCount());
		layout.setWidth(width);
		if (getView().isShowBorder()) {
			layout.setPaddingTop(TITLE_HEIGHT);
			layout.setPaddingLeft(PADDING_BORDER);
			layout.setPaddingRight(PADDING_BORDER);
			layout.setPaddingBottom(PADDING_BORDER);
		}
		else {
			layout.setPaddingBottom(0);
			layout.setPaddingLeft(0);
			layout.setPaddingRight(0);
			layout.setPaddingTop(0);
		}
		int h = layout.updateLayout();
		int w = layout.getWidth();
		if (w < width)
			w = width;
		
		if (h < MIN_HEIGHT)
			h = MIN_HEIGHT;

		getComponent().setSize(new Dimension(w, h));
		getComponent().setMaximumSize(new Dimension(10000, h));
		getComponent().setMinimumSize(new Dimension(w, h));
	}

	
}
