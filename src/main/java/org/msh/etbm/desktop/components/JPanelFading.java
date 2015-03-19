package org.msh.etbm.desktop.components;

import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import org.pushingpixels.trident.Timeline;
import org.pushingpixels.trident.TimelinePropertyBuilder.PropertySetter;
import org.pushingpixels.trident.callback.TimelineCallback;

/**
 * This is the standard {@link JPanel} an extra method to fade in the panel, i.e,
 * implements a transition from invisible to visible using a smooth fade effect
 * 
 * @author Ricardo Memoria
 *
 */
public class JPanelFading extends JPanel {
	private static final long serialVersionUID = 7122667856844497359L;
	private Composite comp;

	
	/**
	 * Display the panel using a fade effect
	 * @param millis is the time it will will take to fade in the panel, in milliseconds 
	 */
	public void fadeIn(int millis) {
		fadePanel(millis, 0.0f, 1.0f, null);
	}

	/**
	 * Display the panel using a fade in effect. A callback interface is used as parameter
	 * to be called when animation finishes 
	 * @param millis is the time it will will take to fade in the panel, in milliseconds
	 * @param callback the callback function that will be called when animation finishes
	 */
	public void fadeIn(int millis, TimelineCallback callback) {
		fadePanel(millis, 0.0f, 1.0f, callback);
	}

	/**
	 * Hide the pane using a fade effect
	 * @param millis is the time it'll take to fade out the panel, in milliseconds
	 */
	public void fadeOut(int millis) {
		fadePanel(millis, 1.0f, 0.0f, null);
	}

	/**
	 * Hide the pane using a fade effect
	 * @param millis is the time it'll take to fade out the panel, in milliseconds
	 * @param callback the callback function that will be called when animation finishes
	 */
	public void fadeOut(int millis, TimelineCallback callback) {
		fadePanel(millis, 1.0f, 0.0f, callback);
	}
	
	/**
	 * Execute a fade transition from an initial to an ending value. The initial and
	 * final value correspond to the opacity of the panel. A value of 0 is full opacity (panel is
	 * hidden) and a value of 1.0 is no opacity (panel is totally visible)
	 * @param millis is the time it'll take to execute the transition, in milliseconds
	 * @param ini is the initial value of the opacity, from 0 to 1
	 * @param end is the final value of the opacity, from 0 to 1
	 */
	protected void fadePanel(int millis, float ini, float end, TimelineCallback callback) {
		setOpaque(false);
		
		Timeline timeline = new Timeline(this);
		PropertySetter<Float> propertySetter = new PropertySetter<Float>() {
			@Override
			public void set(Object obj, String fieldName, Float value) {
				comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, value); 
				getParent().repaint();
			}
			
		};
		timeline.addPropertyToInterpolate(Timeline.<Float> property("value")
				.from(ini).to(end).setWith(propertySetter));
		timeline.setDuration(millis);
		comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.0f);
		if (callback != null)
			timeline.addCallback(callback);
		timeline.play();
		setVisible(true);
	}
	
	/** {@inheritDoc}
	 * @see javax.swing.JComponent#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
	      Graphics2D g2 = (Graphics2D) g.create();  
	      if (comp != null)
	    	  g2.setComposite(comp);
	      g2.setColor(getBackground());
	      g2.fillRect(0, 0, getWidth(), getHeight());
	      super.paint(g2);
	      g2.dispose();
	}
}
