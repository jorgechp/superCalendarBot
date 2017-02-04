package com.jorgechp.calendarBot.TelegramBot.Parser;

import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.Parameter;

public abstract class AbstractParser {
	 	@Parameter
	  	protected List<String> parameters = new ArrayList<String>();
	 
		/**
		 * @return the parameters
		 */
		public List<String> getParameters() {
			return parameters;
		}
}
