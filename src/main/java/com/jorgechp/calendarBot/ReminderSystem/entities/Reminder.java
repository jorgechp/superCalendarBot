package com.jorgechp.calendarBot.ReminderSystem.entities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.quartz.Scheduler;

import com.jorgechp.calendarBot.ReminderSystem.entities.components.ReminderResultSetReturn;
import com.jorgechp.calendarBot.common.interfaces.IRemindable;
import com.jorgechp.calendarBot.common.interfaces.IReminderResultSet;


/**
 * @author jorgechp 
 * @version 1.0
 * @brief Implements all method needed to manage a Reminder
 * @date 15 of January of 2017
 *  
 */
public class Reminder implements IRemindable{
	
	/**
	 * Generates unique id for each reminder. We need reminders to be
	 * uniquely identified in order to able to insert (or remove) them into
	 * a {@link Scheduler} 
	 */
	private static final AtomicInteger idGenerator = new AtomicInteger();
	
	/**
	 * Stores Reminder unique identificator
	 */
	private long reminderId;
	
	/**
	 * Name of the reminder
	 */
	private String name;
	
	/**
	 * Description of the reminder
	 */
	private String description;	
	
	/**
	 * TODO remove this field
	 * 
	 * List of frequencies associated to the Reminder, see {@link Frequency} for
	 * more information.
	 */
	private Map<Long,Notification> notifications;
	
	/**
	 * Stores the id of the owner of this reminder
	 */
	private long userUniqueId;
	
	/**
	 * Constructor for a Reminder
	 * 
	 * @param name name of the reminder
	 * @param description description of the reminder
	 * @param userUniqueId id of the owner of this reminder
	 */
	public Reminder(String name, String description, long userUniqueId) {
		super();
		this.name = name;
		this.description = description;		
		
		this.notifications = new HashMap<Long,Notification>();
		this.userUniqueId = userUniqueId;
		this.reminderId= idGenerator.getAndIncrement();
	}
	
	/**
	 * Add a new {@link Notification} for this reminder.
	 * @param newNotification
	 */
	public void addNotification(Notification newNotification){
		this.notifications.put(newNotification.getNotificationId(),newNotification);
	}
	
	/**
	 * Removes a {@link Notification} of this reminder
	 * @param oldNotificationId
	 */
	public void removeNotification(long oldNotificationId){
		this.notifications.remove(oldNotificationId);
	}

	/**
	 * Gets the {@link Map} of {@link Notification} objects associated to this reminder
	 * @return the notifications
	 */
	public Map<Long,Notification> getNotifications() {
		return notifications;
	}

	/**
	 * Get the owner id
	 * @return the userUniqueId
	 */
	public long getUserUniqueId() {
		return userUniqueId;
	}

	/**
	 * Return the identificator of this reminder
	 * @return the identificator of this reminder
	 */
	public long getReminderId() {
		return reminderId;
	}	

	/**
	 * Returns the name of the Reminder
	 */
	public String getName() {
		return name;
	}


	/**
	 * Sets the name of the Reminder
	 */
	public void setName(String name) {
		this.name = name;
	}


	/**
	 * Returns the description of the Reminder
	 */
	public String getDescription() {
		return description;
	}


	/**
	 * Set the description of the Reminder
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public List<IReminderResultSet> getPeriodicities(){
		List<IReminderResultSet> listToReturn = new LinkedList<IReminderResultSet>();

		for (Notification notificationToAdd : notifications.values()) {	
		   listToReturn.add(new ReminderResultSetReturn
					(		reminderId,
							notificationToAdd.getNotificationId(),
							notificationToAdd.getFrequencyOfReminder().getTimeStart(),
							notificationToAdd.getFrequencyOfReminder().getPeriodicity()
					)
		   );
		}

		return listToReturn;
	}

	
	
	
	
}
