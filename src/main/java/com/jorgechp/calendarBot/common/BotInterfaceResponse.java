/**
 * 
 */
package com.jorgechp.calendarBot.common;

/**
 * @author jorge
 *
 */
public class BotInterfaceResponse<T> extends AbstractResponse<T>{

	private BotInterfaceResponsesTypes responseType;
	
	
	/**
	 * 
	 */
	public BotInterfaceResponse() {
		super();
		this.responseType = BotInterfaceResponsesTypes.BOT_REQUEST_OK;
	}

	/**
	 * @param responseType
	 */
	public BotInterfaceResponse(BotInterfaceResponsesTypes responseType) {
		super();
		this.responseType = responseType;
	}
	
	/**
	 * @param responseType
	 * @param responseObjects
	 */
	public BotInterfaceResponse(BotInterfaceResponsesTypes responseType, T responseObject) {
		super(responseObject);
		this.responseType = responseType;		
	}

	/**
	 * @return the responseType
	 */
	public BotInterfaceResponsesTypes getResponseType() {
		return responseType;
	}
	
}
