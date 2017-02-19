/**
 * 
 */
package com.jorgechp.calendarBot.TelegramBot.interfaces;

import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.api.objects.Message;

/**
 * @author jorge
 *
 */
public interface IOrderSent {
	public void processMesage(Message message);

	/**
	 * Process a {@link CallbackQuery} from the server
	 * @param callbackQuery the {@link CallbackQuery} to process
	 */
	public void processCallBackQuery(CallbackQuery callbackQuery);

}
