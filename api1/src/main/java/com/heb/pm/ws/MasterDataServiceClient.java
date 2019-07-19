/*
 *  MasterDataServiceClient
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information of HEB.
 */
package com.heb.pm.ws;

import com.heb.util.controller.UserInfo;
import com.heb.util.ws.BaseWebServiceClient;
import com.heb.util.ws.SoapException;
import com.heb.xmlns.ei.activateworkorder_reply.ActivateWorkOrderReply;
import com.heb.xmlns.ei.activateworkorder_request.ActivateWorkOrderRequest;
import com.heb.xmlns.ei.activatetransactionidtrigger_reply.ActivateTransactionIDTriggerReply;
import com.heb.xmlns.ei.activatetransactionidtrigger_request.ActivateTransactionIDTriggerRequest;
import com.heb.xmlns.ei.masterdataservice.Fault;
import com.heb.xmlns.ei.masterdataservice.MasterDataServicePortType;
import com.heb.xmlns.ei.masterdataservice.MasterDataServiceServiceagent;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Interface class for Master Data Service.
 *
 * @author d116773
 * @since 2.13.0
 */
@Service
public class MasterDataServiceClient extends BaseWebServiceClient<MasterDataServiceServiceagent, MasterDataServicePortType>{

	private static final Logger logger = LoggerFactory.getLogger(MasterDataServiceClient.class);

	private static final String ACTIVTION_JOB_ERROR = "Unable to start activation job for tracking ID %d";

	@Value("${masterDataService.uri}")
	private String uri;

	@Autowired
	private UserInfo userInfo;

	private static final String MAINFRAME_PROCESS_EXECUTED = "Mainframe Prcoess Executed";

	/**
	 * Submits a request to start the activation job.
	 *
	 * @param trackingId The tracking ID of the job to run.
	 */
	public void submitActivation(long trackingId) throws CheckedSoapException{

		ActivateTransactionIDTriggerRequest request = new ActivateTransactionIDTriggerRequest();
		request.setAuthentication(this.getAuthentication());
		request.setTransactionID(Long.toString(trackingId));

		try {
			ActivateTransactionIDTriggerReply reply = this.getPort().activateTransactionIDTrigger(request);
			if (!reply.isSuccess()) {
				throw new CheckedSoapException(String.format(MasterDataServiceClient.ACTIVTION_JOB_ERROR, trackingId));
			}
		} catch (Fault fault) {
			MasterDataServiceClient.logger.error(fault.getMessage());
			throw new CheckedSoapException(fault);
		}

	}

	/**
	 * Returns the service agent for this class.
	 *
	 * @return The service agent for this class.
	 */
	@Override
	protected MasterDataServiceServiceagent getServiceAgent() {

		try {
			URL url = new URL(this.getWebServiceUri());
			return new MasterDataServiceServiceagent(url);
		} catch (MalformedURLException e) {
			MasterDataServiceClient.logger.error(e.getMessage());
		}
		return new MasterDataServiceServiceagent();
	}

	/**
	 * Returns the port for this client.
	 *
	 * @param agent The agent to use to create the port.
	 * @return The port for this client.
	 */
	@Override
	protected MasterDataServicePortType getServicePort(MasterDataServiceServiceagent agent) {
		return agent.getMasterDataService();
	}

	/**
	 * Returns the URI for this client.
	 *
	 * @return The URI for this client.
	 */
	@Override
	protected String getWebServiceUri() {
		return this.uri;
	}

	/**
	 * Activate candidate.
	 *
	 * @param psWorkId The candidate work id.
	 * @throws SoapException A checked soap exception.
	 */
	public void activateCandidate(Integer psWorkId) throws SoapException {
		ActivateWorkOrderRequest request = new ActivateWorkOrderRequest();
		request.setPSWORKID(psWorkId);
		request.setAuthentication(this.getAuthentication());

		try {
			ActivateWorkOrderReply reply = this.getPort().activateWorkOrder(request);
			String response = reply.getVROWResponse();
			if (!StringUtils.equalsIgnoreCase(MAINFRAME_PROCESS_EXECUTED, response)) {
				throw new SoapException(response);
			}
		} catch (Fault fault) {
			throw new SoapException(fault.getFaultInfo().getFaultString(), fault.getCause());
		}
	}
}
