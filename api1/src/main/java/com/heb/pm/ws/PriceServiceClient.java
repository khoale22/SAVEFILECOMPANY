/*
*  PriceServiceClient
*
*  Copyright (c) 2017 HEB
*  All rights reserved.
*
*  This software is the confidential and proprietary information
*  of HEB.
*/

package com.heb.pm.ws;

import com.heb.pm.entity.PriceDetail;
import com.heb.pm.entity.Retail;
import com.heb.util.ws.BaseWebServiceClient;
import com.heb.util.ws.SoapException;
import com.heb.xmlns.ei.getcurrentregularpricebyupc_reply.GetCurrentRegularPricebyUPCReply;
import com.heb.xmlns.ei.getcurrentregularpricebyupc_request.GetCurrentRegularPricebyUPCRequest;
import com.heb.xmlns.ei.getretailbyupc_reply.GetRetailByUPCReply;
import com.heb.xmlns.ei.getretailbyupc_request.GetRetailByUPCRequest;
import com.heb.xmlns.ei.priceservice.Fault;
import com.heb.xmlns.ei.priceservice.PriceServicePortType;
import com.heb.xmlns.ei.priceservice.PriceServiceServiceagent;
import com.heb.xmlns.ei.retailprices.RETAILBYUPC;
import com.heb.xmlns.ei.retailprices.RETAILPRICES;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Provides access to service endpoint for Price service.
 *
 * @author l730832
 * @since 2.5.0
 */
@Service
public class PriceServiceClient extends BaseWebServiceClient<PriceServiceServiceagent, PriceServicePortType> {

	private static final String ALL_RETAILS = "ALL";
	private static final String REGULAR_RETAIL = "REGULAR_PRICE";
	private static final String NO_RETAIL_FOUND_ERROR_MESSAGE = "Retail not found for UPC %d in zone %d";

	private static final Logger logger = LoggerFactory.getLogger(PriceServiceClient.class);

	@Value("${priceService.uri}")
	private String uri;


	/**
	 * Gets price detail by upc.
	 *
	 * @param upc the upc
	 * @return the price detail by upc
	 * @throws CheckedSoapException the checked soap exception
	 */
	public List<PriceDetail> getPriceDetailByUpc(String upc) throws CheckedSoapException{

		GetRetailByUPCRequest.RetailPricesByUPCRequest retailUPC = new GetRetailByUPCRequest.RetailPricesByUPCRequest();
		GetRetailByUPCRequest.RetailPricesByUPCRequest.UPC queryUpc = new GetRetailByUPCRequest.RetailPricesByUPCRequest.UPC();
		StringBuilder errorMessage = new StringBuilder(StringUtils.EMPTY);

		List<PriceDetail> returnList;

		queryUpc.setValue(new BigDecimal(upc));
		retailUPC.setUPC(queryUpc);

		GetRetailByUPCRequest request = new GetRetailByUPCRequest();
		request.setAuthentication(this.getAuthentication());
		request.setPRICETYPE(ALL_RETAILS);
		request.getRetailPricesByUPCRequest().add(retailUPC);

		try {
			GetRetailByUPCReply reply = this.getRetailByUPC(request);
			if(reply.getRETAILPRICES() == null){
				errorMessage.append(reply.getRETAILPRICES());
			}

			// if there were any error messages, throw exception
			if(!errorMessage.toString().equals(StringUtils.EMPTY)){
				throw new SoapException(errorMessage.toString());
			}

			returnList = this.convertRETAILPRICESToPriceDetails(reply.getRETAILPRICES().get(0).getRETAILBYUPC().get(0).getPRICEDETAILS());
		} catch (Exception e) {
			PriceServiceClient.logger.error(e.getMessage());
			throw new CheckedSoapException(e.getLocalizedMessage(), e.getCause());
		}
		return returnList;
	}
	
