package com.jorgechp.calendarBot.common.interfaces;

import java.time.Instant;

import com.jorgechp.calendarBot.ReminderSystem.entities.Notification;
import com.jorgechp.calendarBot.ReminderSystem.entities.Reminder;



/**
 * @author jorgechp 
 * @version 1.0
 * @brief Implements a ResultSet wich contains information about {@link Notification} 
 * and associated {@link Reminder}s * 
 * @date 27 of January of 2017
 *  
 */
public interface IReminderResultSet {
	/**
	 * @return the idReminder
	 */
	public long getIdReminder();
	
	/**
	 * @return the idNotification
	 */
	public long getIdNotification();
	
	/**
	 * @return the startTime
	 */
	public Instant getStartTime();
	
	/**
	 * @return the periodicity
	 */
	public int getPeriodicity();

}
