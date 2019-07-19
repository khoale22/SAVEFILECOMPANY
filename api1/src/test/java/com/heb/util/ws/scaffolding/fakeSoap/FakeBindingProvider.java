package com.heb.util.ws.scaffolding.fakeSoap;

import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.EndpointReference;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by d116773 on 3/1/2016.
 */
public class FakeBindingProvider implements BindingProvider {

	private Map<String, Object> requestContext = new HashMap<String, Object>();
	private FakeBinding binding = new FakeBinding();

	@Override
	public Map<String, Object> getRequestContext() {
		return this.requestContext;
	}

	@Override
	public Map<String, Object> getResponseContext() {
		return null;
	}

	@Override
	public Binding getBinding() {
		return this.binding;
	}

	@Override
	public EndpointReference getEndpointReference() {
		return null;
	}

	@Override
	public <T extends EndpointReference> T getEndpointReference(Class<T> clazz) {
		return null;
	}
}
