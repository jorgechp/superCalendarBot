/**
 * 
 */
package com.jorgechp.calendarBot.common;

/**
 * @author jorge
 *
 */
public class ServerResponse<T> extends AbstractResponse<T>{

	private ServerResponsesTypes responseType;
	
	
	/**
	 * 
	 */
	public ServerResponse() {
		super();
		this.responseType = ServerResponsesTypes.REQUEST_OK;
	}

	/**
	 * @param responseType
	 */
	public ServerResponse(ServerResponsesTypes responseType) {
		super();
		this.responseType = responseType;
	}
	
	/**
	 * @param responseType
	 * @param responseObjects
	 */
	public ServerResponse(ServerResponsesTypes responseType, T responseObject) {
		super(responseObject);
		this.responseType = responseType;		
	}

	/**
	 * @return the responseType
	 */
	public ServerResponsesTypes getResponseType() {
		return responseType;
	}
	
}
