/**
 * 
 */
package com.jorgechp.calendarBot.ReminderSystem.management;

import java.util.LinkedList;
import java.util.List;

import com.jorgechp.calendarBot.ReminderSystem.entities.Notification;
import com.jorgechp.calendarBot.ReminderSystem.entities.Reminder;
import com.jorgechp.calendarBot.ReminderSystem.entities.interfaces.INotificationListener;

/**
 * @author jorgechp 
 * @version 1.0
 * @brief Dispatches a {@link Notification} object 
 * @date 21 of January of 2017 
 */
public class NotificationManager {
	
	private List<INotificationListener> listeners;
	
	
	/**
	 * Default constructor
	 */
	public NotificationManager() {
		super();
		listeners = new LinkedList<INotificationListener>();
	}

	/**
	 * Dispatches the {@link Notification} object
	 * @param reminderToExecute
	 */
	public void executeReminder(Notification notificationToExecute){
		Reminder reminderToExecute = notificationToExecute.getReminder();
		
		String title = reminderToExecute.getName();
		String description = reminderToExecute.getDescription();
		long userId = reminderToExecute.getUserUniqueId();
		long reminderId = reminderToExecute.getReminderId();
		
		for(INotificationListener listenerToCall : listeners){
			listenerToCall.dispatchListener(title,description,userId,reminderId);
		}
	}
	
	/**
	 * Register a new {@link INotificationListener} object
	 * @param listenerToRegister
	 */
	public void registerListener(INotificationListener listenerToRegister){
		listeners.add(listenerToRegister);
	}
	 
	/**
	 * Removes a {@link INotificationListener} object
	 * @param listenerToRemove
	 */
	public void removeListener(INotificationListener listenerToRemove){
		listeners.remove(listenerToRemove);
	}

}