	//Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
	/**
	 * This method is used to get the price Detail for the given UPC, StoreNo and effectiveDate
	 * @param upc
	 * @param effectiveDate
	 * @param StoreNo
	 * @return PriceDetail
	 * @throws CheckedSoapException
	 */
	public List<PriceDetail> getPriceDetailByUpc(String upc,Date effectiveDate,int storeNo) throws CheckedSoapException{
		List<PriceDetail>  priceDetailList = null;
		try{
			List<RETAILPRICES> retailPricesList = null;
			GetCurrentRegularPricebyUPCRequest getCurrentRegularPricebyUPCRequest = new GetCurrentRegularPricebyUPCRequest();
			getCurrentRegularPricebyUPCRequest.setSTORE(Integer.valueOf(storeNo));
			getCurrentRegularPricebyUPCRequest.setPRICETYPE("CURR");
			getCurrentRegularPricebyUPCRequest.getUPC().add(upc);
			GregorianCalendar gCalendar = new GregorianCalendar();
	        gCalendar.setTime(effectiveDate);
	        XMLGregorianCalendar xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
	        getCurrentRegularPricebyUPCRequest.setEFFDATE(xmlCalendar);
	        getCurrentRegularPricebyUPCRequest.setAuthentication(this.getAuthentication());
        	GetCurrentRegularPricebyUPCReply getCurrentRegularPricebyUPCReply = this.getPort().getCurrentRegularPricebyUPC(getCurrentRegularPricebyUPCRequest);
        	if(getCurrentRegularPricebyUPCReply != null ){
        		retailPricesList =  getCurrentRegularPricebyUPCReply.getRETAILPRICES();
        		if(retailPricesList != null){
        			for(RETAILPRICES priceDetail : retailPricesList){
        				priceDetailList  = convertRETAILPRICESToPriceDetail(priceDetail.getRETAILBYUPC());
        			}
        		}
        	}
		} catch (Exception e) {
		    e.printStackTrace();
			PriceServiceClient.logger.error(e.getMessage());
		}
        return priceDetailList;
	}
	
	/**
	 * Method iterates through results from PriceService
	 *
	 * @param retailprices service type object
	 * @return List of PriceDetail
	 */
	private List<PriceDetail> convertRETAILPRICESToPriceDetail(List<RETAILBYUPC> retailprices){
		List<PriceDetail> toReturn = new ArrayList<>();
		for(RETAILBYUPC retailPrice : retailprices){
			toReturn.addAll(this.convertRETAILPRICESToPriceDetails(retailPrice.getPRICEDETAILS()));
		}

		return toReturn;
	}
	
	//End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani

	

	/**
	 * Method iterates through results from PriceService
	 *
	 * @param retailprices service type object
	 * @return List of PriceDetail
	 */
	private List<PriceDetail> convertRETAILPRICESToPriceDetails(List<RETAILBYUPC.PRICEDETAILS> retailprices){
		List<PriceDetail> toReturn = new ArrayList<>();
		for(RETAILBYUPC.PRICEDETAILS retailPrice : retailprices){
			toReturn.addAll(this.convertRETAILSBYUPCToPriceDetails(retailPrice));
		}

		return toReturn;
	}

	/**
	 * Method converts price data from a service type object into PriceDetail.
	 *
	 * @param retailbyupcs service type object
	 * @return PriceDetail object
	 */
	private List<PriceDetail> convertRETAILSBYUPCToPriceDetails(RETAILBYUPC.PRICEDETAILS retailbyupcs){

		List<PriceDetail> priceDetailList = new ArrayList<>();
		PriceDetail priceDetail = new PriceDetail();

		if(retailbyupcs.getXFORQTY() != null) {
			priceDetail.setxFor(retailbyupcs.getXFORQTY().intValue());
		}

		if(retailbyupcs.getZONENO() != null) {
			priceDetail.setZone(retailbyupcs.getZONENO().intValue());
		}

		if(retailbyupcs.getRETAILPRC() != null) {
			priceDetail.setRetailPrice(retailbyupcs.getRETAILPRC().doubleValue());
		}

		if(retailbyupcs.getWEIGHTSW() != null) {
		   //Start - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
			priceDetail.setWeight(retailbyupcs.getWEIGHTSW().trim().equals("Y") ? true : false);
			//End - Changes are done for Scale Load by Department by Joseph Williams, Swaroopa Rani
		}

		priceDetailList.add(priceDetail);

		return priceDetailList;
	}

	/**
	 * Gets retail by upc.
	 *
	 * @param request the request
	 * @return the retail by upc
	 * @throws CheckedSoapException the checked soap exception
	 */
	public GetRetailByUPCReply getRetailByUPC(GetRetailByUPCRequest request) throws CheckedSoapException {
		GetRetailByUPCReply reply;
		try{
			reply = this.getPort().getRetailByUPC(request);
		} catch (Exception e) {
			PriceServiceClient.logger.error(e.getMessage());
			throw new CheckedSoapException(e.getMessage());
		}

		return reply;
	}

