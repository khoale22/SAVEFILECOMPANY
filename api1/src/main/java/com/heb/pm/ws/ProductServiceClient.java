/*
 *  ProductServiceClient
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *  
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.ws;

import com.heb.util.controller.UserInfo;
import com.heb.util.ws.BaseWebServiceClient;
import com.heb.xmlns.ei.productservice.ProductServicePortType;
import com.heb.xmlns.ei.productservice.ProductServiceServiceagent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
/**
 * Provides access to service endpoint for product service.
 *
 * @author l730832
 * @since 2.12.0
 */
@Service
public class ProductServiceClient extends BaseWebServiceClient<ProductServiceServiceagent, ProductServicePortType> {

	private static final Logger logger = LoggerFactory.getLogger(ProductServiceClient.class);

	@Value("${productService.uri")
	private String uri;

	@Autowired
	private UserInfo userInfo;

	/**
	 * Return the service agent for this client.
	 *
	 * @return ProductServiceServiceagent associated with this client.
	 */
	@Override
	protected ProductServiceServiceagent getServiceAgent() {
		try {
			URL url = new URL(this.getWebServiceUri());
			return new ProductServiceServiceagent(url);
		} catch (MalformedURLException e) {
			ProductServiceClient.logger.error(e.getMessage());
		}
		return new ProductServiceServiceagent();
	}
	/**
	 * Return the port type for this client.
	 *
	 * @param agent The agent to use to create the port.
	 * @return CostServicePortType associated with this client.
	 */
	@Override
	protected ProductServicePortType getServicePort(ProductServiceServiceagent agent) {
		return agent.getProductService();
	}

	/**
	 * Return the url for this client.
	 *
	 * @return String url for this client.
	 */
	@Override
	protected String getWebServiceUri() {
		return this.uri;
	}
}
