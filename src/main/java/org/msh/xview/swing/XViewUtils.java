package org.msh.xview.swing;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;

import org.jdesktop.swingx.JXLabel;
import org.msh.etbm.desktop.app.App;
import org.msh.xview.swing.ui.ComponentUI;
import org.msh.xview.swing.ui.ViewUI;

/**
 * Swing XView utilities
 * 
 * @author Ricardo Memoria
 *
 */
public class XViewUtils {

	/**
	 * Calculate the height of a text given its component and width
	 * @param comp the instance of {@link JComponent} to calculate the height
	 * @param width the fixed width of the component
	 * @return the height of the text in pixels
	 */
	public static int calcTextHeight(JComponent comp, int width) {
		View view = (View)comp.getClientProperty(BasicHTML.propertyKey);
		if (view == null)
			return 0;
		view.setSize(width, 0);
		return (int)Math.ceil(view.getPreferredSpan(View.Y_AXIS));
	}
	
	/**
	 * Calculate the height based on a component, its width and html text
	 * @param comp
	 * @param width
	 * @param html
	 * @return
	 */
	public static int calcTextHeight2(JComponent comp, int width, String html) {
		return calcTextHeight(comp, width);
/*		if (html == null) {
			return 0;
		}
		html = html.replace("\n", "<br>");
		View view = BasicHTML.createHTMLView(comp, html);
		if (view == null)
			return 0;
		view.setSize(width, 0);
		return (int)Math.ceil(view.getPreferredSpan(View.Y_AXIS));
*/	}

	
	/**
	 * Adjust label height based on its width
	 * @param lbl instance of {@link JXLabel}
	 */
	public static int adjustHeight(JXLabel lbl, int width) {
		int h = calcTextHeight2(lbl, width, lbl.getText());
		lbl.setSize(width, h);
		lbl.setPreferredSize(new Dimension(width, h));
		return h;
	}
	
	/**
	 * Calculate the height of a text in a graphic device
	 * @param graphics
	 * @param width
	 * @return
	 */
/*	public static int calcTextHeight(String s, Graphics graphics, int width) {
		if ((s == null) || (s.isEmpty()))
			return 0;
		Graphics2D g = (Graphics2D)graphics;
		FontRenderContext frc = g.getFontRenderContext();
		AttributedString as = new AttributedString(s);
		as.addAttribute(TextAttribute.FONT, g.getFont());
		LineBreakMeasurer measurer = new LineBreakMeasurer(as.getIterator(), frc);
		int yo = 0;
		while (measurer.getPosition() < s.length()) {
			TextLayout layout = measurer.nextLayout(width);
			yo += layout.getAscent();
			yo += layout.getDescent() + layout.getLeading() + 8;
		}
		return yo;
	}
*/
	/**
	 * Calculate the width of a text given its component and width
	 * @param comp the instance of the {@link JXLabel} to calculate the width
	 * @return the width of the text in pixels
	 */
	public static int calcTextWidth(JXLabel comp) {
		View view = (View)comp.getClientProperty(BasicHTML.propertyKey);
		view.setSize(0, comp.getFont().getSize());
		return (int)Math.ceil(view.getPreferredSpan(View.X_AXIS));
	}
	
	
	/**
	 * Check if the message is a key message from the list of messages (it must start with an @ char).
	 * If so, it'll be used as a key in the message file, otherwise, returns the own message
	 * @param s
	 * @return
	 */
	public static String translateMessage(String s) {
		if (s == null)
			return null;
		if (s.startsWith("@"))
			return App.getMessage(s.substring(1));
		
		return s;
	}
	
	
	/**
	 * Browse all children recursively looking for all instances of {@link ComponentUI} view
	 * @param view is the instance of {@link ViewUI} to start looking for 
	 * @return List of {@link ComponentUI}
	 */
	public static List<ComponentUI> createChildComponentList(ViewUI view) {
		List<ComponentUI> lst = new ArrayList<ComponentUI>();
		createComponentList(view, lst);
		return lst;
	}
	

	/**
	 * Browse all children looking up for instances of {@link ComponentUI}
	 * @param view
	 * @param lst
	 */
	private static void createComponentList(ViewUI view, List<ComponentUI> lst) {
		if (!view.isContainer()) {
			return;
		}

		for (Object obj: view.getChildren()) {
			ViewUI aux = (ViewUI)obj;
			if (aux.isVisible()) {
				if (aux instanceof ComponentUI) {
					lst.add((ComponentUI)aux);
				}
				else {
					createComponentList(aux, lst);
				}
			}
		}
	}
}
