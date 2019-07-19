package com.heb.util.ws.scaffolding.fakeSoap;

import javax.xml.bind.JAXBContext;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * This class emulates a SOAPMessageContext.
 *
 * Created by d116773 on 2/16/2016.
 */
public class FakeSoapMessageContext implements SOAPMessageContext{

	private boolean outbound;

	public void setOutbound(boolean outbound) {
		this.outbound = outbound;
	}


	@Override
	public Object get(Object key) {
		return Boolean.valueOf(this.outbound);
	}

	@Override
	public SOAPMessage getMessage() {
		return new FakeSoapMessage();
	}

	// the remaining functions are not implemented

	@Override
	public void setMessage(SOAPMessage message) {

	}

	@Override
	public Object[] getHeaders(QName header, JAXBContext context, boolean allRoles) {
		return new Object[0];
	}

	@Override
	public Set<String> getRoles() {
		return null;
	}

	@Override
	public void setScope(String name, Scope scope) {

	}

	@Override
	public Scope getScope(String name) {
		return null;
	}

	@Override
	public int size() {
		return 0;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}

	@Override
	public boolean containsKey(Object key) {
		return false;
	}

	@Override
	public boolean containsValue(Object value) {
		return false;
	}



	@Override
	public Object put(String key, Object value) {
		return null;
	}

	@Override
	public Object remove(Object key) {
		return null;
	}

	@Override
	public void putAll(Map<? extends String, ?> m) {

	}

	@Override
	public void clear() {

	}

	@Override
	public Set<String> keySet() {
		return null;
	}

	@Override
	public Collection<Object> values() {
		return null;
	}

	@Override
	public Set<Entry<String, Object>> entrySet() {
		return null;
	}
}
