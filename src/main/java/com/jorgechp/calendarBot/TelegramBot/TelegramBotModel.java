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
import com.jorgechp.calendarBot.TelegramBot.Parser.HelpReminderParser;
import com.jorgechp.calendarBot.TelegramBot.Parser.ListUserRemindersParser;
import com.jorgechp.calendarBot.TelegramBot.Parser.RemoveNotificationParser;
import com.jorgechp.calendarBot.TelegramBot.Parser.RemoveReminderParser;
import com.jorgechp.calendarBot.TelegramBot.Parser.RemoveUserParser;
import com.jorgechp.calendarBot.TelegramBot.components.OrderType;
import com.jorgechp.calendarBot.TelegramBot.components.ParamButtonResultSet;
import com.jorgechp.calendarBot.TelegramBot.interfaces.ICommandCreator;
import com.jorgechp.calendarBot.TelegramBot.interfaces.IOrderSent;
import com.jorgechp.calendarBot.TelegramBot.resources.Messages;
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
	private HelpReminderParser helpCommand;
	private RemoveUserParser removeUserCommand;
	private ListUserRemindersParser listUserReminders;
	
	
	/**
	 * bot's nick in Telegram
	 */	
	private String botName;
	
	/**
	 * Defines an error in the command
	 */
	private static String ERROR = Messages.getString("TelegramBot.ERROR"); //$NON-NLS-1$

	/**
	 * Defines the name of UnsuscribeUser command
	 */
	private static String UNSUSCRIBE = Messages.getString("TelegramBot.UNSUSCRIBE"); //$NON-NLS-1$

	/**
	 * Defines the name of the RemoveReminder command
	 */
	private static String REMOVE_REMINDER = Messages.getString("TelegramBot.REMOV_REMINDER"); //$NON-NLS-1$

	/**
	 * Defines the name of the RemoveNotification command
	 */
	private static String REMOVE_NOTIFICATION = Messages.getString("TelegramBot.REMOVE_NOTIFICATION"); //$NON-NLS-1$
	
	/**
	 * Defines the name of the new Reminder command
	 */
	private static String REMINDER = Messages.getString("TelegramBot.REMINDER"); //$NON-NLS-1$
	
	/**
	 * Defines the name of the new Help  command
	 */
	private static String HELP = Messages.getString("TelegramBot.COMMAND_HELP"); //$NON-NLS-1$
	
	/**
	 * Defines the name of the new List command
	 */
	private static String LIST = Messages.getString("TelegramBot.LIST"); //$NON-NLS-1$

	/**
	 * This interface defines the controller of this bot
	 */
	private TelegramBotInterfaceController botController;
	
	private DateTimeFormatter formatter;
	
	
	
	/*
	 * Regular expressions that match with different Time patterns
	 * 
	 */
	public static String HOUR_PATTERN = "((1|2)?\\d:[0-5]\\d)";	 //$NON-NLS-1$
	public static String DATE_PATTERN = "((|1|2|3)\\d\\/1?\\d\\/\\d?\\d?\\d\\d)"; //$NON-NLS-1$
	public static String DATE_HOUR_PATTERN = DATE_PATTERN + " ?" + HOUR_PATTERN + " .+";	 //$NON-NLS-1$ //$NON-NLS-2$
	public static String ADD_REMINDER_PATTERN = REMINDER+" "+ DATE_HOUR_PATTERN; //$NON-NLS-1$
	
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
		helpCommand = new HelpReminderParser();
		listUserReminders = new ListUserRemindersParser();	
		
		createCommands();
		
		formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm").withZone(ZoneId.systemDefault()); //$NON-NLS-1$
		botName = '@'+botController.getBotUsername();
	}
	
	/**
	 * Create new {@link AbstractParser} objects
	 */
	private void createCommands(){
		jc.addCommand(REMINDER,reminderCommand);
		jc.addCommand(REMOVE_REMINDER, removeReminderCommand);
		jc.addCommand(REMOVE_NOTIFICATION, removeNotificationCommand);
		jc.addCommand(UNSUSCRIBE, removeUserCommand);
		jc.addCommand(HELP, helpCommand);
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
					sendMessageToTelegram(chatId, Messages.getString("TelegramBot.REMINDER_EXAMPLE")); //$NON-NLS-1$
				break;
			case BOT_REMOVE_REMINDER_ERROR:
				sendMessageToTelegram(chatId, Messages.getString("TelegramBot.REMOVE_REMINDER_EXAMPLE")); //$NON-NLS-1$
				break;	
			case BOT_REMOVE_NOTIFICATION_ERROR:
				sendMessageToTelegram(chatId, Messages.getString("TelegramBot.REMOVE_NOTIFICATION_EXAMPLE")); //$NON-NLS-1$
				break;	
			case BOT_COMMAND_HELP:
				sendMessageToTelegram(chatId, Messages.getString("HELP"));
				break;
			default:
				List<ParamButtonResultSet> listOfNotificationIds //
				= new LinkedList<ParamButtonResultSet>();
				
				ParamButtonResultSet paramToInclude = new ParamButtonResultSet(
						Messages.getString("TelegramBot.VIEW_HELP"),  //$NON-NLS-1$
						Messages.getString("TelegramBot.COMMAND_HELP"), //$NON-NLS-1$
						true);
				
				listOfNotificationIds.add(paramToInclude);
				sendCustomKeyboard(
						chatId,
						Messages.getString("TelegramBot.COMMAND_ERROR_HELP"),
						listOfNotificationIds
						);
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
					sendMessageToTelegram(userId, Messages.getString("TelegramBot.NEW_REMINDER_OK")); //$NON-NLS-1$
				break;
			case REMOVE_REMINDER_ERROR:
				sendMessageToTelegram(userId, Messages.getString("TelegramBot.NEW_REMINDER_ERROR")); //$NON-NLS-1$
				break;
			case REMOVE_NOTIFICATION_ERROR:
				sendMessageToTelegram(userId, Messages.getString("TelegramBot.NOTIFICATION_ERROR")); //$NON-NLS-1$
				break;
			case USER_NOT_FOUND_ERROR:
				sendMessageToTelegram(userId, Messages.getString("TelegramBot.ADD_USER_OK")); //$NON-NLS-1$
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
		argvModified.add(Messages.getString("TelegramBot.PARAMETER_AT")); //$NON-NLS-1$
		argvModified.add(command[index++]); 
		if(command[index].matches(HOUR_PATTERN)){
			argvModified.add(command[index++]);
		}
		argvModified.add(Messages.getString("TelegramBot.PARAMETER_TITLE")); //$NON-NLS-1$
		if(index < limit){
			while(index < limit && command[index].trim().equals("|") == false){ //$NON-NLS-1$
				argvModified.add(command[index++]);
			}
			++index;				
				if(index < limit){
					argvModified.add(Messages.getString("TelegramBot.PARAMETER_DESCRIPTION")); //$NON-NLS-1$
					while(index < limit && command[index].trim().equals("|") == false){ //$NON-NLS-1$
						argvModified.add(command[index++]);
					}	
					++index;
					
					if(index < limit){
						argvModified.add(Messages.getString("TelegramBot.PARAMETER_PERIODICITY")); //$NON-NLS-1$
						while(index < limit && command[index].trim().equals("|") == false){ //$NON-NLS-1$
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
		
		String parsedString = messageText;
		if(messageText.contains(botName)){
			parsedString = messageText.substring(botName.length()+1);
		}
		
		String[] argv = parsedString.split(" ");	 //$NON-NLS-1$
		
		//If user don't user params, we need to map the input adding these params
		//before calling to the parser
		if(parsedString.matches(ADD_REMINDER_PATTERN)){
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
		
		JCommander parsedJCommander = jc.getCommands().get(selectedCommand);
		if(parsedJCommander != null){
			AbstractParser abstractParser = (AbstractParser) parsedJCommander.getObjects().get(0);		
			orderArguments = abstractParser.createCommand();
			newOrderType = abstractParser.getOrderType();
		}else{
			newOrderType = OrderType.ERROR;
			isParsedWithoutErrors = false;			
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
				bufferOfMessageToSend.append(Messages.getString("TelegramBot.ALARM_LIST"+'\n')); //$NON-NLS-1$
				bufferOfMessageToSend.append("\n"); //$NON-NLS-1$
				bufferOfMessageToSend.append("\n"); //$NON-NLS-1$
				sendMessageToTelegram(idUser,bufferOfMessageToSend.toString());
				bufferOfMessageToSend = new StringBuffer();
				
				ParamButtonResultSet paramToInclude;
				
				for(IRemindable reminder : reminderList){
					bufferOfMessageToSend.append("- ("); //$NON-NLS-1$
					bufferOfMessageToSend.append(reminder.getReminderId());
					bufferOfMessageToSend.append(") "); //$NON-NLS-1$
					bufferOfMessageToSend.append(reminder.getName());
					bufferOfMessageToSend.append("\n"); //$NON-NLS-1$
					
					List<IReminderResultSet> notificationsInformation;
					List<ParamButtonResultSet> listOfNotificationIds //
							= new LinkedList<ParamButtonResultSet>();
					
					paramToInclude = new ParamButtonResultSet(
							Messages.getString("TelegramBot.REMOVE_REMINDER")+reminder.getReminderId(),  //$NON-NLS-1$
							"REMOVE-REMINDER -r "+reminder.getReminderId(), //$NON-NLS-1$
							true);				
					
					notificationsInformation = reminder.getPeriodicities();	
					
					listOfNotificationIds.add(paramToInclude);
					
					bufferOfMessageToSend.append(Messages.getString('\n'+"TelegramBot.NOTIFICATION_LIST"+'\n')); //$NON-NLS-1$
					for(IReminderResultSet notification : notificationsInformation){
						bufferOfMessageToSend.append("--- "); //$NON-NLS-1$
						bufferOfMessageToSend.append("("); //$NON-NLS-1$
						bufferOfMessageToSend.append(notification.getPeriodicity());
						bufferOfMessageToSend.append(") "); //$NON-NLS-1$
						if(notification.getPeriodicity() > 0){
							bufferOfMessageToSend.append(Messages.getString("TelegramBot.FREQUENCY")); //$NON-NLS-1$
							bufferOfMessageToSend.append(notification.getPeriodicity());
							bufferOfMessageToSend.append("\n"); //$NON-NLS-1$
						}else{
							bufferOfMessageToSend.append(Messages.getString("TelegramBot.DATE")); //$NON-NLS-1$
							bufferOfMessageToSend.append(formatter.format(notification.getStartTime()));
							bufferOfMessageToSend.append("\n"); //$NON-NLS-1$
						}
					
						paramToInclude = new ParamButtonResultSet(
								Messages.getString("TelegramBot.REMOVE_NOTIFICATION_TEXT")+notification.getIdNotification(),  //$NON-NLS-1$
								"REMOVE-NOTIFICATION -n "+ //$NON-NLS-1$
								notification.getIdNotification()+
								" -r "+ //$NON-NLS-1$
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
				bufferOfMessageToSend.append(Messages.getString("TelegramBot.NO_ALARMS")); //$NON-NLS-1$
				sendMessageToTelegram(idUser,bufferOfMessageToSend.toString());
			}		
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}


	
	

}
