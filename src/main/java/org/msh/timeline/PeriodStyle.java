/**
 * 
 */
package org.msh.timeline;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;

/**
 * Information about the style to be applied in a bar when drawing it
 * 
 * @author Ricardo Memoria
 *
 */
public class PeriodStyle {

	private Color borderColor;
	private Color backgroundColor;
	private Color textColor;
	private Font font;
	private int arcSize;

	public PeriodStyle(Color borderColor, Color backgroundColor, Color textColor,
			Font font, int arcSize) {
		super();
		this.borderColor = borderColor;
		this.backgroundColor = backgroundColor;
		this.textColor = textColor;
		this.font = font;
		this.arcSize = arcSize;
	}
	
	
	/**
	 * Draw a bar following the styles defined
	 * @param g instance of {@link Graphics2D} to draw the bar into
	 * @param r instance of {@link Rectangle} containing the boundaries of the bar
	 */
	public void drawBar(Graphics2D g, Rectangle r, String text) {
		g.setColor(backgroundColor);
		if (arcSize > 0)
			 g.fillRoundRect(r.x, r.y, r.width, r.height, arcSize, arcSize);
		else g.fillRect(r.x, r.y, r.width, r.height);
		
		g.setColor(borderColor);
		g.setStroke(new BasicStroke(2.0f));
		if (arcSize > 0)
			 g.drawRoundRect(r.x, r.y, r.width, r.height, arcSize, arcSize);
		else g.drawRect(r.x, r.y, r.width, r.height);

		// any given text ?
		if (text != null) {
			Shape clipArea = g.getClip();
			if (font != null)
				g.setFont(font);
			if (textColor != null)
				g.setColor(textColor);
			g.setClip((int)r.getX(), (int)r.getY(), (int)r.getWidth(), (int)r.getHeight());
			g.drawString(text, (int)r.getX() + 4, (int)r.getY() + 13);
			g.setClip(clipArea);
		}
	}
	
	/**
	 * @return the borderColor
	 */
	public Color getBorderColor() {
		return borderColor;
	}
	/**
	 * @param borderColor the borderColor to set
	 */
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}
	/**
	 * @return the backgroundColor
	 */
	public Color getBackgroundColor() {
		return backgroundColor;
	}
	/**
	 * @param backgroundColor the backgroundColor to set
	 */
	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	/**
	 * @return the textColor
	 */
	public Color getTextColor() {
		return textColor;
	}
	/**
	 * @param textColor the textColor to set
	 */
	public void setTextColor(Color textColor) {
		this.textColor = textColor;
	}
	/**
	 * @return the font
	 */
	public Font getFont() {
		return font;
	}
	/**
	 * @param font the font to set
	 */
	public void setFont(Font font) {
		this.font = font;
	}
	/**
	 * @return the arcSize
	 */
	public int getArcSize() {
		return arcSize;
	}
	/**
	 * @param arcSize the arcSize to set
	 */
	public void setArcSize(int arcSize) {
		this.arcSize = arcSize;
	}
}
