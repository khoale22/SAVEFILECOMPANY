package com.heb.util.ws.scaffolding.appender;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.spi.LoggingEvent;

/**
 * Created by d116773 on 2/16/2016.
 */
public class StringHoldingAppender extends AppenderSkeleton {

	private String lastMessage;

	public String getLastMessage() {
		return this.lastMessage;
	}

	public void resetLastMessge() {
		this.lastMessage = null;
	}

	@Override
	protected void append(LoggingEvent event) {
		this.lastMessage = event.getMessage().toString();
	}

	@Override
	public void close() {

	}

	@Override
	public boolean requiresLayout() {
		return false;
	}
}
