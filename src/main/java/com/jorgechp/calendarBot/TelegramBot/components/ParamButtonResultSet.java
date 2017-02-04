package com.jorgechp.calendarBot.TelegramBot.components;

public class ParamButtonResultSet {

	private String textElement;
	private String callBackString;
	private boolean isTitle;
	
	
	/**
	 * @param textElement
	 * @param callBackString
	 */
	public ParamButtonResultSet(String textElement, String callBackString) {
		super();
		this.textElement = textElement;
		this.callBackString = callBackString;
		isTitle = false;
	}
	


	/**
	 * @param textElement
	 * @param callBackString
	 * @param isTitle
	 */
	public ParamButtonResultSet(String textElement, String callBackString,
			boolean isTitle) {
		super();
		this.textElement = textElement;
		this.callBackString = callBackString;
		this.isTitle = isTitle;
	}



	/**
	 * @return the idElement
	 */
	public String getTextElement() {
		return textElement;
	}


	/**
	 * @return the callBackString
	 */
	public String getCallBackString() {
		return callBackString;
	}



	/**
	 * @return the isTitle
	 */
	public boolean isTitle() {
		return isTitle;
	}
	
	
	
	
	
}
