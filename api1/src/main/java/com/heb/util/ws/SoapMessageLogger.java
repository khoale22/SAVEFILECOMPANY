package com.heb.util.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Set;

/**
 * This class will log SOAP XML messages to a log file if logging is set to DEBUG or TRACE. Any
 * other logging level will keep this from producing any output. You can enable this class by
 * adding it to the handler chain of a BindingProvider. You can target inbound XML messages,
 * outbound XML messages, or both.
 *
 * Created by d116773 on 2/15/2016.
 */
public class SoapMessageLogger implements SOAPHandler<SOAPMessageContext> {

	private static final Logger logger = LoggerFactory.getLogger(SoapMessageLogger.class);

	protected static final String OUTBOUND_MESSAGE = "OUTBOUND MESSAGE: ";
	protected static final String INBOUND_MESSAGE = "INBOUND MESSAGE: ";

	private boolean logInbound;
	private boolean logOutbound;

	/**
	 * Method does nothing.
	 * @return Always returns null.
	 */
	@Override
	public Set<QName> getHeaders() {
		return null;
	}

	/**
	 * This is the method that logs the XML messages.
	 * @param context The SOAP message context that contains the message.
	 * @return Always returns true even if there is some kind of problem logging the message.
	 */
	@Override
	public boolean handleMessage(SOAPMessageContext context) {

		// if logging is not configured for debug or trace, just exit
		if (!SoapMessageLogger.shouldLog()) {
			return true;
		}

		// if the context or message is null, log it as an error and return true
		// to keep messages processing
		if (context == null) {
			SoapMessageLogger.logger.error("SOAPMessageContext NULL");
			return true;
		}

		SOAPMessage message = context.getMessage();
		if (message == null) {
			SoapMessageLogger.logger.error("SOAPMessageContext contained NULL message");
			return true;
		}

		boolean isOutboundMessage = SoapMessageLogger.isOutboundMessage(context);

		// log messages if this is an outbound message and the class
		// is configured to log outbound messages
		if (this.logOutbound && isOutboundMessage) {
			SoapMessageLogger.logMessage(SoapMessageLogger.OUTBOUND_MESSAGE, message);
		}

		// log messages if this is an inbound message and the class
		// is configured to log inbound messages
		if (this.logInbound && !isOutboundMessage) {
			SoapMessageLogger.logMessage(SoapMessageLogger.INBOUND_MESSAGE, message);
		}

		// return true to keep the message processing
		return true;
	}

	/**
	 * Method does nothing.
	 * @param context The SOAPMessageContext of the fault.
	 * @return Always returns true.
	 */
	@Override
	public boolean handleFault(SOAPMessageContext context) {
		// returns true so the fault keeps processing
		return true;
	}

	/**
	 * Method does nothing.
	 * @param context The SOAPMessageContext being closed.
	 */
	@Override
	public void close(MessageContext context) {
	}

	/**
	 * Returns true if logging of inbound messages is turned on.
	 * @return True if logging of inbound messages is turned on and false otherwise.
	 */
	public boolean logInbound() {
		return this.logInbound;
	}

	/**
	 * Turns on or off logging of inbound messages.
	 * @param logInbound True to turn on logging of inbound messages and false otherwise.
	 */
	public void setLogInbound(boolean logInbound) {
		this.logInbound = logInbound;
	}

	/**
	 * Returns true if logging of outbound messages is turned on.
	 * @return True if logging of outbound messages is turned on and false otherwise.
	 */
	public boolean logOutbound() {
		return this.logOutbound;
	}

	/**
	 * Turns on or off logging of outbound messages.
	 * @param logOutbound True to turn on logging of outbound messages and false otherwise.
	 */
	public void setLogOutbound(boolean logOutbound) {
		this.logOutbound = logOutbound;
	}

	/**
	 * Utility function which will return true if the message is outbound and false otherwise.
	 *
	 * @param context The context of the SOAP message.
	 * @return True if the message is an outbound message and false otherwise. If it is indetermined,
	 * the method returns false.
	 */
	protected static boolean isOutboundMessage(SOAPMessageContext context) {
		Object obj = context.get (MessageContext.MESSAGE_OUTBOUND_PROPERTY);

		if (obj instanceof Boolean) {
			return (Boolean) obj;
		} else {
			return false;
		}
	}

	/**
	 * Utility function which will help decide if it's worth processing the logging
	 * based on the log level
	 * @return True if this class should log and false otherwise.
	 */
	protected static boolean shouldLog() {
		return SoapMessageLogger.logger.isDebugEnabled() || SoapMessageLogger.logger.isTraceEnabled();
	}

	/**
	 * Does the actual logging of SOAP messages to the class's log file.
	 * @param inOutMessage Message to add to the beginning to indicate if it is an inbound or outbound message.
	 * @param message The actual SOAP message to log.
	 */
	private static void logMessage(String inOutMessage, SOAPMessage message) {

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		try {
			message.writeTo(bos);
			SoapMessageLogger.logger.debug(inOutMessage + bos.toString());
		} catch (SOAPException | IOException e) {
			SoapMessageLogger.logger.error(e.getMessage());
		}

	}
}
