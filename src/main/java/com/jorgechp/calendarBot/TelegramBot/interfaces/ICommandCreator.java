/**
 * 
 */
package com.jorgechp.calendarBot.TelegramBot.interfaces;

import java.util.LinkedList;

import com.jorgechp.calendarBot.TelegramBot.Parser.AbstractParser;

/**
 * @author jorge
 *
 */
public interface ICommandCreator<T extends AbstractParser> {

	public LinkedList<String> createArguments(T arguments);
}
