/**
 * 
 */
package com.jorgechp.calendarBot.TelegramBot.Parser;

import java.util.LinkedList;

import com.jorgechp.calendarBot.TelegramBot.components.OrderType;

/**
 * @author jorge
 *
 */
public class ListUserRemindersParser extends AbstractParser {

	/**
	 * Default Constructor
	 */
	public ListUserRemindersParser() {
		super(OrderType.LIST);		
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
