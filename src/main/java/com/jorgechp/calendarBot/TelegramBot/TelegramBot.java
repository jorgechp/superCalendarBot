/**
 * 
 */
package com.jorgechp.calendarBot.TelegramBot;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.jorgechp.calendarBot.ReminderSystem.entities.interfaces.INotificationListener;
import com.jorgechp.calendarBot.TelegramBot.Entities.BotOrder;
import com.jorgechp.calendarBot.TelegramBot.components.OrderType;
import com.jorgechp.calendarBot.TelegramBot.interfaces.IOrderSent;
import com.jorgechp.calendarBot.common.ServerResponse;
import com.jorgechp.calendarBot.common.interfaces.IRemindable;
import com.jorgechp.calendarBot.common.interfaces.IReminderSystem;
import com.jorgechp.calendarBot.common.ServerResponses;

/**
 * @author jorge
 *
 */
public class TelegramBot implements INotificationListener, IOrderSent {

	private TelegramBotModel botModel;
	private IReminderSystem reminderSystem;
	
	/**
	 * Default constructor
	 */
	public TelegramBot() {
		super();
		botModel = new TelegramBotModel(this);		
	}
	
	

	/**
	 * @return the reminderSystem
	 */
	public IReminderSystem getReminderSystem() {
		return reminderSystem;
	}



	/**
	 * @param reminderSystem the reminderSystem to set
	 */
	public void setReminderSystem(IReminderSystem reminderSystem) {
		this.reminderSystem = reminderSystem;
	}



	public void startBot(){		
		ApiContextInitializer.init(); 
		TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            botsApi.registerBot(botModel.getBotController());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
	}

	public void dispatchListener(String title, String description, long userId,
			long reminderId) {
		try {
			botModel.sendMessageToTelegram(userId, title);
		} catch (TelegramApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void processMesage(Message messageReceived)  {
		ServerResponses response = ServerResponses.REQUEST_OK;
		String messageText = messageReceived.getText();
		BotOrder orderProcessed = botModel.processOrder(messageText);	
		OrderType newOrder = orderProcessed.getOrder();
		List<String> arguments = orderProcessed.getOrderArguments();
		
		switch (newOrder) {
			case ADD_REMINDER:				  
			    java.util.Date d = new Date(Long.parseLong(arguments.get(0)));	
			    
			    ServerResponse<Long> responseFromReminderSystem;
			    responseFromReminderSystem = reminderSystem.addReminder(arguments.get(1),
						arguments.get(2),
						messageReceived.getChatId());		
			    
			    if(responseFromReminderSystem.getResponseType() == ServerResponses.ADD_REMINDER_OK){
			    	long idReminder = responseFromReminderSystem.getResponseObject();
					reminderSystem.addNotification(
							idReminder,
							Instant.ofEpochMilli(d.getTime()),
							0,
							0,
							false);	
					response = ServerResponses.ADD_REMINDER_OK;
			    }else{
			    	response = ServerResponses.ADD_REMINDER_ERROR;
			    	}	
				break;
			case LIST:
				List<IRemindable> reminderList = reminderSystem.getAllRemindersByUser(messageReceived.getChatId());
				botModel.sendRemindersList(messageReceived.getChatId(),reminderList);
				break;
			case REMOVE_NOTIFICATION:
				reminderSystem.removeNotification(
						Long.parseLong(arguments.get(0)),
						Long.parseLong(arguments.get(1)),
						messageReceived.getChatId()
						);
				break;
			case REMOVE_REMINDER:
				reminderSystem.removeReminder(
						Long.parseLong(arguments.get(0)),
						messageReceived.getChatId()
						);				
				break;

		default:
			break;
		}
		
		botModel.processServerRequest(messageReceived.getChatId(),response);
		
	}
}
