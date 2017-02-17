package com.jorgechp.calendarBot.TelegramBot.Parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;




import com.beust.jcommander.Parameter;
import com.jorgechp.calendarBot.TelegramBot.components.OrderType;
import com.jorgechp.calendarBot.TelegramBot.components.creator.AddReminderCreator;

public class HelpReminderParser extends AbstractParser{
	

	/**
	 * Default Constructor
	 */
	public HelpReminderParser() {
		super(OrderType.HELP);		
	}


	/* (non-Javadoc)
	 * @see com.jorgechp.calendarBot.TelegramBot.Parser.AbstractParser#createCommand()
	 */
	@Override
	public LinkedList<String> createCommand() {
		return null;
	}
	  
	  


	  
	  
}
