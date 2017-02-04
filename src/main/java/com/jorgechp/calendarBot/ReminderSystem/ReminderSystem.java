/**
 * 
 */
package com.jorgechp.calendarBot.ReminderSystem;

import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.DirectSchedulerFactory;

import com.jorgechp.calendarBot.ReminderSystem.entities.Frequency;
import com.jorgechp.calendarBot.ReminderSystem.entities.Notification;
import com.jorgechp.calendarBot.ReminderSystem.entities.Reminder;
import com.jorgechp.calendarBot.ReminderSystem.entities.User;
import com.jorgechp.calendarBot.ReminderSystem.entities.interfaces.INotificationListener;
import com.jorgechp.calendarBot.ReminderSystem.management.CalendarManager;
import com.jorgechp.calendarBot.common.ServerResponse;
import com.jorgechp.calendarBot.common.ServerResponsesTypes;
import com.jorgechp.calendarBot.common.interfaces.IRemindable;
import com.jorgechp.calendarBot.common.interfaces.IReminderSystem;

/**
 * @author jorgechp 
 * @version 1.0
 * @brief Implements all method needed to manage the Reminder System.  
 * @see  {@link IReminderSystem} for more information 
 * @date 21 of January of 2017
 *  
 */
public class ReminderSystem implements IReminderSystem, INotificationListener{
	
	private CalendarManager systemCalendarManager;
	private Map<Long, User> usersMap;
	private Map<Long, Reminder> remindersMap;
		
	

	/**
	 * Constructor of {@link ReminderSystem} object
	 */
	public ReminderSystem() {
		super();		
		systemCalendarManager = new CalendarManager(getScheduler());
		systemCalendarManager.registerNotificationListener(this);
		usersMap = new HashMap<Long, User>();
		remindersMap = new HashMap<Long, Reminder>();
	}
	
	/**
	 * Add a new {@link INotificationListener}
	 * @param listenerToAdd
	 */
	public void registerNotificationListener(INotificationListener listenerToAdd){
		systemCalendarManager.registerNotificationListener(listenerToAdd);
	}
	
	/**
	 * Ramoves a {@link INotificationListener}
	 * @param listenerToRemove
	 */
	public void removeNotificationListener(INotificationListener listenerToRemove){
		systemCalendarManager.removeNotificationListener(listenerToRemove);
	}
	
