package com.heb.util.ws;

import com.heb.xmlns.ei.authentication.Authentication;
import com.heb.xmlns.ei.providerfaultschema.ProviderSOAPErrorMsg;
import com.heb.xmlns.ei.providerfaultschema.ProviderSOAPFault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.xml.ws.BindingProvider;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;


/**
 * This is a base class for classes that call web-services. Subclasses should implement the getServiceAgent
 * and getServicePort methods to return the service agent and port instances.  Additionally, subclasses should override
 * getApiKey to set the API key, getWebServiceUri to point to the web service, and getLogInboundMessages and
 * getLogOutboundMessages to set logging of the XML messages themselves. If these functions return nulls, default
 * behavior will call the web service with the URL in the generated classes for the service without an API key.
 *
 * Subclasses should add further methods to call the actual service methods themselves.
 *
 * This class will help manage the lifecycle of the objects, manage the API key used to identify the application
 * with the web-services gateway, aid with some debug message logging, and manage pointing the service to different
 * servers in different environment.
 *
 * For the XML messages to be logged, the logger on the SoapMessageLogger class must be set to
 * DEBUG for these messages be logged as well.
 *
 * @param <A> The type of class that creates the port.
 * @param <P> The type of class that calls web-services. Because of the way wsimoprt creates
 * these classes, this cannot be enforced by the compiler, but this class should be castable to a BindingProvider.
 *
 * @author d116773
 * @since 2.0.1
 */
public abstract  class BaseWebServiceClient<A, P> {

	private static final Logger logger = LoggerFactory.getLogger(BaseWebServiceClient.class);
	private static final String CANNOT_CAST_MESSAGE = " cannot be cast to javax.xml.ws.BindingProvider. This " +
			"will cause unpredictable behavior.";

	private SoapMessageLogger soapMessageLogger = new SoapMessageLogger();
	private static final int MINIMUM_RANDOM_NUMBER = 1;
	private static final int MAXIMUM_RANDOM_NUMBER = 999999999;

	private A agent;
	private P port;

	@Value("${wsag.apiKey}")
	private String apiKey;

	@Value("${webServicesClient.logInboundMessages}")
	private Boolean logInboundMessages = Boolean.FALSE;

	@Value("${webServicesClient.logOutboundMessages}")
	private Boolean logOutboundMessages = Boolean.FALSE;

	private static Authentication authentication;

	// Initialize the Authentication object.
	static {
		BaseWebServiceClient.authentication = new Authentication();
		BaseWebServiceClient.authentication.setUSERID("a");
		BaseWebServiceClient.authentication.setCLIENTID("c");
		BaseWebServiceClient.authentication.setPWD("b");
	}

	/**
	 * Subclasses should override this method to return an instance of the agent that will subsequently create
	 * the ports upon which service calls can be made.
	 *
	 * @return An instance of the ServiceAgent.
	 */
	protected abstract A getServiceAgent();

	/**
	 * Subclasses should override this method to return the port that will be used to make service calls.
	 *
	 * @param agent The agent to use to create the port.
	 * @return An instance of the port to use to make service calls.
	 */
	protected abstract P getServicePort(A agent);


	/**
	 * Returns an Authentication object o pass to the service calls.
	 *
	 * @return An Authentication object o pass to the service calls.
	 */
	public Authentication getAuthentication() {

		return BaseWebServiceClient.authentication;
	}

	/**
	 * Subclasses should override this to return the URI to connect to the web service. If it returns null, the
	 * one generated with the service JAR file will be used.
	 *
	 * @return The URI to connect to the web service.
	 */
	protected abstract String getWebServiceUri();

	/** Returns the agent used to create the port. Subclasses should use this method
	 * to get the agent as it manages the agent's lifecycle. Note, when implementing the
	 * getServicePort method it is unnecessary (though will not cause any issues) to call this
	 * function as that method is passed the agent.
	 *
	 * @return The agent used to create the port.
	 */
	protected A getAgent() {

		if (this.agent == null) {
			this.agent = this.getServiceAgent();
			if (this.agent == null) {
				throw new IllegalStateException("agent cannot be null");
			}
		}
		return this.agent;
	}

