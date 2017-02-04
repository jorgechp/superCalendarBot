/**
 * 
 */
package com.jorgechp.calendarBot.ReminderSystem.entities.interfaces;

/**
 * Dispatch a Notification
 * @author jorge 
 */
public interface INotificationListener {
	public void dispatchListener(String title, String description, long userId, long reminderId);

}
