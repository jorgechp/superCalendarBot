/**
 * 
 */
package com.jorgechp.calendarBot.ReminderSystem.entities.components;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerContext;
import org.quartz.SchedulerException;

import com.jorgechp.calendarBot.ReminderSystem.entities.Notification;
import com.jorgechp.calendarBot.ReminderSystem.exceptions.IncorrectExecutionContextConditionsExceptions;
import com.jorgechp.calendarBot.ReminderSystem.management.CalendarManager;
import com.jorgechp.calendarBot.ReminderSystem.management.NotificationManager;
import com.jorgechp.calendarBot.common.interfaces.IReminderSystem;

/**
 * @author jorge
 *
 */
public class NotificationJob implements Job {
	

	
    public NotificationJob(){  }

	/**
	 * This method must receive a {@link IReminderSystem} and {@link NotificationManager} 
	 * objects from DataMap of the scheduler. Executes the current Reminder
	 *  
	 * 
	 * @param context get the context of the scheduler
	 */
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
        SchedulerContext schedulerContext = null;
        try {
            schedulerContext = context.getScheduler().getContext();
            
            Notification reminderToExecute =
                    (Notification) schedulerContext//
                    .get(CalendarManager.REMINDER_OBJECT_IDENTIFICATOR);        

                
            NotificationManager remindersManager =
                     (NotificationManager) schedulerContext//
                      .get(CalendarManager.REMINDER_MANAGER_IDENTIFICATOR);  
            
            if(reminderToExecute == null || remindersManager == null){            	
            	throw new IncorrectExecutionContextConditionsExceptions();
            }  
           
            
            remindersManager.executeReminder(reminderToExecute); 
            
        } catch (SchedulerException e1) {
            e1.printStackTrace();        
		} catch (IncorrectExecutionContextConditionsExceptions e) {			
			e.printStackTrace();
		}            
    }

		
	




	
	

}
