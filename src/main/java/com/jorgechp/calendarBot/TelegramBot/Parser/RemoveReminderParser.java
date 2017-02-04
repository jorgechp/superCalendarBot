/**
 * 
 */
package com.jorgechp.calendarBot.TelegramBot.Parser;


import com.beust.jcommander.Parameter;

/**
 * @author jorge
 *
 */
public class RemoveReminderParser extends AbstractParser {
	
	@Parameter(names = "-r", description = "Reminder", required = true)
	private int reminderId;

	/**
	 * @return the reminderId
	 */
	public int getReminderId() {
		return reminderId;
	}
	
	

}
