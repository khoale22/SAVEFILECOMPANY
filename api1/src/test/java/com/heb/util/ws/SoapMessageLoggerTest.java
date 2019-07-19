package com.heb.util.ws;

import com.heb.util.ws.scaffolding.appender.StringHoldingAppender;
import com.heb.util.ws.scaffolding.fakeSoap.FakeSoapMessage;
import com.heb.util.ws.scaffolding.fakeSoap.FakeSoapMessageContext;
import org.apache.log4j.Appender;
import org.apache.log4j.Category;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.namespace.QName;
import java.util.Set;

/**
 * Created by d116773 on 2/16/2016.
 */
public class SoapMessageLoggerTest {

	private static final String TEST_APPENDER_NAME = "test";

	private SoapMessageLogger logger = new SoapMessageLogger();
	private FakeSoapMessageContext messageContext = new FakeSoapMessageContext();


	//@Test
	public void testHandleMessageOutboundOn() {
		this.setLoggingLevel(Level.DEBUG);
		this.logger.setLogOutbound(true);
		this.messageContext.setOutbound(true);

		boolean b = this.logger.handleMessage(this.messageContext);

		StringHoldingAppender appender = this.getTestAppender(SoapMessageLogger.class);

		Assert.assertEquals("outbound message logged incorrectly", appender.getLastMessage(), SoapMessageLogger.OUTBOUND_MESSAGE + FakeSoapMessage.TEST_MESSGE);

		Assert.assertTrue("handleMessage returned false", b);
	}

	//@Test
	public void testHandleMessageOutboundOff() {
		this.setLoggingLevel(Level.DEBUG);
		this.logger.setLogOutbound(false);
		this.messageContext.setOutbound(true);

		StringHoldingAppender appender = this.getTestAppender(SoapMessageLogger.class);
		appender.resetLastMessge();

		boolean b = this.logger.handleMessage(this.messageContext);

		Assert.assertNull("handleMessage wrote log message", appender.getLastMessage());

		Assert.assertTrue("handleMessage returned false", b);
	}

	//@Test
	public void testHandleMessageInboundOn() {
		this.setLoggingLevel(Level.DEBUG);
		this.logger.setLogInbound(true);
		this.messageContext.setOutbound(false);

		boolean b = this.logger.handleMessage(this.messageContext);

		StringHoldingAppender appender = this.getTestAppender(SoapMessageLogger.class);

		Assert.assertEquals("inbound message logged incorrectly", appender.getLastMessage(), SoapMessageLogger.INBOUND_MESSAGE + FakeSoapMessage.TEST_MESSGE);

		Assert.assertTrue("handleMessage returned false", b);
	}

	//@Test
	public void testHandleMessageInboundOff() {
		this.setLoggingLevel(Level.DEBUG);
		this.logger.setLogInbound(false);
		this.messageContext.setOutbound(false);

		StringHoldingAppender appender = this.getTestAppender(SoapMessageLogger.class);
		appender.resetLastMessge();

		boolean b = this.logger.handleMessage(this.messageContext);

		Assert.assertNull("handleMessage wrote log message", appender.getLastMessage());

		Assert.assertTrue("handleMessage returned false", b);
	}

	//@Test
	public void testGetHeadersReturnsNull() {
		Set<QName> qNames = this.logger.getHeaders();
		Assert.assertNull("getHeaders did not return null", qNames);
	}

	//@Test
	public void testHandleFaultReturnsTrue() {
		boolean b = this.logger.handleFault(null);
		Assert.assertTrue("handle fault did not return true", b);
	}

	//@Test
	public void testIsOutboundMessageReturnsTrue() {
		this.messageContext.setOutbound(true);
		boolean b = this.logger.isOutboundMessage(this.messageContext);
		Assert.assertTrue("isOutbound returned false", b);
	}

	//@Test
	public void testIsOutboundMessageReturnsFalse() {
		this.messageContext.setOutbound(false);
		boolean b = this.logger.isOutboundMessage(this.messageContext);
		Assert.assertFalse("isOutbound returned true", b);
	}

