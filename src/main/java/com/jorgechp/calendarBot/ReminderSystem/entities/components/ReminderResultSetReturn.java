package com.jorgechp.calendarBot.ReminderSystem.entities.components;

import java.time.Instant;

import com.jorgechp.calendarBot.ReminderSystem.entities.Reminder;
import com.jorgechp.calendarBot.common.interfaces.IReminderResultSet;

/**
 * @author jorgechp 
 * @version 1.0
 * @brief Implements a ResultSet with information about a {@link Notification}
 * instance of a {@link Reminder} object
 * @date 15 of January of 2017
 *  
 */
public class ReminderResultSetReturn implements IReminderResultSet{
	private long idReminder;
	private long idNotification;
	private Instant StartTime;
	private int periodicity;
	/**
	 * @param idReminder
	 * @param idNotification
	 * @param startTime
	 * @param periodicity
	 */
	public ReminderResultSetReturn(long idReminder, long idNotification,
			Instant startTime, int periodicity) {
		super();
		this.idReminder = idReminder;
		this.idNotification = idNotification;
		StartTime = startTime;
		this.periodicity = periodicity;
	}

	public long getIdReminder() {
		return idReminder;
	}

	public long getIdNotification() {
		return idNotification;
	}

	public Instant getStartTime() {
		return StartTime;
	}
	/**
	 * @return the periodicity
	 */
	public int getPeriodicity() {
		return periodicity;
	}
	
	
	

}
