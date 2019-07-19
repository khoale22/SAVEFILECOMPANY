/*
*  CostServiceClient
*
*  Copyright (c) 2017 HEB
*  All rights reserved.
*
*  This software is the confidential and proprietary information
*  of HEB.
*/
package com.heb.pm.ws;

import com.heb.pm.entity.Cost;
import com.heb.util.controller.UserInfo;
import com.heb.util.ws.BaseWebServiceClient;
import com.heb.xmlns.ei.costdetails.COSTDETAILS;
import com.heb.xmlns.ei.costservice.CostServicePortType;
import com.heb.xmlns.ei.costservice.CostServiceServiceagent;
import com.heb.xmlns.ei.costservice.Fault;
import com.heb.xmlns.ei.getcostdetailsbyscancodeid_reply.GetCostDetailsByScanCodeIDReply;
import com.heb.xmlns.ei.getcostdetailsbyscancodeid_request.GetCostDetailsByScanCodeIDRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Provides access to service endpoint for Cost service.
 * @author l730832
 * @since 2.5.0
 */
@Service
public class CostServiceClient extends BaseWebServiceClient<CostServiceServiceagent, CostServicePortType> {

	private static final Logger logger = LoggerFactory.getLogger(CostServiceClient.class);

	private static final String WAREHOUSE_COST_TYPE_CODE = "00001";
	private static final String DSD_COST_TYPE_CODE = "LIST";

	@Value("${costService.uri}")
	private String uri;

	@Autowired
	private UserInfo userInfo;

	/**
	 * Since this is returning COSTELEMENTDETAILS by scanCodeId, the web service returns all element details. We only
	 * want the one that is attached to the current searched vendor number, upc, and item id.
	 *
	 * @param primaryUpc The primary upc.
	 * @param vendorNumber The vendor number.
	 * @param itemId The item id
	 * @return The cost of the item.
	 * @throws CheckedSoapException
	 */
	public Cost getCostDetail(long primaryUpc, int vendorNumber, long itemId) throws CheckedSoapException {
		GetCostDetailsByScanCodeIDRequest request = new GetCostDetailsByScanCodeIDRequest();
		request.setAuthentication(this.getAuthentication());
		request.setSCANCODEID(BigDecimal.valueOf(primaryUpc));
		request.setVENDORNUMBER(BigDecimal.valueOf(vendorNumber));

		try {
			GetCostDetailsByScanCodeIDReply reply = this.getCostDetailsByScanCodeIDReply(request);
			int index = 0;
			int detailIndex;
			for (COSTDETAILS costdetails : reply.getCOSTDETAILS()) {
				detailIndex = 0;
				if (reply.getCOSTDETAILS().get(index).getCOSTELEMENT().getSPLRNUMBER() != null) {
					if (reply.getCOSTDETAILS().get(index).getCOSTELEMENT().getSPLRNUMBER().longValue() ==
							vendorNumber &&
							reply.getCOSTDETAILS().get(index).getCOSTELEMENT().getITEMCODE().longValue() == itemId) {
						for (COSTDETAILS.COSTELEMENT.COSTELEMENTDETAIL elementDetail :
								reply.getCOSTDETAILS().get(index).getCOSTELEMENT().getCOSTELEMENTDETAIL()) {
							if (WAREHOUSE_COST_TYPE_CODE.equals(elementDetail.getCOSTTYPECD())) {
								COSTDETAILS.COSTELEMENT.COSTELEMENTDETAIL detail =
										reply.getCOSTDETAILS().get(index).getCOSTELEMENT().getCOSTELEMENTDETAIL().get(detailIndex);
								Cost toReturn = new Cost();
								toReturn.setCost(detail.getCOSTAMOUNT().doubleValue());
								return toReturn;
							}
							detailIndex++;
						}
					}
				} else {
					if (reply.getCOSTDETAILS().get(index).getCOSTELEMENT().getSCANCODE().longValue() == itemId) {
						for (COSTDETAILS.COSTELEMENT.COSTELEMENTDETAIL elementDetail : reply.getCOSTDETAILS().get(index).getCOSTELEMENT().getCOSTELEMENTDETAIL()) {
							if (DSD_COST_TYPE_CODE.equals(elementDetail.getCOSTTYPECD().trim())) {
								COSTDETAILS.COSTELEMENT.COSTELEMENTDETAIL detail =
										reply.getCOSTDETAILS().get(index).getCOSTELEMENT().getCOSTELEMENTDETAIL().get(detailIndex);
								Cost toReturn = new Cost();
								toReturn.setCost(detail.getCOSTAMOUNT().doubleValue());
								return toReturn;
							}
							detailIndex++;
						}
					}
				}
				index++;
			}
		} catch (Exception e) {
			CostServiceClient.logger.error(e.getMessage());
			throw new CheckedSoapException(e.getCause());
		}
		return null;
	}

	/**
	 * The reply for the webservice call.
	 * @param request the request to retrieve the costDetails.
	 * @return the reply
	 * @throws CheckedSoapException If the webservice fails.
	 */
	public GetCostDetailsByScanCodeIDReply getCostDetailsByScanCodeIDReply(GetCostDetailsByScanCodeIDRequest request)
			throws CheckedSoapException {
		GetCostDetailsByScanCodeIDReply reply;

		try {
			reply = this.getPort().getCostDetailsByScanCodeID(request);
		} catch (Fault fault) {
			fault.printStackTrace();
			throw new CheckedSoapException(fault.getMessage());
		}
		return reply;
	}

	/**
	 * Return the service agent for this client.
	 *
	 * @return CostServiceServiceAgent associated with this client.
	 */
	@Override
	protected CostServiceServiceagent getServiceAgent() {
		try {
			URL url = new URL(this.getWebServiceUri());
			return new CostServiceServiceagent(url);
		} catch (MalformedURLException e) {
			CostServiceClient.logger.error(e.getMessage());
		}
		return new CostServiceServiceagent();
	}

	/**
	 * Return the port type for this client.
	 *
	 * @param agent The agent to use to create the port.
	 * @return CostServicePortType associated with this client.
	 */
	@Override
	protected CostServicePortType getServicePort(CostServiceServiceagent agent) {
		return agent.getCostService();
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
