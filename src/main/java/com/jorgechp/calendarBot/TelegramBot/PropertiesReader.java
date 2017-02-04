/**
 * 
 */
package com.jorgechp.calendarBot.TelegramBot;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @author jorge
 *
 */
public class PropertiesReader {
	private static final String BUNDLE_NAME = "com.jorgechp.calendarBot.TelegramBot.BotConfig"; //$NON-NLS-1$

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private PropertiesReader() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