	/**
	 * Returns the EDLP retail for a given UPC for a given retail zone.
	 *
	 * @param retailZone The retail Zone to look for retails for.
	 * @param upc        The price details being pulled specified by this upc.
	 * @return The current EDLP retail for the UPC for that zone.
	 * @throws CheckedSoapException If the webservice fails.
	 */
	public Retail getRegularRetail(int retailZone, long upc) throws CheckedSoapException {

		// First Layer of request
		GetRetailByUPCRequest getRetailByUPCRequest = new GetRetailByUPCRequest();

		// Setting the authentication of the first layer.
		getRetailByUPCRequest.setAuthentication(this.getAuthentication());

		// Looks like there's a bug in price service and you can only ask for all retails
		getRetailByUPCRequest.setPRICETYPE(PriceServiceClient.ALL_RETAILS);

		// Second layer of request
		GetRetailByUPCRequest.RetailPricesByUPCRequest retailPricesByUPCRequest =
				new GetRetailByUPCRequest.RetailPricesByUPCRequest();
		GetRetailByUPCRequest.RetailPricesByUPCRequest.UPC upcValue =
				new GetRetailByUPCRequest.RetailPricesByUPCRequest.UPC();
		GetRetailByUPCRequest.RetailPricesByUPCRequest.ZONE zone =
				new GetRetailByUPCRequest.RetailPricesByUPCRequest.ZONE();

		// Setting the upc and zone of second layer
		upcValue.setValue(BigDecimal.valueOf(upc));
		retailPricesByUPCRequest.setUPC(upcValue);

		zone.setValue(BigInteger.valueOf(retailZone));
		retailPricesByUPCRequest.setZONE(zone);

		// Adding the retailpricesbyupcRequest to the getRetailByUPCRequest.
		getRetailByUPCRequest.getRetailPricesByUPCRequest().add(retailPricesByUPCRequest);

		try {
			GetRetailByUPCReply reply = this.getRetailByUPCReply(getRetailByUPCRequest);

			// The reply has nested lists that will not be populated. For most of them, we can just grab the first result.
			// We have to loop through the price details to get out the regular price.
			for (RETAILBYUPC.PRICEDETAILS priceDetails :
					reply.getRETAILPRICES().get(0).getRETAILBYUPC().get(0).getPRICEDETAILS()) {
				if (PriceServiceClient.REGULAR_RETAIL.equals(priceDetails.getPRICETYP())) {
					Retail retail = new Retail();
					retail.setRetail(priceDetails.getRETAILPRC().floatValue());
					retail.setxFor(priceDetails.getXFORQTY().intValue());
					return retail;
				}
			}
		} catch (Exception e) {
			PriceServiceClient.logger.error(e.getMessage());
			throw new CheckedSoapException(e.getCause());
		}

		PriceServiceClient.logger.error(String.format(PriceServiceClient.NO_RETAIL_FOUND_ERROR_MESSAGE,
				retailZone, upc));
		return null;
	}

	/**
	 * The reply for the webservice call.
	 *
	 * @param request the request to retrieve the pricedetails.
	 * @return the reply
	 * @throws CheckedSoapException If the webservice fails.
	 */
	public GetRetailByUPCReply getRetailByUPCReply(GetRetailByUPCRequest request) throws CheckedSoapException {
		GetRetailByUPCReply reply;

		try {
			reply = this.getPort().getRetailByUPC(request);
		} catch (Fault fault) {
			PriceServiceClient.logger.error(fault.getMessage());
			throw new CheckedSoapException(fault.getMessage());
		}
		return reply;
	}

	/**
	 * Return the service agent for this client.
	 *
	 * @return PriceServiceServiceAgent associated with this client.
	 */
	@Override
	protected PriceServiceServiceagent getServiceAgent() {
		try {
			URL url = new URL(this.getWebServiceUri());
			return new PriceServiceServiceagent(url);
		} catch (MalformedURLException e) {
			PriceServiceClient.logger.error(e.getMessage());
		}
		return new PriceServiceServiceagent();
	}

	/**
	 * Return the port type for this client.
	 *
	 * @param agent The agent to use to create the port.
	 * @return PriceServicePortType associated with this client.
	 */
	@Override
	protected PriceServicePortType getServicePort(PriceServiceServiceagent agent) {
		return agent.getPriceService();
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