	//@Test
	public void testShouldLogReturnsTrueDebug() {
		Logger logger = Logger.getLogger(SoapMessageLogger.class);
		logger.setLevel(Level.DEBUG);
		boolean b = SoapMessageLogger.shouldLog();
		Assert.assertTrue("shouldLog returns false", b);
	}

	//@Test
	public void testShouldLogReturnsTrueAll() {
		Logger logger = Logger.getLogger(SoapMessageLogger.class);
		logger.setLevel(Level.ALL);
		boolean b = SoapMessageLogger.shouldLog();
		Assert.assertTrue("shouldLog returns false", b);
	}

	//@Test
	public void testShouldLogReturnsTrueTrace() {
		Logger logger = Logger.getLogger(SoapMessageLogger.class);
		logger.setLevel(Level.TRACE);
		boolean b = SoapMessageLogger.shouldLog();
		Assert.assertTrue("shouldLog returns false", b);
	}

	//@Test
	public void testShouldLogReturnsFalseInfo() {
		Logger logger = Logger.getLogger(SoapMessageLogger.class);
		logger.setLevel(Level.INFO);
		boolean b = SoapMessageLogger.shouldLog();
		Assert.assertFalse("shouldLog returns true", b);
	}

	//@Test
	public void testShouldLogReturnsFalseError() {
		Logger logger = Logger.getLogger(SoapMessageLogger.class);
		logger.setLevel(Level.ERROR);
		boolean b = SoapMessageLogger.shouldLog();
		Assert.assertFalse("shouldLog returns true", b);
	}

	//@Test
	public void testShouldLogReturnsFalseFatal() {
		Logger logger = Logger.getLogger(SoapMessageLogger.class);
		logger.setLevel(Level.FATAL);
		boolean b = SoapMessageLogger.shouldLog();
		Assert.assertFalse("shouldLog returns true", b);
	}

	//@Test
	public void testShouldLogReturnsFalseWarn() {
		Logger logger = Logger.getLogger(SoapMessageLogger.class);
		logger.setLevel(Level.WARN);
		boolean b = SoapMessageLogger.shouldLog();
		Assert.assertFalse("shouldLog returns true", b);
	}

	//@Test
	public void testShouldLogReturnsFalseOff() {
		Logger logger = Logger.getLogger(SoapMessageLogger.class);
		logger.setLevel(Level.OFF);
		boolean b = SoapMessageLogger.shouldLog();
		Assert.assertFalse("shouldLog returns true", b);
	}

	private void setLoggingLevel(Level level) {
		Logger logger = Logger.getLogger(SoapMessageLogger.class);
		logger.setLevel(level);
	}

	/**
	 * Recursively goes through loggers until it gets one with an appender named "test".
	 * This appender be configured in the log4j properties files for these tests.
	 *
	 * @param logger A logger to test for the an appender named "test".
	 * @return The logger given the sourceField "test".
	 */
	private StringHoldingAppender extractTestAppender(Category logger) {

		Appender appender = logger.getAppender(SoapMessageLoggerTest.TEST_APPENDER_NAME);
		if (appender != null) {
			if (appender instanceof StringHoldingAppender) {
				return (StringHoldingAppender) appender;
			} else {
				return null;
			}
		} else {
			if (logger.getParent() == null) {
				return null;
			} else {
				return extractTestAppender(logger.getParent());
			}

		}
	}

	/**
	 * Returns a StringHoldingLogger appender configured with the sourceField "test" for a given class.
	 * This should be configured in the log4j properties files for these tests.
	 *
	 * @param clazz The class to find a logger for.
	 * @return The logger given the sourceField "test".
	 */
	private StringHoldingAppender getTestAppender(Class clazz) {
		Logger logger = Logger.getLogger(clazz);
		StringHoldingAppender appender = this.extractTestAppender(logger);
		if (appender == null) {
			Assert.fail("Misconfiguration: create a log appender named " + SoapMessageLoggerTest.TEST_APPENDER_NAME);
		}
		return appender;
	}
}
