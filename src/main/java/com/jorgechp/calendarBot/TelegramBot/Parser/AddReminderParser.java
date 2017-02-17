package com.jorgechp.calendarBot.TelegramBot.Parser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;



import com.beust.jcommander.Parameter;
import com.jorgechp.calendarBot.TelegramBot.components.OrderType;
import com.jorgechp.calendarBot.TelegramBot.components.creator.AddReminderCreator;

public class AddReminderParser extends AbstractParser{
	

	/**
	 * Default Constructor
	 */
	public AddReminderParser() {
		super(OrderType.ADD_REMINDER);		
	}

	private final String  acceptedFormatDate= "dd/MM/yyyy";
	private final String  acceptedFormatDateHour = "dd/MM/yyyyHH:mm";

  
	  @Parameter(names = "-title", description = "Reminder", variableArity = true, required = true)
	  private List<String> reminderTitle = new LinkedList<String>();
	  
	  @Parameter(names = "-description", description = "Description", variableArity = true)
	  private List<String> description = new LinkedList<String>();
	   
	  @Parameter(names = "-periodicity", description = "Periodicity", variableArity = true)
	  private List<String> periodicities = new LinkedList<String>();
	  
	  @Parameter(names = "-at", variableArity = true)
	  private List<String> dates = new LinkedList<String>();



	  
	/**
	 * @return the reminderTitle
	 */
	public String getReminderTitle() {		
		return String.join(" ", reminderTitle);
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return String.join(" ", description);
	}

	/**
	 * @return the periodicity
	 */
	public List<String> getPeriodicities() {
		return periodicities;
	}

	/**
	 * @return the dates
	 */
	public String getDate() {
		String completeDate = null;
		SimpleDateFormat parser = null;
		java.util.Date d = null;
		
		if(dates.size() == 2){
			completeDate = String.join("", dates);	
			parser = new SimpleDateFormat(acceptedFormatDateHour);
		}else{
			completeDate = dates.get(0);
			parser = new SimpleDateFormat(acceptedFormatDate);
		}		
	    
		try {
			d = parser.parse(completeDate);
		} catch (ParseException e) {			
			e.printStackTrace();
		}
		
		return new Long(d.getTime()).toString();
	}

	/* (non-Javadoc)
	 * @see com.jorgechp.calendarBot.TelegramBot.Parser.AbstractParser#createCommand()
	 */
	@Override
	public LinkedList<String> createCommand() {
		AddReminderCreator commandCreator = new AddReminderCreator();
		return commandCreator.createArguments(this);
	}
	  
	  
	  


	  
	  
}
