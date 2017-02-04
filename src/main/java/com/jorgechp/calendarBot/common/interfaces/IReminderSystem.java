/**
 * 
 */
package com.jorgechp.calendarBot.common.interfaces;

import java.time.Instant;
import java.util.List;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import com.jorgechp.calendarBot.ReminderSystem.entities.Frequency;
import com.jorgechp.calendarBot.ReminderSystem.entities.Notification;
import com.jorgechp.calendarBot.ReminderSystem.entities.Reminder;
import com.jorgechp.calendarBot.ReminderSystem.entities.User;
import com.jorgechp.calendarBot.common.ServerResponse;

/**
 * @author jorge
 *
 */
public interface IReminderSystem {
	
	/**
	 * Adds a new user to the System
	 * 
	 * @param uniqueId The id of the user
	 */
	public void addUser(long uniqueId);
	
	/**
	 * Removes an user from the System. 	 * 
	 * @param uniqueId
	 */
	public void removeUser(long uniqueId);
	
	/**
	 * Checks if user exists in the System
	 * @param uniqueId
	 * @return
	 */
	public boolean isUserExists(long uniqueId);
	
	/**
	 * Adds a new {@link Reminder} to the system
	 * 
	 * @param title
	 * @param description
	 * @param userUniqueId
	 * @return The identificator of the {@link Reminder}
	 */
	public ServerResponse<Long> addReminder(String name, String description, long userUniqueId);
	
	/**
	 * Removes a {@link Reminder} from the System
	 * 
	 * @param reminderId	the identificator of the {@link Reminder}
	 * @param userId		the identificator of the {@link User}
	 * @param reminderGroup
	 */
	public void removeReminder(long reminderUniqueId, long userId);
	
	/**
	 * Get all {@link Reminder} objects from a user
	 * 
	 * @param userUniqueId user id
	 * @return A {@link List} of {@link IRemindable} interface objects
	 */
	public List<IRemindable> getAllRemindersByUser(long userUniqueId);
	
	/**
	 * Add a new {@link Frequency} for an {@link Reminder}
	 * 
	 * @param reminderId The id of the {@link Reminder} object
	 * @param timeStart	Start time of the {@link Reminder}
	 * @param periodicity Periodicity of the {@link Reminder}
	 * @param numPeriods Num of extra {@link Notification}
	 * @param isInfinite If true, the {@link Reminder} object will generate
	 * 	{@link Notification} objects constantly
	 */
	public ServerResponse<Boolean> addNotification(long reminderId, Instant timeStart, 
			int periodicity, int numOfExtraPeriods, boolean isInfinite);
	
	/**
	 * Removes {@link Notification} object associated to a {@link Reminder}
	 * @param reminderId The id of the {@link Reminder} object
	 * @param notificationId Number of {@link Notification} of the {@link Reminder}	 
	 * @param userId id of {@link User} 
	 * @throws SchedulerException 
	 */
	public void removeNotification(long reminderId, long notificationId, long userId);
	
	/**
	 * Get all {@link Notification} objects associated to a {@link Reminder}
	 * 
	 * @param reminderId
	 * @return A {@link List} of {@link Notification} objects
	 */
	public List<Notification> getAllNotificationsByReminder(long reminderId);
	
	/**
	 * Get all jobs in the {@link Scheduler}
	 * @return A list of {@link Instant} with the date fire dates of each {@link Trigger}
	 * registered in the {@link Scheduler}
	 */
	public List<Instant> getAllJobsInScheduler();
	
	
	

}