	/**
	 * Starts the {@link CalendarManager}
	 */
	public void startReminderSystem(){
		try {
			systemCalendarManager.startScheduler();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Stops the {@link CalendarManager} 
	 * @param isWaitUntilAllNotificationsFinish if true, {@link CalendarManager} will stop only
	 *  when finish to notify all remaining {@link Notification}
	 */
	public void stopReminderSystem(boolean isWaitUntilAllNotificationsFinish){
		try {
			systemCalendarManager.stopScheduler(isWaitUntilAllNotificationsFinish);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets a default {@link Scheduler} 
	 * @return The default {@link Scheduler}
	 */
	private Scheduler getScheduler(){
		Scheduler scheduler = null;
		try {
			DirectSchedulerFactory.getInstance().createVolatileScheduler(10);
			scheduler = DirectSchedulerFactory.getInstance().getScheduler();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return scheduler;
	}
	
	

	/*
	 * METHODS FROM {@link IReminderSystem} Interface
	 */
	
	public void addUser(long uniqueId) {
		User newUser = new User(uniqueId);
		usersMap.put(uniqueId, newUser);		
	}

	/**
	 * Removes an {@link User} from the systems. This implies removing also all
	 * {@link Reminder}, {@link Frequency} and {@link Notification} objects
	 * associated with that user.
	 */
	public void removeUser(long uniqueId) {
		usersMap.remove(uniqueId);		
	}

	public boolean isUserExists(long uniqueId) {
		return usersMap.containsKey(uniqueId);		
	}

	public ServerResponse<Long> addReminder(String name, String description, long userUniqueId) {
		Reminder newReminder = new Reminder(name, description,userUniqueId);
		if(usersMap.get(userUniqueId).addNewReminder(newReminder)){
			try{
				long uniqueId = newReminder.getReminderId();
				remindersMap.put(uniqueId,newReminder);
				return new ServerResponse<Long>(ServerResponsesTypes.ADD_REMINDER_OK,uniqueId);
			}catch(NullPointerException e){
				//If something wrong has happen, we need to remove the Reminder from usersMap
				remindersMap.remove(newReminder);
			}						
		}
		return new ServerResponse<>(ServerResponsesTypes.ADD_REMINDER_ERROR);
	}
	
	/* (non-Javadoc)
	 * @see com.jorgechp.calendarBot.CommonInterfaces.IReminderSystem#removeReminder(long, long)
	 */
	@Override
	public void removeReminder(long reminderUniqueId, long userId){
		if(isSameAsReminderUser(reminderUniqueId, userId)){
			removeReminder(reminderUniqueId);
		}
	} 


	private void removeReminder(long reminderUniqueId) 	{	
		List<Notification> notificationsToRemove = getAllNotificationsByReminder(reminderUniqueId);
		Reminder reminderToRemove = remindersMap.get(reminderUniqueId);
		User userToExtract = usersMap.get(reminderToRemove.getUserUniqueId());
		try {
			for(Notification notificationToRemove : notificationsToRemove){			
					systemCalendarManager.removeNotificationJob(notificationToRemove);					
			}			
					
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		userToExtract.removeReminder(reminderToRemove);	
		remindersMap.remove(reminderUniqueId);		
	}

	public List<IRemindable> getAllRemindersByUser(long userUniqueId) {
		User userToGetRemindersFrom = usersMap.get(userUniqueId);
		List<IRemindable> listToReturn = new LinkedList<IRemindable>();
		
		for(Reminder reminderToAdd : userToGetRemindersFrom.getUserReminders()){
			listToReturn.add(reminderToAdd);
		}
		return listToReturn;
	}

	public ServerResponse<Boolean> addNotification(long reminderId, Instant timeStart,
			int periodicity, int numPeriods, boolean isInfinite) {
	
		Reminder reminderToGet = remindersMap.get(reminderId);
		Frequency newFrequency = new Frequency(timeStart, periodicity, numPeriods, isInfinite);
		Notification newNotification = new Notification(reminderToGet, newFrequency);
		reminderToGet.addNotification(newNotification);
		
		try {
			if(systemCalendarManager.addNotificationJob(newNotification) != null){
				return new ServerResponse<Boolean>();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ServerResponse<Boolean>(ServerResponsesTypes.ADD_NOTIFICATION_ERROR);
	}
	
	/**
	 * Compares if the {@link User} instance of a {@link Reminder} is the same than
	 * param userId
	 * @param reminderId
	 * @param userId
	 * @return true if userId == id of the User associated with the Reminder
	 */
	private boolean isSameAsReminderUser(long reminderId, long userId ){
		Reminder reminderToGet = remindersMap.get(reminderId);
		return reminderToGet.getUserUniqueId() == userId;
	}
	
	/* (non-Javadoc)
	 * @see com.jorgechp.calendarBot.CommonInterfaces.IReminderSystem#removeNotification(long, long, long)
	 */
	@Override
	public ServerResponse<Boolean> removeNotification(long reminderId, long notificationId,
			long userId) {		
		try {
			if(isSameAsReminderUser(reminderId, userId)){			
				removeNotification(reminderId, notificationId);
				return new ServerResponse<Boolean>(ServerResponsesTypes.REQUEST_OK,true);			
			}
		} catch (SchedulerException e) {
			e.printStackTrace();								
		} catch (NullPointerException e){
			e.printStackTrace();	
		}
		return new ServerResponse<Boolean>(ServerResponsesTypes.REMOVE_NOTIFICATION_ERROR,false);		
	}
	
	private void removeNotification(long reminderId, long notificationId) throws SchedulerException {
		Reminder reminderToExtract = remindersMap.get(reminderId);		
		systemCalendarManager.removeNotificationJob(reminderToExtract.getNotifications().get(notificationId));		
		reminderToExtract.removeNotification(notificationId);
	}

	public List<Notification> getAllNotificationsByReminder(long reminderId) {
		return new LinkedList<Notification>(remindersMap.get(reminderId)//
				.getNotifications().values());
	}

	//TODO REMOVE OR IMPROVE THIS METHOD
	public List<Instant> getAllJobsInScheduler() {
		try {
			return systemCalendarManager.getAllJobsInScheduler();
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/*
	 * IMPLEMENTED INotficationListener Interface
	 * 
	 */

	public void dispatchListener(String title, String description, long userId,
			long reminderId) {
		try{
			Reminder reminderToRemove = remindersMap.get(reminderId);
			User user = usersMap.get(userId);
			List<Reminder> reminders = user.getUserReminders();
			reminders.remove(reminderToRemove);
			remindersMap.remove(reminderToRemove);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		System.out.println("Fuego!");
	}




	
	

}
