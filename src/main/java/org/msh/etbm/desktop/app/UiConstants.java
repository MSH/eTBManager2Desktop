/**
 * 
 */
package org.msh.etbm.desktop.app;

import java.awt.Color;
import java.awt.Font;

import javax.swing.plaf.FontUIResource;

/**
 * Set of UI constants used throughout the UI
 * 
 * @author Ricardo Memoria
 *
 */
public class UiConstants {

	// default font used in the system
	public static final FontUIResource defaultFont = new FontUIResource("Verdana", 0, 13);
	// warning messages
	public static final FontUIResource warningFont = new FontUIResource( defaultFont.deriveFont(Font.BOLD) );
	public static final Color warningColor = new Color(255, 140, 0);

	public static final FontUIResource h1Font = new FontUIResource("Arial", Font.BOLD, 28);
	public static final FontUIResource h2Font = new FontUIResource("Arial", Font.BOLD, 20);
	public static final FontUIResource h3Font = new FontUIResource("Arial", Font.BOLD, 16);

	public static final FontUIResource fieldLabel = new FontUIResource(defaultFont.deriveFont(Font.BOLD));
	public static final Font fieldValue = defaultFont;

	public static final FontUIResource buttonSmallFont = new FontUIResource(defaultFont.deriveFont(10.0f));
	public static final FontUIResource buttonStandardFont = defaultFont;
	public static final FontUIResource buttonBigFont = new FontUIResource(defaultFont.deriveFont(14.0f).deriveFont(Font.BOLD));
	
	public static final Color selectedRow = new Color(51, 102, 153);
	
	public static final Color formBackground = Color.WHITE;
	
	public static final Color panelBackgroundColor = new Color(245, 245, 245);
	public static final Color darkBackgroundColor = new Color(51, 102, 153);
}
