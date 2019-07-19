package com.heb.util.ws;

import com.heb.util.ws.scaffolding.fakeSoap.FakeAgent;
import com.heb.util.ws.scaffolding.fakeSoap.FakeBindingProvider;
import com.heb.xmlns.ei.authentication.Authentication;

/**
 * Since BaseWebServiceClient is abstract, this is a concrete implementation
 * using the fake SOAP implementation.
 *
 * Created by d116773 on 3/1/2016.
 */
public class WebServiceClient extends BaseWebServiceClient<FakeAgent, FakeBindingProvider> {

	public FakeAgent fakeAgent;
	public FakeBindingProvider fakeBindingProvider;

	public WebServiceClient() {
		this.fakeAgent = new FakeAgent();
		this.fakeBindingProvider = new FakeBindingProvider();

	}
	@Override
	protected FakeAgent getServiceAgent() {
		return this.fakeAgent;
	}

	@Override
	protected FakeBindingProvider getServicePort(FakeAgent agent) {
		return this.fakeBindingProvider;
	}

	@Override
	protected String getWebServiceUri() {
		return null;
	}
}
