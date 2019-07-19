/*
 * VendorServiceClient
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.vendor;

import com.heb.pm.entity.BicepVendor;
import com.heb.pm.ws.CheckedSoapException;
import com.heb.util.ws.BaseWebServiceClient;
import com.heb.xmlns.ei.VendorServicePortType;
import com.heb.xmlns.ei.VendorServiceServiceagent;
import com.heb.xmlns.ei.bicepslist.BicepsList;
import com.heb.xmlns.ei.get_bicepslist_reply.GetBicepsListReply;
import com.heb.xmlns.ei.get_bicepslist_request.GetBicepsListRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides access to service endpoint for vendor service.
 *
 * @author d116773
 * @since 2.0.2
 */
@Service
public class VendorServiceClient extends BaseWebServiceClient<VendorServiceServiceagent, VendorServicePortType> {

	private static final Logger logger = LoggerFactory.getLogger(VendorServiceClient.class);

	@Value("${vendorService.uri}")
	private String uri;

	@Override
	protected VendorServiceServiceagent getServiceAgent() {

		try {
			URL url = new URL(this.getWebServiceUri());
			return new VendorServiceServiceagent(url);
		} catch (MalformedURLException e) {
			VendorServiceClient.logger.error(e.getMessage());
		}
		return new VendorServiceServiceagent();
	}

	@Override
	protected VendorServicePortType getServicePort(VendorServiceServiceagent agent) {
		return agent.getVendorService();
	}

	@Override
	protected String getWebServiceUri() {
		return this.uri;
	}

	/**
	 * Sets the URI to access vendor service. This is primarily used for testing.
	 *
	 * @param uri The URI to access vendor service.
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}

	/**
	 * Get list of bicep vendor, bicep vendor include information of vendor location number, bicep, ap location
	 * number, warehouse number, facility.
	 * @param apVendor - The ap location vendor number
	 * @param classCode - The class code
	 * @return list of bicep vendor.
	 * @throws CheckedSoapException
	 */
	public List<BicepVendor> getBicepsListByApVendorAndClass(int apVendor, int classCode) throws CheckedSoapException{
		List<BicepVendor> bicepVendors = new ArrayList<>();
		// Create Biceps List request to call getBicepsList method in VendorService.
		GetBicepsListRequest request = new GetBicepsListRequest();
		request.setAuthentication(this.getAuthentication());
		request.getVENDCLSCD().add(String.valueOf(classCode));
		request.getAPNBR().add(String.valueOf(apVendor));
		//Call method, return reply and handle data
		try{
			GetBicepsListReply reply = this.getPort().getBicepsList(request);
			List<BicepsList> bicepsLists = reply.getBicepsList();
			//Convert to list of BicepVendor to sent to font-end.
			List<String> whseNbrLst = new ArrayList<>();
			if(bicepsLists != null && !bicepsLists.isEmpty()) {
				BicepVendor bicepVendor;
				for(BicepsList bicepsList : bicepsLists){
					//Check remove duplicate ware house number return from webservice.
					if(!whseNbrLst.contains(bicepsList.getWHSENBR())) {
						bicepVendor = new BicepVendor();
						bicepVendor.setVendorLocationNumber(bicepsList.getVENDLOCNBR().intValue());
						bicepVendor.setWarehouseNumber(bicepsList.getWHSENBR());
						bicepVendor.setFacility(bicepsList.getFACILITY().intValue());
						bicepVendors.add(bicepVendor);
					}
					whseNbrLst.add(bicepsList.getWHSENBR());
				}
			}
		} catch (com.heb.xmlns.ei.Fault fault) {
			VendorServiceClient.logger.error(fault.getMessage());
			throw new CheckedSoapException("No Biceps available for authorization.");
		}catch (Exception e) {
			VendorServiceClient.logger.error(e.getMessage());
			throw new CheckedSoapException(e.getCause());
		}
		return bicepVendors;
	}
}
