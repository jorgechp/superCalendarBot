package com.jorgechp.calendarBot.ReminderSystem.entities.components;

/**
 * @author jorgechp 
 * @version 1.0
 * @brief Define different periodicities managed by reminders
 * @date 15 of January of 2017
 *  
 */
public class Periodicity {
	
	private long customPeriodicity;
		
	/**
	 * A minute is the minimum time possible
	 */
	public static final int MINUTE = 1;
	public static final int QUARTER_HOUR = 15;
	public static final int HALF_HOUR = 30;
	public static final int HOUR = 60;
	public static final int QUARTER_DAY = 6*60;
	public static final int HALF_DAY = 12*60;
	public static final int DAY = 1440;
	public static final int WEEK = 7*1440;
	public static final int MONTH = 43200; //1 month = 43200 minutes
	public static final int TRIMESTRER = 3*43200;
	public static final int SEMESTRER = 6*43200;
	public static final int YEAR = 259200; //1 year = 259200 minutes
	public static final int BI_YEAR = 2*259200;
	public static final int LUSTRUM = 5*259200;
	public static final int DECADE = 10*259200;
	public static final int TWO_DECADE = 20*259200;
	public static final int FIVE_DECADE = 50*259200;
	
	/**
	 * OMG The next values are only for elfs
	 */
	public static final int CENTURY = 25920000;  //1 century = 25920000 minutes
	public static final int MILLENIUM = 1000*25920000;
		
	public Periodicity() {
		  this.customPeriodicity = -1;
	}
	
	public Periodicity(long value) {
	  this.customPeriodicity = value;
	}

	public long getValue() {
		   return this.customPeriodicity;
	}
	
	
}
