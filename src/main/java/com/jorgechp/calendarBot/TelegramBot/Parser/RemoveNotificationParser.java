/**
 * 
 */
package com.jorgechp.calendarBot.TelegramBot.Parser;

import com.beust.jcommander.Parameter;


/**
 * @author jorgechp
 *
 */
public class RemoveNotificationParser extends AbstractParser {
	
	@Parameter(names = "-r", description = "Reminder", required = true)
	private long reminderId;
	
	@Parameter(names = "-n", description = "Notification", required = true)
	private long notificationId;

	/**
	 * @return the reminderId
	 */
	public long getReminderId() {
		return reminderId;
	}

	/**
	 * @return the notificationId
	 */
	public long getNotificationId() {
		return notificationId;
	}

	
	

	
}
