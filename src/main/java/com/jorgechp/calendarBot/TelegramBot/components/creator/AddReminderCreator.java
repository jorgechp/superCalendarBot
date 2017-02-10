/**
 * 
 */
package com.jorgechp.calendarBot.TelegramBot.components.creator;

import java.util.LinkedList;

import com.jorgechp.calendarBot.TelegramBot.Parser.AddReminderParser;
import com.jorgechp.calendarBot.TelegramBot.interfaces.ICommandCreator;

/**
 * @author jorge
 * @param <T>
 *
 */
public class AddReminderCreator implements ICommandCreator<AddReminderParser> {



	/* (non-Javadoc)
	 * @see com.jorgechp.calendarBot.TelegramBot.interfaces.ICommandCreator#createArguments(com.jorgechp.calendarBot.TelegramBot.Parser.AbstractParser)
	 */
	@Override
	public LinkedList<String> createArguments(AddReminderParser arguments) {		
		LinkedList<String> orderArguments = new LinkedList<String>();
		orderArguments.add(arguments.getDate());
		orderArguments.add(arguments.getReminderTitle());
		orderArguments.add(arguments.getDescription());
		orderArguments.addAll(arguments.getPeriodicities());
		return orderArguments;
	}

}
