/**
 * 
 */
package com.jorgechp.calendarBot.common;

/**
 * @author jorge
 *
 *TODO THIS CLASSS IS NOT LONGER NECCESARY
 */
public abstract class AbstractResponse<T> {
	protected T responseObject;
		
	
	/**
	 * 
	 */
	public AbstractResponse() {
		super();
	}

	/**
	 * @param responseObjects
	 */
	public AbstractResponse(T responseObject) {
		super();
		this.responseObject = responseObject;		
	}
	
	/**
	 * @return the responseObject
	 */
	public T getResponseObject() {
		return responseObject;
	}
	
	
	
}
