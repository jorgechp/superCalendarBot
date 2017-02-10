package com.jorgechp.calendarBot.TelegramBot.Parser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.jorgechp.calendarBot.TelegramBot.components.OrderType;
import com.jorgechp.calendarBot.TelegramBot.interfaces.ICommandCreator;

public abstract class AbstractParser {
	
		protected OrderType orderType;
		
	 	@Parameter
	  	protected List<String> parameters = new ArrayList<String>();
	 
		/**
		 * @return the parameters
		 */
		public List<String> getParameters() {
			return parameters;
		}

		public abstract LinkedList<String> createCommand();
		
		public OrderType getOrderType(){
			return orderType;
		}

		public AbstractParser(OrderType orderType) {
			super();
			this.orderType = orderType;
		}
		
		
}
