package com.jorgechp.calendarBot.TelegramBot;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import com.beust.jcommander.JCommander;
import com.jorgechp.calendarBot.TelegramBot.Entities.BotOrder;
import com.jorgechp.calendarBot.TelegramBot.Parser.AbstractParser;
import com.jorgechp.calendarBot.TelegramBot.Parser.AddReminderParser;
import com.jorgechp.calendarBot.TelegramBot.Parser.ListUserRemindersParser;
import com.jorgechp.calendarBot.TelegramBot.Parser.RemoveNotificationParser;
import com.jorgechp.calendarBot.TelegramBot.Parser.RemoveReminderParser;
import com.jorgechp.calendarBot.TelegramBot.Parser.RemoveUserParser;
import com.jorgechp.calendarBot.TelegramBot.components.OrderType;
import com.jorgechp.calendarBot.TelegramBot.components.ParamButtonResultSet;
import com.jorgechp.calendarBot.TelegramBot.interfaces.IOrderSent;
import com.jorgechp.calendarBot.common.BotInterfaceResponsesTypes;
import com.jorgechp.calendarBot.common.ServerResponsesTypes;
import com.jorgechp.calendarBot.common.interfaces.IRemindable;
import com.jorgechp.calendarBot.common.interfaces.IReminderResultSet;

/**
 * @author jorgechp 
 * @version 1.0
 * @brief Implements all method needed to represent the model of the telegram bot
 * @date 25 of January of 2017
 *  
 */
public class TelegramBotModel {
	
	private JCommander jc;
	private AddReminderParser reminderCommand;
	private RemoveReminderParser removeReminderCommand;
	private RemoveNotificationParser removeNotificationCommand;
	private RemoveUserParser removeUserCommand;
	private ListUserRemindersParser listUserReminders;
	
	
	/**
	 * Defines an error in the command
	 */
	private static final String ERROR = "ERROR";

	/**
	 * Defines the name of UnsuscribeUser command
	 */
	private static final String UNSUSCRIBE = "UNSUSCRIBE";

	/**
	 * Defines the name of the RemoveReminder command
	 */
	private static final String REMOVE_REMINDER = "REMOVE-REMINDER";

	/**
	 * Defines the name of the RemoveNotification command
	 */
	private static final String REMOVE_NOTIFICATION = "REMOVE-NOTIFICATION";
	
	/**
	 * Defines the name of the new Reminder command
	 */
	private static final String REMINDER = "REMINDER";
	
	/**
	 * Defines the name of the new List command
	 */
	private static final String LIST = "LIST";

	/**
	 * This interface defines the controller of this bot
	 */
	private TelegramBotInterfaceController botController;
	
	private DateTimeFormatter formatter;
	
	
	
	/*
	 * Regular expressions that match with different Time patterns
	 * 
	 */
	public static String HOUR_PATTERN = "((1|2)?\\d:[0-5]\\d)";	
	public static String DATE_PATTERN = "((|1|2|3)\\d\\/1?\\d\\/\\d?\\d?\\d\\d)";
	public static String DATE_HOUR_PATTERN = DATE_PATTERN + " ?" + HOUR_PATTERN + " .+";	
	public static String ADD_REMINDER_PATTERN = REMINDER+" "+ DATE_HOUR_PATTERN;
	
