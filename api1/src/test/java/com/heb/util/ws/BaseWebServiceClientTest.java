package com.heb.util.ws;

import org.junit.Assert;
import org.junit.Test;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Created by d116773 on 3/1/2016.
 */
public class BaseWebServiceClientTest {

	private WebServiceClient webServiceClient = new WebServiceClient();

	@Test
	public void testGetAgentReturnsSameAgent() {
		Assert.assertEquals("agent not correct", this.webServiceClient.getAgent(), this.webServiceClient.fakeAgent);
	}

	@Test
	public void testGetAgentReturnsSameAgentTwice() {
		this.webServiceClient.getAgent();
		Assert.assertEquals("new agent generated", this.webServiceClient.getAgent(), this.webServiceClient.fakeAgent);
	}

	@Test
	public void testGetPortReturnsSamePort() {
		Assert.assertEquals("port not correct", this.webServiceClient.getPort(), this.webServiceClient.fakeBindingProvider);
	}

	@Test
	public void testGetPortReturnsSamePortTwice() {
		this.webServiceClient.getPort();
		Assert.assertEquals("new port generated", this.webServiceClient.getPort(), this.webServiceClient.fakeBindingProvider);
	}

	// these get tricky as they are testing side effects of public function
	//@Test
	public void testSetWebServiceUriMovesWebService() {
		String testUri = null;
		try {
			testUri = String.valueOf(SecureRandom.getInstanceStrong().nextInt());
		} catch (NoSuchAlgorithmException e) {
			Assert.fail(e.getMessage());
		}

		this.webServiceClient.getPort();
		//this.webServiceClient.setWebServiceUri(testUri);

		WebServiceHelperTest.checkEndpoint(this.webServiceClient.fakeBindingProvider, testUri);
	}

	//@Test
	public void testSetApiKeySetsApiKey() {
		String apiKey = null;
		try {
			apiKey = String.valueOf(SecureRandom.getInstanceStrong().nextInt());
		} catch (NoSuchAlgorithmException e) {
			Assert.fail(e.getMessage());
		}

		this.webServiceClient.getPort();
		//this.webServiceClient.setApiKey(apiKey);

		WebServiceHelperTest.checkApiKey(this.webServiceClient.fakeBindingProvider, apiKey);
	}
}
