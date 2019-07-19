package com.heb.util.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.MessageContext;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A set of methods  that make writing web service code easier.
 *
 * @author d116773
 * @since 2.0.1
 */
public final class WebServiceHelper {

	public static final String API_KEY_HEADER_NAME = "apiKey";

	private static final Logger logger = LoggerFactory.getLogger(WebServiceHelper.class);

	// constructor is private since all methods are static and the class is final
	private WebServiceHelper() {}

	/**
	 * Points a web service at a different server. This will allow the same code to move across
	 * environments.
	 *
	 * @param provider The BindingProvider of the web service.
	 * @param endPointUrl The URL to move the web service to.
	 */
	public static void moveEndpoint(BindingProvider provider, String endPointUrl) {
		// If the provider passed in is null, log it as an error, but don't throw an
		// exception. This is not worth breaking the application over.
		if (provider == null || provider.getRequestContext() == null) {
			WebServiceHelper.logger.error("incomplete binding provider");
			return;
		}

		WebServiceHelper.logger.debug("Moving web service to " + endPointUrl);

		provider.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endPointUrl);
	}

	/**
	 * Sets a header of the web service to include the API key needed to identify this application.
	 *
	 * @param provider The BindingProvider of the web service.
	 * @param apiKey The API key to use when calling the service.
	 */
	public static void setApiKey(BindingProvider provider, String apiKey) {
		// If the provider passed in is null, log it as an error, but don't throw an
		// exception. This is not worth breaking the application over (though not having
		// an API key may break the application later).
		if (provider == null || provider.getRequestContext() == null) {
			WebServiceHelper.logger.error("incomplete binding provider");
			return;
		}

		WebServiceHelper.logger.debug("Setting API Key: " + apiKey);

		Map<String, List<String>> headers = new HashMap<>();
		headers.put(WebServiceHelper.API_KEY_HEADER_NAME, Arrays.asList(apiKey));
		provider.getRequestContext().put(MessageContext.HTTP_REQUEST_HEADERS, headers);
	}

	/**
	 * Adds a message logger to the web service. This can be used to print out the XML messages
	 * and drop them into a tool like SOAP UI for debugging.
	 *
	 * @param provider The BindingProvider of the web service.
	 * @param logger The SoapMessageLogger to use to log messages.
	 */
	@SuppressWarnings("rawtypes")
	public static void addMessageLogger(BindingProvider provider, SoapMessageLogger logger) {

		// If the provider passed in is null, log it as an error, but don't throw an
		// exception. This is not worth breaking the application over.
		if (provider == null || provider.getBinding() == null) {
			WebServiceHelper.logger.error("incomplete binding provider");
			return;
		}

		List<Handler> handlers = provider.getBinding().getHandlerChain();

		handlers.add(logger);
		provider.getBinding().setHandlerChain(handlers);
	}
}
