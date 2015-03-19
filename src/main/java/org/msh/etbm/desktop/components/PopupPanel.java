/**
 * 
 */
package org.msh.etbm.desktop.components;

import java.awt.AWTEvent;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingUtilities;


/**
 * Extension of a JPanel to be displayed as a popup panel. The popup panel
 * is displayed over other components, and it's automatically closed if any
 * event (mouse or window) happens outside the panel.
 * <p/>
 * It's usage is very simple:<p/>
 * <ul>
 * <li>Create an instance of the {@link PopupPanel} (the panel itself or its extension);</li>
 * <li>Decorate your popup panel with your components</li>
 * <li>Use the method {@link PopupPanel#show(JComponent, int, int)}</li>
 * </ul>
 * 
 * @author Ricardo Memoria
 *
 */
public class PopupPanel extends JPanel {
	private static final long serialVersionUID = 6078661002158893219L;

	private Popup popup;
	
	/**
	 * Default listener to AWT events (mouse and window events)
	 */
	private AWTEventListener mouseListener =  new AWTEventListener()	{
	    public void eventDispatched(AWTEvent e)
	    {
	    	handleAWTEvents(e);
	    }
	}; 
	
	/**
	 * Default constructor
	 */
	public PopupPanel() {
		super();
		setSize(200,100);
	}
	
	/**
	 * Called to handle AWT events
	 * @param e instance of {@link AWTEvent}
	 */
	protected void handleAWTEvents(AWTEvent e) {
    	if (e instanceof MouseEvent) {
    		MouseEvent evt = (MouseEvent)e;
    		// mouse was clicked and point is outside of the panel ?
    		Point p = new Point(evt.getX(), evt.getY());
    		SwingUtilities.convertPointToScreen(p, evt.getComponent());
    		if ((evt.getID() == MouseEvent.MOUSE_PRESSED) && (!isPointInsidePanel(p)))
    			hidePanel();
    	}
    	if (e instanceof WindowEvent)
    		hidePanel();
	}


	/**
	 * Called to hide the panel
	 */
	protected void hidePanel() {
		if (popup != null) {
			popup.hide();
			popup = null;
		}
		finishMouseMonitoring();
	}


	/**
	 * Check if the given point in screen coordinates is inside the popup panel.
	 * @param x is the screen x-coordinate of the point
	 * @param y is the screen y-coordinate of the point
	 * @return true if the point is inside the panel
	 */
	protected boolean isPointInsidePanel(Point p) {
		SwingUtilities.convertPointFromScreen(p, this);
		return (p.x >= 0) && (p.x < getWidth()) && (p.y >= 0) && (p.y < getHeight());
	}


	/**
	 * Show the popup panel in x and y position relative to the invoker
	 * @param invoker instance of the {@link JComponent} that is invoking this panel
	 * @param x horizontal coordinate in relation to the invoker component
	 * @param y vertical coordinate in relation to the invoker component 
	 */
	public void show(JComponent invoker, int x, int y) {
		if (popup != null) {
			finishMouseMonitoring();
			popup.hide();
		}

		Point p = new Point(x, y);
		SwingUtilities.convertPointToScreen(p, invoker);
		popup = PopupFactory.getSharedInstance().getPopup(invoker, this, p.x, p.y);
		popup.show();
		startMouseMonitoring();
	}
	
	/**
	 * Start mouse monitoring in application level. What this component is interested in is
	 * the mouse events that makes the panel to close, like mouse click
	 */
	protected void startMouseMonitoring() {
		long eventMask = AWTEvent.MOUSE_EVENT_MASK + AWTEvent.WINDOW_EVENT_MASK;
		Toolkit.getDefaultToolkit().addAWTEventListener(mouseListener, eventMask);
	}

	/**
	 * Finish mouse monitoring in application level 
	 */
	protected void finishMouseMonitoring() {
		Toolkit.getDefaultToolkit().removeAWTEventListener(mouseListener);
	}
}
