package com.heb.util.ws;

import com.heb.util.ws.scaffolding.fakeSoap.FakeBindingProvider;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.MessageContext;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Map;

/**
 * Created by d116773 on 2/15/2016.
 */
public class WebServiceHelperTest {

	private FakeBindingProvider fakeBindingProvider = new FakeBindingProvider();

	@Test
	public void testMoveEndpointMovesEndpoint() {
		String testUri = null;
		try {
			testUri = String.valueOf(SecureRandom.getInstanceStrong().nextInt());
		} catch (NoSuchAlgorithmException e) {
			Assert.fail(e.getMessage());
		}

		WebServiceHelper.moveEndpoint(this.fakeBindingProvider, testUri);

		WebServiceHelperTest.checkEndpoint(this.fakeBindingProvider, testUri);
	}

	@Test
	public void testSetApiKeySetsApiKey() {
		String apiKey = null;
		try {
			apiKey = String.valueOf(SecureRandom.getInstanceStrong().nextInt());
		} catch (NoSuchAlgorithmException e) {
			Assert.fail(e.getMessage());
		}

		WebServiceHelper.setApiKey(this.fakeBindingProvider, apiKey);
		WebServiceHelperTest.checkApiKey(this.fakeBindingProvider, apiKey);
	}

	@Test
	public void checkAddMessageLoggerAddsLogger() {
		SoapMessageLogger logger = new SoapMessageLogger();

		WebServiceHelper.addMessageLogger(this.fakeBindingProvider, logger);
		List<Handler> handlers = this.fakeBindingProvider.getBinding().getHandlerChain();
		if (handlers == null) {
			Assert.fail("handlers are null");
		}
		if (handlers.size() < 1) {
			Assert.fail("no handlers");
		}
		for (Handler handler:handlers) {
			if (handler.equals(logger)) {
				return;
			}
		}
		Assert.fail("did not add logger");
	}

	// these tests are re-used in BaseWebServiceClientTest
	public static void checkEndpoint(BindingProvider provider, String uri) {
		Assert.assertEquals("URI not set", uri,
				provider.getRequestContext().get(BindingProvider.ENDPOINT_ADDRESS_PROPERTY));
	}

	public static void checkApiKey(BindingProvider provider, String apiKey) {
		Object objectHeaders = provider.getRequestContext().get(MessageContext.HTTP_REQUEST_HEADERS);
		if (!(objectHeaders instanceof Map)) {
			Assert.fail("headers are not map<string, object>");
		}

		Map<String, Object> headers = (Map<String, Object>)objectHeaders;
		Object objectKeys = headers.get(WebServiceHelper.API_KEY_HEADER_NAME);
		if (!(objectKeys instanceof List)) {
			Assert.fail("keys are not string[]");
		}
		List<String> keys = (List<String>)objectKeys;
		Assert.assertEquals("keys array wrong size", 1, keys.size());

		Assert.assertEquals("api key not set", apiKey, keys.get(0));

	}
}
