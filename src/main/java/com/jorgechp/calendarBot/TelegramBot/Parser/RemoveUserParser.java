package com.jorgechp.calendarBot.TelegramBot.Parser;

import java.util.LinkedList;

import com.jorgechp.calendarBot.TelegramBot.components.OrderType;

public class RemoveUserParser extends AbstractParser {

	
	/*
	 * Default constructor
	 */
	public RemoveUserParser() {
		super(OrderType.USER_UNSUSCRIBE);
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