	/**
	 * Returns the port to call services on. Subclasses should use this method to get the port
	 * rather than the getServicePort method as it handles configuring the port and maintains
	 * the lifecycle of the object.
	 *
	 * @return The port to call services on.
	 */
	public P getPort() {

		if (this.port == null) {
			this.port = this.getServicePort(this.getAgent());
			if (this.port == null) {
				throw new IllegalStateException("port cannot be null");
			}
			this.configureUri();
			this.configureApiKey();
			this.configureLogger();
		}

		return this.port;
	}

	/**
	 * Helper method that returns the port cast to a BindingProvider. It will log a warning if
	 * the port cannot be cast.
	 *
	 * @return The port cast to a BindingProvider if possible and null otherwise.
	 */
	private BindingProvider getPortAsProvider() {
		if (this.port == null) {
			return null;
		}
		// Because of the way that the web service code is generated,
		// the parameter type cannot extend BindingProvider.
		// If this cast fails, then the user of the class is attempting
		// something unpredictable; just log a warning and keep going
		if (this.port instanceof BindingProvider) {
			return (BindingProvider) this.port;
		} else {
			BaseWebServiceClient.logger.warn(this.port.getClass().getName() + BaseWebServiceClient.CANNOT_CAST_MESSAGE);
			return null;
		}
	}

	/**
	 * Helper method that will move this service to point to a different service.
	 */
	private void configureUri() {
		BindingProvider provider = this.getPortAsProvider();
		if (provider != null && this.getWebServiceUri() != null) {
			WebServiceHelper.moveEndpoint(provider, this.getWebServiceUri());
		}
	}

	/**
	 * Helper method that will add the API key to the headers of the SOAP calls this service makes.
	 */
	private void configureApiKey() {
		BindingProvider provider = this.getPortAsProvider();
		if (provider != null && this.apiKey != null) {
			WebServiceHelper.setApiKey(provider, this.apiKey);
		}
	}

	/**
	 * Helper method that adds a SOAP message logger to the handler chain of this service.
	 */
	private void configureLogger() {
		BindingProvider provider = this.getPortAsProvider();
		if (provider != null) {
			WebServiceHelper.addMessageLogger(provider, this.soapMessageLogger);
			this.soapMessageLogger.setLogInbound(this.logInboundMessages);
			this.soapMessageLogger.setLogOutbound(this.logOutboundMessages);
		}
	}

	/**
	 * Returns a random integer between the minimum and maximum allowed (1 - 999,999,999).
	 *
	 * @return a random integer between the minimum and maximum allowed (1 - 999,999,999).
	 */
	protected Integer getWorkId(){
		return ThreadLocalRandom.current().nextInt(MINIMUM_RANDOM_NUMBER, MAXIMUM_RANDOM_NUMBER + 1);
	}

	/**
	 * This method will convert booleans into Y/N string flags.
	 * @param flag the boolean
	 * @return Y for true, N for false
	 */
	public String convertBooleanToString(Boolean flag){
		if(flag == null){
			return null;
		} else if(flag){
			return "Y";
		} else{
			return "N";
		}
	}

	protected String formatErrorMessage(ProviderSOAPFault fault, String format, String genericMessage) {
		if (fault != null && fault.getProviderSOAPErrorMsg() != null) {
			return fault.getProviderSOAPErrorMsg().stream()
					.map(ProviderSOAPErrorMsg::getMessage)
					.collect(Collectors.joining("| "));
		}
		return String.format(format, genericMessage);
	}

	public void setLogInboundMessages(Boolean logInboundMessages) {
		this.logInboundMessages = logInboundMessages;
	}

	public void setLogOutboundMessages(Boolean logOutboundMessages) {
		this.logOutboundMessages = logOutboundMessages;
	}
}
