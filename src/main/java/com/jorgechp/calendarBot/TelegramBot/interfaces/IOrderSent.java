/**
 * 
 */
package com.jorgechp.calendarBot.TelegramBot.interfaces;

import org.telegram.telegrambots.api.objects.Message;

/**
 * @author jorge
 *
 */
public interface IOrderSent {
	public void processMesage(Message message);

}