	/**
	 * Instantiates a {@link TelegramBotModel} object
	 * 
	 * @param orderProcessor {@link IOrderSent} interface
	 * Default constructor
	 */
	public TelegramBotModel(IOrderSent orderProcessor) {
		super();
		botController = new TelegramBotInterfaceController(
				new DefaultBotOptions(),
				orderProcessor
				);	
		
		jc = new JCommander();
		
		reminderCommand = new AddReminderParser();
		removeReminderCommand = new RemoveReminderParser();
		removeNotificationCommand = new RemoveNotificationParser();
		removeUserCommand = new RemoveUserParser();
		listUserReminders = new ListUserRemindersParser();	
		
		createCommands();
		
		formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.systemDefault());
	}
	
	/**
	 * Create new {@link AbstractParser} objects
	 */
	private void createCommands(){
		jc.addCommand(REMINDER,reminderCommand);
		jc.addCommand(REMOVE_REMINDER, removeReminderCommand);
		jc.addCommand(REMOVE_NOTIFICATION, removeNotificationCommand);
		jc.addCommand(UNSUSCRIBE, removeUserCommand);
		jc.addCommand(LIST, listUserReminders);
	}

	
	/**
	 * @param chatId
	 * @param responseBot
	 */
	public void processBotParseResultRequest(Long chatId,
			BotInterfaceResponsesTypes responseBot) {
		try {
			switch (responseBot) {
			case BOT_ADD_REMINDER_ERROR:			
					sendMessageToTelegram(chatId, "REMINDER -at 5/7/2016 -title sacar al perro -description sacar tambien la basura!");
				break;
	
			default:
				break;
			}
		} catch (TelegramApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void processServerResultRequest(long userId, ServerResponsesTypes request){
		try {
			switch (request) {
			case ADD_REMINDER_OK:			
					sendMessageToTelegram(userId, "Recordatorio añadido correctamente");
				break;
			case REMOVE_REMINDER_ERROR:
				sendMessageToTelegram(userId, "Error al eliminar el recordatorio");
				break;
			case REMOVE_NOTIFICATION_ERROR:
				sendMessageToTelegram(userId, "Error al eliminar la notificación");
				break;
			default:
				break;
			}
		} catch (TelegramApiException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the Bot Controller
	 * 
	 * @return the botController
	 */
	public TelegramBotInterfaceController getBotController() {
		return botController;
	}

	/**
	 * Sends a message to the controller
	 * 
	 * @param userId chat id
	 * @param message text of the message
	 * @throws TelegramApiException 
	 */
	public void sendMessageToTelegram(long userId, String message) throws TelegramApiException {
		SendMessage messageToSend = new SendMessage()//
				.setChatId(userId)//
		        .setText(message);
		botController.sendMessage(messageToSend);
		
	}
	
	/**
	 * Sends a custom keyboard message.
	 *  
	 * @param chatId The chat id
	 * @param messageText Message text which appears before the buttons
	 * @param listOfNotificationIds List of {@link ParamButtonResultSet} instances
	 * 
	 * @throws TelegramApiException
	 */
	public void sendCustomKeyboard(
			long chatId,
			String messageText,
			List<ParamButtonResultSet> listOfNotificationIds 
			)
			throws TelegramApiException {
		
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(messageText);

        InlineKeyboardMarkup inlineKeyboarMarkup = new InlineKeyboardMarkup();        
        
        List<List<InlineKeyboardButton>> keyboard = new ArrayList<List<InlineKeyboardButton>>();
        List<InlineKeyboardButton> keyboardRow = new ArrayList<InlineKeyboardButton>();
        
        byte numColumn = 0;
        for(ParamButtonResultSet buttonParameters : listOfNotificationIds){         	
	            InlineKeyboardButton newButton = new InlineKeyboardButton(); 
	            newButton.setText(buttonParameters.getTextElement());	       
	            newButton.setSwitchInlineQueryCurrentChat(buttonParameters.getCallBackString());
	            
	        if(buttonParameters.isTitle()){
	           keyboard.add(keyboardRow);
	           keyboardRow = new ArrayList<InlineKeyboardButton>(); 
	           keyboardRow.add(newButton);
	           keyboard.add(keyboardRow);
	           keyboardRow = new ArrayList<InlineKeyboardButton>(); 
	           numColumn = 0;
	        }
	        if(numColumn >= 3 || buttonParameters.isTitle()){
        		keyboard.add(keyboardRow);
        		keyboardRow = new ArrayList<InlineKeyboardButton>(); 
        		numColumn = 0;
        	}
	        if(!buttonParameters.isTitle()){
		        keyboardRow.add(newButton);
		        ++numColumn;
	        }
        }
        
        keyboard.add(keyboardRow);
        inlineKeyboarMarkup.setKeyboard(keyboard);        
          
        message.setReplyMarkup(inlineKeyboarMarkup);

        botController.sendMessage(message);
    }

	/**
	 * Parses a command with no params setted
	 * 
	 * @param command
	 * @return parser String[]
	 */
	private String[] parseCommandWithoutParams(String[] command){
		byte index = 0;
		byte limit = (byte) command.length;
		
		List<String> argvModified = new LinkedList<String>();
		argvModified.add(command[index++]); 
		argvModified.add("-at");
		argvModified.add(command[index++]); 
		if(command[index].matches(HOUR_PATTERN)){
			argvModified.add(command[index++]);
		}
		argvModified.add("-title");
		if(index < limit){
			while(index < limit && command[index].trim().equals("|") == false){
				argvModified.add(command[index++]);
			}
			++index;				
				if(index < limit){
					argvModified.add("-description");
					while(index < limit && command[index].trim().equals("|") == false){
						argvModified.add(command[index++]);
					}	
					++index;
					
					if(index < limit){
						argvModified.add("-periodicity");
						while(index < limit && command[index].trim().equals("|") == false){
							argvModified.add(command[index++]);
						}	
					}
					++index;
				}
			
		}

		return argvModified.toArray(new String[limit]);	
	}
	

	/**
	 * Parses a command and convert it in a {@link BotOrder} object, wich contains
	 * all information needed to send a request.
	 * 
	 * Returns a {@link BotOrder} object which contains all information about
	 * the command.
	 * 
	 * @param messageText the message to be parsed
	 * @return A {@link BotOrder} object
	 */
	public BotOrder processOrder(String messageText) {	
		OrderType newOrderType = null;	
		List<String> orderArguments = new LinkedList<String>();
		boolean isParsedWithoutErrors = true;
		
		String[] argv = messageText.split(" ");	
		
		//If user don't user params, we need to map the input adding these params
		//before calling to the parser
		if(messageText.matches(ADD_REMINDER_PATTERN)){
			argv = parseCommandWithoutParams(argv);
		}
		String selectedCommand = null;
		createCommands();
		
		try{
			jc.parse(argv);
			selectedCommand = jc.getParsedCommand();
		}catch (com.beust.jcommander.MissingCommandException e){
			selectedCommand = ERROR;
		}catch(com.beust.jcommander.ParameterException e){
			selectedCommand = jc.getParsedCommand();
			isParsedWithoutErrors = false;
		}
		
		
		switch (selectedCommand) {
		case REMINDER:	
			newOrderType = OrderType.ADD_REMINDER;
			if(isParsedWithoutErrors){				
				orderArguments.add(reminderCommand.getDate());
				orderArguments.add(reminderCommand.getReminderTitle());
				orderArguments.add(reminderCommand.getDescription());
				orderArguments.addAll(reminderCommand.getPeriodicities());
			}
			break;
		case REMOVE_REMINDER:
			newOrderType = OrderType.REMOVE_REMINDER;
			orderArguments.add(new Integer(removeReminderCommand.getReminderId()).toString());
			break;
		case REMOVE_NOTIFICATION:
			newOrderType = OrderType.REMOVE_NOTIFICATION;			
			orderArguments.add(new Long(removeNotificationCommand.getNotificationId()).toString());
			orderArguments.add(new Long(removeNotificationCommand.getReminderId()).toString());
			break;
		case LIST:
			newOrderType = OrderType.LIST;		
			break;
		case UNSUSCRIBE:
			newOrderType = OrderType.USER_UNSUSCRIBE;			
			break;	
		case ERROR:
			
		default:
			newOrderType = OrderType.ERROR;
			isParsedWithoutErrors = false;
			break;
		}
		return new BotOrder(newOrderType, orderArguments, isParsedWithoutErrors);
		
		
	}

	/**
	 * Send to the {@link TelegramBotInterfaceController} controller a list of 
	 * reminders associated of a user.
	 * 
	 * @param reminderList	 * 
	 */
	public void sendRemindersList(long idUser, List<IRemindable> reminderList)  {
		StringBuffer bufferOfMessageToSend = new StringBuffer();

		try {			
			if(reminderList.size() > 0){
				bufferOfMessageToSend.append("Tienes las siguientes alarmas:\n");
				bufferOfMessageToSend.append("\n");
				bufferOfMessageToSend.append("\n");
				sendMessageToTelegram(idUser,bufferOfMessageToSend.toString());
				bufferOfMessageToSend = new StringBuffer();
				
				ParamButtonResultSet paramToInclude;
				
				for(IRemindable reminder : reminderList){
					bufferOfMessageToSend.append("- (");
					bufferOfMessageToSend.append(reminder.getReminderId());
					bufferOfMessageToSend.append(") ");
					bufferOfMessageToSend.append(reminder.getName());
					bufferOfMessageToSend.append("\n");
					
					List<IReminderResultSet> notificationsInformation;
					List<ParamButtonResultSet> listOfNotificationIds //
							= new LinkedList<ParamButtonResultSet>();
					
					paramToInclude = new ParamButtonResultSet(
							"Eliminar recordatorio "+reminder.getReminderId(), 
							"REMOVE-REMINDER -r "+reminder.getReminderId(),
							true);				
					
					notificationsInformation = reminder.getPeriodicities();	
					
					listOfNotificationIds.add(paramToInclude);
					
					bufferOfMessageToSend.append("\nNOTIFICACIONES ASOCIADAS\n");
					for(IReminderResultSet notification : notificationsInformation){
						bufferOfMessageToSend.append("--- ");
						bufferOfMessageToSend.append("(");
						bufferOfMessageToSend.append(notification.getPeriodicity());
						bufferOfMessageToSend.append(") ");
						if(notification.getPeriodicity() > 0){
							bufferOfMessageToSend.append("Frecuencia (minutos): ");
							bufferOfMessageToSend.append(notification.getPeriodicity());
							bufferOfMessageToSend.append("\n");
						}else{
							bufferOfMessageToSend.append("Fecha: ");
							bufferOfMessageToSend.append(formatter.format(notification.getStartTime()));
							bufferOfMessageToSend.append("\n");
						}
					
						paramToInclude = new ParamButtonResultSet(
								"Eliminar notificación "+notification.getIdNotification(), 
								"REMOVE-NOTIFICATION -n "+
								notification.getIdNotification()+
								" -r "+
								notification.getIdReminder()
								);
						
						listOfNotificationIds.add(paramToInclude);	
					}				
					
					sendCustomKeyboard(
							idUser,
							bufferOfMessageToSend.toString(),
							listOfNotificationIds
							);
					bufferOfMessageToSend = new StringBuffer();
				}				
				
			}else{
				bufferOfMessageToSend.append("No tienes alarmas programadas :)");
				sendMessageToTelegram(idUser,bufferOfMessageToSend.toString());
			}		
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}


	
	

}
