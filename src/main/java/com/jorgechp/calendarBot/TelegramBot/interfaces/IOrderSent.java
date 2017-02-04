/**
 * 
 */
package com.jorgechp.calendarBot.TelegramBot.interfaces;

import org.telegram.telegrambots.api.objects.Message;

import com.jorgechp.calendarBot.common.ServerResponses;

/**
 * @author jorge
 *
 */
public interface IOrderSent {
	public void processMesage(Message message);

}
