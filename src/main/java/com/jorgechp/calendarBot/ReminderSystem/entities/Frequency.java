package com.jorgechp.calendarBot.ReminderSystem.entities;

import java.time.Instant;
import java.time.temporal.ChronoUnit;


/**
 * @author jorgechp 
 * @version 1.0
 * @brief Implements all method needed to manage a Reminder Frequency
 * @date 15 of January of 2017
 *  
 */
public class Frequency {
	private Instant timeStart;
	private int periodicity;
	private int numOfExtraPeriods;
	private boolean isInfinite;
	
	

	/**
	 * @param timeStart
	 * @param periodicity
	 * @param numPeriods
	 * @param isInfinite
	 */
	public Frequency(Instant timeStart, int periodicity, int numOfExtraPeriods,
			boolean isInfinite) {
		super();
		this.timeStart = timeStart;
		this.periodicity = periodicity;
		this.numOfExtraPeriods = numOfExtraPeriods;
		this.isInfinite = isInfinite;
	}

	/** 
	 * @return	The first time of the interval
	 */
	public Instant getTimeStart() {
		return timeStart;
	}
	
	/** 
	 * Set the first time of the interval
	 * @param timeStart first time of the interval	 
	 */
	public void setTimeStart(Instant timeStart) {
		this.timeStart = timeStart.truncatedTo(ChronoUnit.MINUTES);
	}
	
	/** 
	 * @return	The periodicity between intervals
	 */
	public int getPeriodicity() {
		return periodicity;
	}
	
	/** 
	 * @param periodicity periodicity between intervals	 
	 */
	public void setPeriodicity(int periodicity) {
		this.periodicity = periodicity;
	}
	
	/** 
	 * @return	Num of extra periods of the interval
	 */
	public int getNumOfExtraPeriods() {
		return numOfExtraPeriods;
	}
	
	/** 
	 * Set the num of extra repetitions of a frequency
	 * @param numPeriods integer with the number of periods	 
	 */
	public void setNumOfExtraPeriods(int numOfExtraPeriods) {
		this.numOfExtraPeriods = numOfExtraPeriods;
	}

	/**
	 * @return the isInfinite
	 */
	public boolean isInfinite() {
		return isInfinite;
	}

	/**
	 * @param isInfinite the isInfinite to set
	 */
	public void setInfinite(boolean isInfinite) {
		this.isInfinite = isInfinite;
	}
	
	
	
	

}
