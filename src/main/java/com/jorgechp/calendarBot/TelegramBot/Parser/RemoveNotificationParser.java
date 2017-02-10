/**
 * 
 */
package com.jorgechp.calendarBot.TelegramBot.Parser;

import java.util.LinkedList;

import com.beust.jcommander.Parameter;
import com.jorgechp.calendarBot.TelegramBot.components.OrderType;


/**
 * @author jorgechp
 *
 */
public class RemoveNotificationParser extends AbstractParser {
	
	@Parameter(names = "-r", description = "Reminder", required = true)
	private long reminderId;
	
	@Parameter(names = "-n", description = "Notification", required = true)
	private long notificationId;

	
	
	/*
	 * Default constructor
	 */
	public RemoveNotificationParser() {
		super(OrderType.REMOVE_NOTIFICATION);
	}

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

	/* (non-Javadoc)
	 * @see com.jorgechp.calendarBot.TelegramBot.Parser.AbstractParser#createCommand()
	 */
	@Override
	public LinkedList<String> createCommand() {
		// TODO Auto-generated method stub
		return null;
	}


	
	

	
}
