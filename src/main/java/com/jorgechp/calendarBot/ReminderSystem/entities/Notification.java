/**
 * 
 */
package com.jorgechp.calendarBot.ReminderSystem.entities;

import java.util.concurrent.atomic.AtomicInteger;

import org.quartz.Scheduler;


/**
 *  @author jorgechp
 *  @version 1.0
 *	@brief A Notification is an instantiation of a {@link Reminder} 
 *		object with just one {@link Frequency}
 *	@date 21 of January of 2017
 *	
 */

public class Notification {
	
	/**
	 * Generates unique id for each Notification. We need notifications to be
	 * uniquely identified in order to able to insert (or remove) them into
	 * a {@link Scheduler} 
	 */
	private static final AtomicInteger idGenerator = new AtomicInteger();
	
	private long notificationId;
	private Reminder reminder;
	private Frequency frequencyOfReminder;
	
	
	/**
	 * @param reminder
	 * @param frequencyOfReminder
	 */
	public Notification(Reminder reminder, Frequency frequencyOfReminder) {
		super();
		this.reminder = reminder;
		this.frequencyOfReminder = frequencyOfReminder;
		this.notificationId = idGenerator.getAndIncrement();
	}
	
	/**
	 * @return the reminder
	 */
	public Reminder getReminder() {
		return reminder;
	}
	/**
	 * @return the frequencyOfReminder
	 */
	public Frequency getFrequencyOfReminder() {
		return frequencyOfReminder;
	}

	/**
	 * @return the notificationId
	 */
	public long getNotificationId() {
		return notificationId;
	}
	
	
	

}
