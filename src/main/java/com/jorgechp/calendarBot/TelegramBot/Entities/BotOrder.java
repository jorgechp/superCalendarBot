package com.jorgechp.calendarBot.TelegramBot.Entities;

import java.util.List;

import com.jorgechp.calendarBot.TelegramBot.components.OrderType;

public class BotOrder {
	private OrderType order;
	private List<String> orderArguments;
	private boolean isParsedWithoutErrors;
	
	
	/**
	 * @param order
	 * @param orderArguments
	 */
	public BotOrder(OrderType order, List<String> orderArguments) {
		super();
		this.order = order;
		this.orderArguments = orderArguments;
		this.isParsedWithoutErrors = true;
	}
	
	/**
	 * @param order
	 * @param orderArguments
	 */
	public BotOrder(OrderType order, List<String> orderArguments, boolean isParsedWithoutErrors) {
		super();
		this.order = order;
		this.orderArguments = orderArguments;
		this.isParsedWithoutErrors = isParsedWithoutErrors;
	}

	/**
	 * @return the order
	 */
	public OrderType getOrder() {
		return order;
	}

	/**
	 * @return the orderArguments
	 */
	public List<String> getOrderArguments() {
		return orderArguments;
	}

	/**
	 * @return the isParsedWithoutErrors
	 */
	public boolean isParsedWithoutErrors() {
		return isParsedWithoutErrors;
	}
	
	
	

	
	
}
