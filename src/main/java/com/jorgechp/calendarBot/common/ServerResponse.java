/**
 * 
 */
package com.jorgechp.calendarBot.common;

/**
 * @author jorge
 *
 */
public class ServerResponse<T> {

	private ServerResponses responseType;
	private T responseObject;
	
	
	
	/**
	 * 
	 */
	public ServerResponse() {
		super();
		this.responseType = ServerResponses.REQUEST_OK;
	}



	/**
	 * @param responseObjects
	 */
	public ServerResponse(T responseObject) {
		super();
		this.responseObject = responseObject;
		this.responseType = ServerResponses.REQUEST_OK;
	}



	/**
	 * @param responseType
	 */
	public ServerResponse(ServerResponses responseType) {
		super();
		this.responseType = responseType;
	}
	
	



	/**
	 * @param responseType
	 * @param responseObjects
	 */
	public ServerResponse(ServerResponses responseType, T responseObject) {
		super();
		this.responseType = responseType;
		this.responseObject = responseObject;
	}



	/**
	 * @return the responseType
	 */
	public ServerResponses getResponseType() {
		return responseType;
	}



	/**
	 * @return the responseObject
	 */
	public T getResponseObject() {
		return responseObject;
	}
	
	
	
	
}
