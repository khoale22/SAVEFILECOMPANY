package com.heb.util.ws.scaffolding.fakeSoap;

import javax.xml.ws.Binding;
import javax.xml.ws.handler.Handler;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by d116773 on 3/2/2016.
 */
public class FakeBinding implements Binding {

	List<Handler> handlers;

	@Override
	public List<Handler> getHandlerChain() {
		if (this.handlers == null) {
			this.handlers = new LinkedList<Handler>();
		}
		return this.handlers;
	}

	@Override
	public void setHandlerChain(List<Handler> chain) {
		this.handlers = chain;
	}

	@Override
	public String getBindingID() {
		return null;
	}
}
