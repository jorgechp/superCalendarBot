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
import com.jorgechp.calendarBot.common.BotInterfaceResponsesTypes;
import com.jorgechp.calendarBot.common.ServerResponse;
import com.jorgechp.calendarBot.common.interfaces.IRemindable;
import com.jorgechp.calendarBot.common.interfaces.IReminderSystem;
import com.jorgechp.calendarBot.common.ServerResponsesTypes;

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



	public void startBot() throws org.telegram.telegrambots.exceptions.TelegramApiRequestException{		
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
		ServerResponsesTypes responseServer = ServerResponsesTypes.REQUEST_VOID;
		BotInterfaceResponsesTypes responseBot = BotInterfaceResponsesTypes.BOT_REQUEST_OK;
		
		String messageText = messageReceived.getText();
		BotOrder orderProcessed = botModel.processOrder(messageText);
		boolean isParsedWithoutErrors = orderProcessed.isParsedWithoutErrors();
		OrderType newOrder = orderProcessed.getOrder();
		List<String> arguments = orderProcessed.getOrderArguments();
		
		switch (newOrder) {
			case ADD_REMINDER:	
				if(isParsedWithoutErrors){
				    java.util.Date d = new Date(Long.parseLong(arguments.get(0)));	
				    
				    ServerResponse<Long> responseFromReminderSystem;
				    responseFromReminderSystem = reminderSystem.addReminder(arguments.get(1),
							arguments.get(2),
							messageReceived.getChatId());	
							    
				    if(responseFromReminderSystem.getResponseType() == ServerResponsesTypes.ADD_REMINDER_OK){
				    	long idReminder = responseFromReminderSystem.getResponseObject();
						reminderSystem.addNotification(
								idReminder,
								Instant.ofEpochMilli(d.getTime()),
								0,
								0,
								false);	
						responseServer = ServerResponsesTypes.ADD_REMINDER_OK;
				    }else{
				    	responseServer = ServerResponsesTypes.ADD_REMINDER_ERROR;
				    	}
				    responseBot = BotInterfaceResponsesTypes.BOT_REQUEST_OK;
				}else{
					responseBot = BotInterfaceResponsesTypes.BOT_ADD_REMINDER_ERROR;
				}
				break;
			case LIST:
				List<IRemindable> reminderList = reminderSystem.getAllRemindersByUser(messageReceived.getChatId());
				botModel.sendRemindersList(messageReceived.getChatId(),reminderList);
				break;
			case REMOVE_NOTIFICATION:
				ServerResponse<Boolean> responseFromReminderSystem = reminderSystem.removeNotification(
						Long.parseLong(arguments.get(0)),
						Long.parseLong(arguments.get(1)),
						messageReceived.getChatId()
						);
				responseServer = responseFromReminderSystem.getResponseType();
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
		
		botModel.processBotParseResultRequest(messageReceived.getChatId(),responseBot);
		botModel.processServerResultRequest(messageReceived.getChatId(),responseServer);
		
	}
}
