/**
 * 
 */
package com.jorgechp.calendarBot.common.interfaces;

import java.util.List;
import java.util.Map;

import com.jorgechp.calendarBot.ReminderSystem.entities.Notification;
import com.jorgechp.calendarBot.ReminderSystem.entities.Reminder;

/**
 * @author jorgechp
 * @brief Represents an implemented Reminder
 * @version 1.0
 * @date 26 of January of 2017
 *
 */
public interface IRemindable {
	
	/**
	 * Return the id of the reminder
	 * @return
	 */
	public long getReminderId();
	
	/**
	 * 
	 * @return the name of this reminder
	 */
	public String getName();
	
	
	/**
	 * @param name the new name of this reminder
	 */
	public void setName(String name);
	
	/**
	 * @return the description of this reminder
	 */
	public String getDescription();
	
	/**
	 * @param description the new description of this reminder
	 */
	public void setDescription(String description);
	
	/**
	 * Get a Map of {@link Notification} objects associated to
	 * this Reminder
	 * @return
	 */
	public  Map<Long,Notification> getNotifications();
	
	/**
	 * Get a list of {@link IReminderResultSet} instances which periodicities of a 
	 * {@link Reminder}. 
	 * 
	 * @return List<IReminderResultSet> the list of {@link IReminderResultSet}
	 */
	public List<IReminderResultSet> getPeriodicities();
}
