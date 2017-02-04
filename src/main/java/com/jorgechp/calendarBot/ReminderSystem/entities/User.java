package com.jorgechp.calendarBot.ReminderSystem.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jorgechp 
 * @version 1.0
 * @brief Represents an user
 * @date 16 of January of 2017 *  
 */
public class User {
	private long telegramId;	
	private List<Reminder> userReminders;
	
	/**
	 * Default constructor
	 * 
	 * @param telegramId
	 */
	public User(long telegramId) {
		super();
		this.telegramId = telegramId;		
		this.userReminders = new ArrayList<Reminder>();
	}


	/**
	 * @return the telegramId
	 */
	public long getTelegramId() {
		return telegramId;
	}


	/**
	 * @param telegramId the telegramId to set
	 */
	public void setTelegramId(long telegramId) {
		this.telegramId = telegramId;
	}
	
	/**
	 * Gets all reminders from user
	 * @return a list of {@link Reminder} objects
	 */
	public List<Reminder> getUserReminders(){
		return userReminders;
	}
	
	/**
	 * Adds a new {@link Reminder} to this user
	 * @param reminderToAdd An instance of a {@link Reminder} object
	 * @return true if Reminder has been added correctly
	 */
	public boolean addNewReminder(Reminder reminderToAdd){
		return userReminders.add(reminderToAdd);
	}
	
	/**
	 * Removes a {@link Reminder} from the user
	 * @param Reminder
	 */
	public void removeReminder(Reminder reminderToRemove){
		userReminders.remove(reminderToRemove);
	}
	
}
