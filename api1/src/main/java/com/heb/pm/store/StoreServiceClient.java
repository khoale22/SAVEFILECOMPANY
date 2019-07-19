/*
 * StoreServiceClient
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.store;

import com.heb.util.ws.BaseWebServiceClient;
import com.heb.xmlns.ei.StoreServicePortType1;
import com.heb.xmlns.ei.StoreServiceServiceagent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Grants access to the service endpoint for store service.
 *
 * @author d116773
 * @since 2.0.1
 */
@Service
public class StoreServiceClient
	extends BaseWebServiceClient<StoreServiceServiceagent, StoreServicePortType1> {

	private static final Logger logger = LoggerFactory.getLogger(StoreServiceClient.class);

	@Value("${storeRepository.uri}")
	private String uri;

	/**
	 * Returns the service agent of the web service.
	 *
	 * @return The service agent of the web service.
	 */
	@Override
	protected StoreServiceServiceagent getServiceAgent() {
		try {
			URL url = new URL(this.getWebServiceUri());
			return new StoreServiceServiceagent(url);
		} catch (MalformedURLException e) {
			StoreServiceClient.logger.error(e.getMessage());
		}
		return new StoreServiceServiceagent();
	}

	/**
	 * Returns the port to call functions against.
	 *
	 * @param agent The agent to use to create the port.
	 * @return The port to call functions against.
	 */
	@Override
	protected StoreServicePortType1 getServicePort(StoreServiceServiceagent agent) {
		return agent.getStoreService1();
	}

	/**
	 * Returns the URI to the web service.
	 *
	 * @return The URI to the web service.
	 */
	@Override
	protected String getWebServiceUri() {
		return this.uri;
	}

	/**
	 * Sets the URI for the web service. This is primarily used for testing as Spring will set the value.
	 *
	 * @param uri The URI for the service to use.
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}
}
