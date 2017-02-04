package com.jorgechp.calendarBot.ReminderSystem.management;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

import java.time.Instant;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;

import com.jorgechp.calendarBot.ReminderSystem.entities.Frequency;
import com.jorgechp.calendarBot.ReminderSystem.entities.Notification;
import com.jorgechp.calendarBot.ReminderSystem.entities.Reminder;
import com.jorgechp.calendarBot.ReminderSystem.entities.components.NotificationJob;
import com.jorgechp.calendarBot.ReminderSystem.entities.interfaces.INotificationListener;


/**
 * @author jorgechp 
 * @version 1.0
 * @brief Represents a Calendar Manager, which is responsible of check new {@link Reminder} and process them 
 * @date 16 of January of 2017 *  
 */
public class CalendarManager {
	public static final String REMINDER_OBJECT_IDENTIFICATOR = "reminderObject";
	public static final String REMINDER_MANAGER_IDENTIFICATOR = "reminderManager";	

	
	private Scheduler scheduler;
	private NotificationManager notificationManager;

	/**
	 * @throws SchedulerException 
	 * 
	 */
	public CalendarManager(Scheduler scheduler) {
		super();
		this.scheduler = scheduler;	 
		notificationManager = new NotificationManager();
	}
	
	/**
	 * Create a Trigger for a simple task executed just one time
	 * @param jobName The name of the job to be created
	 * @param groupName	The name of the group to be created
	 * @param frequencyToAdd	Object {@link Frequency} with task information
	 * @return	{@link Trigger} object
	 */
	private Trigger getTriggerForSimpleTask(String jobName, String groupName, Frequency frequencyToAdd){
		return newTrigger()
	    .withIdentity(jobName, groupName)
	    .startAt(Date.from(frequencyToAdd.getTimeStart()))
	    .forJob(jobName, groupName)
	    .build();
		
		
	}
	
	/**
	 * Create a Trigger for a periodic task executed in a specific interval
	 * @param jobName The name of the job to be created
	 * @param groupName	The name of the group to be created
	 * @param frequencyToAdd	Object {@link Frequency} with task information
	 * @return	{@link Trigger} object
	 * @throws Exception 
	 */
	private Trigger getTriggerForComplexTask(String jobName, String groupName,
			Frequency frequencyToAdd) throws Exception {
		
		SimpleScheduleBuilder scheduleBuilder = null;
		if(frequencyToAdd.isInfinite()){
			scheduleBuilder = simpleSchedule()
			          .withIntervalInSeconds(frequencyToAdd.getPeriodicity())
			          .repeatForever();
		}else{
			scheduleBuilder = simpleSchedule()
			          .withIntervalInSeconds(frequencyToAdd.getPeriodicity())	
			          .withRepeatCount(frequencyToAdd.getNumOfExtraPeriods());
		}
		
		return newTrigger()
		      .withIdentity(jobName,groupName)
		      .startAt(Date.from(frequencyToAdd.getTimeStart())) 
		      .withSchedule(scheduleBuilder)            
		      .build();
		
	}
	
	/**
	 * Starts the scheduler
	 * @throws SchedulerException
	 */
	public void startScheduler() throws SchedulerException{
		this.scheduler.start();
	}
	
	/**
	 * Stops the scheduler
	 * @param isWaitUntilAllNotificationsFinish if true, the  system only stops when finish to notify
	 *  all remaining {@link Job}
	 * @throws SchedulerException
	 */
	public void stopScheduler(boolean isWaitUntilAllNotificationsFinish) throws SchedulerException{
		this.scheduler.shutdown(isWaitUntilAllNotificationsFinish);
	}
		
	/**
	 * Add a {@link Notification} object to scheduler
	 * @param notificationToAdd
	 * @throws Exception 
	 */
	public Trigger addNotificationJob(Notification notificationToAdd) throws Exception {	
		Reminder reminderAssociatedToNotification = notificationToAdd.getReminder();
		Frequency frequencyAssociatedToNotification = notificationToAdd.getFrequencyOfReminder();
		
		
		String jobName = Long.toString(notificationToAdd.getNotificationId());
		String jobGroup = Long.toString(reminderAssociatedToNotification.getReminderId());
		
        JobDetail job = newJob(NotificationJob.class)//
        		.withIdentity(jobName, jobGroup)//        		
        		.build();
        Trigger trigger = null;
        if(frequencyAssociatedToNotification.getNumOfExtraPeriods() > 0){
        	trigger = getTriggerForComplexTask(jobName,
        			jobGroup,
        			frequencyAssociatedToNotification);
        }else{
        	trigger = getTriggerForSimpleTask(jobName,
        			jobGroup,
        			frequencyAssociatedToNotification);
        }

        
        scheduler.getContext().put(CalendarManager.REMINDER_OBJECT_IDENTIFICATOR,
        		notificationToAdd);
        scheduler.getContext().put(CalendarManager.REMINDER_MANAGER_IDENTIFICATOR,
        		notificationManager);
        scheduler.scheduleJob(job, trigger);
        
        return trigger;
	}
	
	/**
	 * Removes a Notification
	 * @param reminderToRemove
	 * @throws SchedulerException
	 */
	public void removeNotificationJob(Notification notificationToRemove)
			throws SchedulerException{	
		Reminder reminderAssociatedToNotification = notificationToRemove.getReminder();
		String reminderName = Long.toString(reminderAssociatedToNotification.getReminderId());
		String reminderGroup = Long.toString(reminderAssociatedToNotification.getUserUniqueId());
		
		this.scheduler.deleteJob(new JobKey(reminderName,reminderGroup));	
	}


	public List<Instant> getAllJobsInScheduler() throws SchedulerException {
		List<Instant> listOfInstants = new LinkedList<Instant>();
		   for (String groupName : scheduler.getJobGroupNames()) {

			     for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {
				
				 List<? extends Trigger> triggersOfJob = scheduler.getTriggersOfJob(jobKey);
				
				  Date nextFireTime = triggersOfJob.get(0).getNextFireTime();
				  listOfInstants.add(Instant.ofEpochMilli(nextFireTime.getTime()));	

			    }
			     
			     return listOfInstants;
		   }
		return null;
	}

	/**
	 * Register a new {@link INotificationListener} object into System {@link NotificationManager}
	 * @param listenerToAdd {@link INotificationListener} object to add
	 */
	public void registerNotificationListener(INotificationListener listenerToAdd) {
		notificationManager.registerListener(listenerToAdd);		
	}
	
	/**
	 * Removes a {@link INotificationListener} from the System {@link NotificationManager}
	 * @param listenerToAdd {@link INotificationListener} to remove
	 */
	public void removeNotificationListener(INotificationListener listenerToAdd){
		notificationManager.removeListener(listenerToAdd);
	}


}
	
	

	



	
	
	

	


	
	
	


