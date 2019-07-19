/*
*  PlanogramServiceClient
*
*  Copyright (c) 2017 HEB
*  All rights reserved.
*
*  This software is the confidential and proprietary information
*  of HEB.
*/

package com.heb.pm.ws;

import com.heb.util.controller.UserInfo;
import com.heb.util.ws.BaseWebServiceClient;
import com.heb.xmlns.ei.get_nextweekpog_reply.GetNextWeekPogReply;
import com.heb.xmlns.ei.get_nextweekpog_request.GetNextWeekPogRequest;
import com.heb.xmlns.ei.planogramservice.Fault;
import com.heb.xmlns.ei.planogramservice.PlanogramServicePortType;
import com.heb.xmlns.ei.planogramservice.PlanogramServiceServiceagent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides access to service endpoint for Planogram service.
 * @author s573181
 * @since 2.5.0
 */
@Service
public class PlanogramServiceClient extends BaseWebServiceClient
		<PlanogramServiceServiceagent, PlanogramServicePortType> {

	private static final Logger logger = LoggerFactory.getLogger(PlanogramServiceClient.class);

	@Value("${planogramService.uri}")
	private String uri;

	@Autowired
	private UserInfo userInfo;

	/**
	 * Returns whether or not a upc is on a planogram.
	 *
	 * @param upc the upc.
	 * @return Whether or not a upc is on a planogram.
	 * @throws CheckedSoapException CheckedSoapException.
	 */
	public boolean isOnPlanogramByUpc(long upc) throws CheckedSoapException {
		GetNextWeekPogRequest request = new GetNextWeekPogRequest();
		request.setAuthentication(this.getAuthentication());
		request.getSCNCDID().add(upc);
		GetNextWeekPogReply reply = this.getNextWeekPlanogram(request);
		return reply != null && reply.getNextWeekPog() != null && reply.getNextWeekPog().size() > 0
				&& reply.getNextWeekPog().get(0).getStoreCount() > 0;
	}

	/**
	 * Return GetNextWeekPogReply by upc.
	 * @param request GetNextWeekPogRequest.
	 * @return GetNextWeekPogReply by upc.
	 * @throws CheckedSoapException a CheckedSoapException.
	 */
	public GetNextWeekPogReply getNextWeekPlanogram(GetNextWeekPogRequest request) throws CheckedSoapException {
		GetNextWeekPogReply reply;
		try {
			reply = this.getPort().getNextWeekPog(request);
		} catch (Exception e) {
			PlanogramServiceClient.logger.error(e.getMessage());
			throw new CheckedSoapException(e.getMessage());
		}
		return reply;
	}


	/**
	 * Return the port type for this client.
	 *
	 * @param agent The agent to use to create the port.
	 * @return PlanogramServicePortType associated with this client.
	 */
	@Override
	protected PlanogramServicePortType getServicePort(PlanogramServiceServiceagent agent) {
		return agent.getPlanogramService();
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

	/**
	 * Return the service agent for this client.
	 *
	 * @return ProductManagementServiceServiceagent associated with this client.
	 */
	@Override
	protected PlanogramServiceServiceagent getServiceAgent() {
		try {
			URL url = new URL(this.getWebServiceUri());
			return new PlanogramServiceServiceagent(url);
		} catch (MalformedURLException e) {
			PlanogramServiceClient.logger.error(e.getMessage());
		}
		return new PlanogramServiceServiceagent();
	}
}

