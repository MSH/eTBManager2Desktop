package org.msh.etbm.desktop.app;

import java.beans.Beans;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.msh.eventbus.EventBusService;

/**
 * Class automatically created by Window Builder tool, and maintained
 * to support language selection and localized messages.
 * 
 * @author Ricardo Memoria
 *
 */
public class Messages {
	
	private static final Messages instance = createInstance();
	private List<Locale> locales;
	private static Messages createInstance() {
		return new Messages();
	}
	
	////////////////////////////////////////////////////////////////////////////
	//
	// Constructor
	//
	////////////////////////////////////////////////////////////////////////////
	private Messages() {
		// do not instantiate
	}
	////////////////////////////////////////////////////////////////////////////
	//
	// Bundle access
	//
	////////////////////////////////////////////////////////////////////////////
	private static final String BUNDLE_NAME = "messages"; //$NON-NLS-1$
	private ResourceBundle RESOURCE_BUNDLE = loadBundle();
	private static ResourceBundle loadBundle() {
		return ResourceBundle.getBundle(BUNDLE_NAME);
	}
	////////////////////////////////////////////////////////////////////////////
	//
	// Strings access
	//
	////////////////////////////////////////////////////////////////////////////
	public static String getString(String key) {
		try {
			ResourceBundle bundle = Beans.isDesignTime() ? loadBundle() : instance.RESOURCE_BUNDLE;
			return bundle.getString(key);
		} catch (MissingResourceException e) {
			return "!" + key + "!";
		}
	}
	
	/**
	 * Return the standard required field message in the selected language
	 * @return
	 */
	public static String requiredField() {
		return Messages.getString("javax.faces.component.UIInput.REQUIRED");
	}
	
	
	/**
	 * Return 'No record found' in the selected language
	 * @return String value
	 */
	public static String noRecordFound() {
		return Messages.getString("form.norecordfound");
	}
	
	
	/**
	 * Refresh the resource bundle based on the new locale 
	 */
	public static void changeLocale(Locale locale) {
		Locale.setDefault(locale);
		ResourceBundle.clearCache();
		instance.RESOURCE_BUNDLE = loadBundle();
		EventBusService.raiseEvent(AppEvent.LANGUAGE_CHANGED);
	}
	
	
	/**
	 * Return the list of available languages supported by the system
	 * @return
	 */
	public static List<Locale> getAvailableLocales() {
		if (instance.locales == null) {
			List<Locale> lst = new ArrayList<Locale>();
			lst.add(new Locale("en"));
			lst.add(new Locale("in"));
			lst.add(new Locale("pt", "BR"));
			lst.add(new Locale("ru"));
			lst.add(new Locale("uz"));
			lst.add(new Locale("vi"));
			lst.add(new Locale("en", "BD"));
			lst.add(new Locale("en", "KH"));
			lst.add(new Locale("en", "NA"));
			lst.add(new Locale("en", "NG"));
			instance.locales = lst;
		}
		
		return instance.locales;
	}
	
	
	/**
	 * Check if the message starts with a @ character. If so, the message will be replaced
	 * by a system message, otherwise, the same string given as argument will be returned
	 * @param s is the String containing the text to be translated
	 * @return message translated
	 */
	public static String translateMessage(String s) {
		if (s.startsWith("@")) {
			return getString(s.substring(1));
		}
		
		return s;
	}
}
