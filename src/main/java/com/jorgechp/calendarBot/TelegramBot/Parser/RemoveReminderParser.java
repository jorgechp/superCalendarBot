/**
 * 
 */
package com.jorgechp.calendarBot.TelegramBot.Parser;


import java.util.LinkedList;

import com.beust.jcommander.Parameter;
import com.jorgechp.calendarBot.TelegramBot.components.OrderType;

/**
 * @author jorge
 *
 */
public class RemoveReminderParser extends AbstractParser {
	
	@Parameter(names = "-r", description = "Reminder", required = true)
	private int reminderId;

	/*
	 * Default constructor
	 */
	public RemoveReminderParser() {
		super(OrderType.REMOVE_REMINDER);
	}
	
	/**
	 * @return the reminderId
	 */
	public int getReminderId() {
		return reminderId;
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
