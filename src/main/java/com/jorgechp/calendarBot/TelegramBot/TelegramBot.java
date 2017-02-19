/**
 * 
 */
package com.jorgechp.calendarBot.TelegramBot;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.jorgechp.calendarBot.ReminderSystem.entities.interfaces.INotificationListener;
import com.jorgechp.calendarBot.TelegramBot.Entities.BotOrder;
import com.jorgechp.calendarBot.TelegramBot.components.OrderType;
import com.jorgechp.calendarBot.TelegramBot.components.Interactive.InteractiveCommandBuilder;
import com.jorgechp.calendarBot.TelegramBot.interfaces.IOrderSent;
import com.jorgechp.calendarBot.common.BotInterfaceResponsesTypes;
import com.jorgechp.calendarBot.common.ServerResponse;
import com.jorgechp.calendarBot.common.ServerResponsesTypes;
import com.jorgechp.calendarBot.common.interfaces.IRemindable;
import com.jorgechp.calendarBot.common.interfaces.IReminderSystem;

/**
 * @author jorge
 *
 */
public class TelegramBot implements INotificationListener, IOrderSent {

	private TelegramBotModel botModel;
	private IReminderSystem reminderSystem;
	private Map<Integer,InteractiveCommandBuilder> interactiveCommandBuildMap;
	
	/**
	 * Default constructor
	 */
	public TelegramBot() {
		super();
		botModel = new TelegramBotModel(this);	
		interactiveCommandBuildMap = new HashMap<Integer,InteractiveCommandBuilder>();
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



	/**
	 * Initialize the bot
	 * @throws org.telegram.telegrambots.exceptions.TelegramApiRequestException
	 */
	public void startBot() throws org.telegram.telegrambots.exceptions.TelegramApiRequestException{		
		ApiContextInitializer.init(); 
		TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            botsApi.registerBot(botModel.getBotController());            
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
	}


	/**
	 * Sends a meessage to the user
	 * 
	 * @param title the title of the message
	 * @param description the description of the message
	 * @param userId the destinatary of the message
	 */
	public void dispatchListener(String title, String description, long userId,
			long reminderId) {
		try {
			botModel.sendMessageToTelegram(userId, title);
		} catch (TelegramApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	/**
	 * Processes a message
	 * @param messageText message to process
	 * @param userId user id
	 */
	public void processMesage(String messageText, long userId){
		ServerResponsesTypes responseServer = ServerResponsesTypes.REQUEST_VOID;
		BotInterfaceResponsesTypes responseBot = BotInterfaceResponsesTypes.BOT_REQUEST_OK;
		
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
							userId);	
							    
				    responseServer = responseFromReminderSystem.getResponseType();
				    if(responseServer == ServerResponsesTypes.ADD_REMINDER_OK){
				    	long idReminder = responseFromReminderSystem.getResponseObject();
						reminderSystem.addNotification(
								idReminder,
								Instant.ofEpochMilli(d.getTime()),
								0,
								0,
								false);							
				    }	
				    
				}
				break;
			case LIST:
				List<IRemindable> reminderList = reminderSystem.getAllRemindersByUser(userId);
				botModel.sendRemindersList(userId,reminderList);
				break;
			case REMOVE_NOTIFICATION:
				if(isParsedWithoutErrors){
					ServerResponse<Boolean> responseFromReminderSystem = reminderSystem.removeNotification(
							Long.parseLong(arguments.get(0)),
							Long.parseLong(arguments.get(1)),
							userId
							);
					responseServer = responseFromReminderSystem.getResponseType();						
				}
				
				break;
			case REMOVE_REMINDER:
				if(isParsedWithoutErrors){
					reminderSystem.removeReminder(
							Long.parseLong(arguments.get(0)),
							userId
							);				
					break;
				}else{
					responseBot = BotInterfaceResponsesTypes.BOT_REMOVE_REMINDER_ERROR;
				}
			case HELP:
				responseBot = BotInterfaceResponsesTypes.BOT_COMMAND_HELP;
				break;
			case ERROR:
				responseBot = BotInterfaceResponsesTypes.BOT_COMMAND_ERROR;
				break;

		default:
			break;
		}
			
		responseServer = handleServerResponse(responseServer,userId);
		
		botModel.processBotParseResultRequest(userId,responseBot);
		botModel.processServerResultRequest(userId,responseServer);
	}
	
	/**
	 * Processes a {@link Message} received
	 * 
	 * @param messageReceived instance of a {@link Message} object
	 */
	public void processMesage(Message messageReceived)  {
		processMesage(messageReceived.getText(), messageReceived.getChatId());		
	}



	/**
	 * @param responseServer
	 * @param messageReceived
	 * @return
	 */
	private ServerResponsesTypes handleServerResponse(ServerResponsesTypes responseServer, long chatId) {
		if(responseServer == ServerResponsesTypes.USER_NOT_FOUND_ERROR){
			reminderSystem.addUser(chatId);
		}
		return responseServer;
	}



	/* (non-Javadoc)
	 * @see com.jorgechp.calendarBot.TelegramBot.interfaces.IOrderSent#processCallBackQuery(org.telegram.telegrambots.api.objects.CallbackQuery)
	 */
	@Override
	public void processCallBackQuery(CallbackQuery callbackQuery) {
		String messageData = callbackQuery.getData();
		if(messageData.startsWith("I")){//entering into Interactive mode
			String dataToProcess = messageData.split(";")[1];
			InteractiveCommandBuilder commandBuilder = 
					interactiveCommandBuildMap.get(callbackQuery.getMessage().getChatId());
			
			
		}
		processMesage(callbackQuery.getMessage());		
	}
}
