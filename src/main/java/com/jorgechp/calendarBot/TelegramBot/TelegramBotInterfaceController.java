/**
 * 
 */
package com.jorgechp.calendarBot.TelegramBot;

import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import com.jorgechp.calendarBot.TelegramBot.interfaces.IOrderSent;

/**
 * @author jorge
 *
 */
public class TelegramBotInterfaceController extends TelegramLongPollingBot  {
	private IOrderSent orderProcessor;
	
	/**
	 * @param options
	 */
	public TelegramBotInterfaceController(DefaultBotOptions options,
			IOrderSent orderProcessor) {
		super(options);		
		this.orderProcessor = orderProcessor;
	}	
	

	public String getBotUsername() {
		return PropertiesReader.getString("Bot.name"); //$NON-NLS-1$
	}

	public void onUpdateReceived(Update updateReceived) {

	    if (updateReceived.hasMessage() && updateReceived.getMessage().hasText()) {
	    	orderProcessor.processMesage(updateReceived.getMessage());
	    }    
	    
	}
	
	

	@Override
	public String getBotToken() {
		return PropertiesReader.getString("Bot.key"); //$NON-NLS-1$
	}
	
	

}
