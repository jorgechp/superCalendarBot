/**
 * 
 */
package com.jorgechp.calendarBot.TelegramBot.components.Interactive;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import com.jorgechp.calendarBot.TelegramBot.components.ParamButtonResultSet;

/**
 * @author jorgechp 
 * @version 1.0
 * @brief Implements all methods needed to make an interactive command build
 * @date 19 of february of 2017 
 */
public abstract class InteractiveCommandBuilder {

	/**
	 * Stores a collection of param value needed to build the command
	 */
	private Queue<Map.Entry<String, String>> paramsQueue;
	
	/**
	 * Stores a collection of {@link ParamButtonResultSet} objects needed to 
	 * interactively build a command
	 */
	private Map<Integer,ParamButtonResultSet> buttonResultsetMap;
	
	/**
	 * Public constructor
	 */
	public InteractiveCommandBuilder() {
		super();
		paramsQueue = new LinkedList<Map.Entry<String, String>>();
		buttonResultsetMap = new HashMap<Integer,ParamButtonResultSet>();
	}
	
	/**
	 * Inserts a new element into the params map
	 * 
	 * @param key
	 * @param value
	 */
	public void pushParam(String key, String value){
		paramsQueue.add(new AbstractMap.SimpleEntry<String,String>(key,value));
	}
	
	/**
	 * Pops the last inserted param
	 */
	public void popParam(){
		paramsQueue.poll();
	}
	
	/**
	 * Returns a {@link ParamButtonResultSet} to be sent to the User Interface.
	 * This instance provides with the buttons required to continue
	 * building the command.
	 * 
	 * @return an instance of a {@link ParamButtonResultSet} object
	 */
	public ParamButtonResultSet getButtonResultSet(){
		return buttonResultsetMap.get(paramsQueue.size());
	}
	
	/**
	 * Returns the command builded
	 * @return String with the new command
	 */
	public String buildCommand(){
		StringBuilder commandToBuild = new StringBuilder();
		for (Map.Entry<String, String> param : paramsQueue) {
			commandToBuild.append(param.getKey());
			commandToBuild.append(' ');
			commandToBuild.append(param.getValue());
			commandToBuild.append(' ');
		}
		return commandToBuild.toString();
	}
	
	
}
