/*
 * VendorReposiotry
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.repository;

import com.heb.pm.entity.Vendor;
import com.heb.pm.vendor.VendorServiceClient;
import com.heb.util.ws.ObjectConverter;
import com.heb.util.ws.SoapException;
import com.heb.xmlns.ei.Fault;
import com.heb.xmlns.ei.activeapvendorlist.ActiveApVendorList;
import com.heb.xmlns.ei.get_activeapvendorlistbyaptype_reply.GetActiveApVendorListByApTypeReply;
import com.heb.xmlns.ei.get_activeapvendorlistbyaptype_request.GetActiveApVendorListByApTypeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.Set;
import java.util.HashSet;

/**
 * Repository to retrieve information about vendors.
 *
 * @author d116773
 * @since 2.0.2
 */
@Repository
public class VendorRepository {

	@Autowired
	private VendorServiceClient vendorServiceClient;

	private ObjectConverter<ActiveApVendorList, Vendor>  objectConverter = new ObjectConverter<>(Vendor::new);
	/** The Constant AP type code. */
	private static final String AP_TYPE_CODE = "AP";
	/** The Constant DS type code. */
	private static final String DS_TYPE_CODE = "DS";

	/**
	 * Calls the vendor service to retrieve a list of all active vendors.
	 *
	 * @return A list of all active vendors.
	 */
	public Set<Vendor> findAll() {
		GetActiveApVendorListByApTypeRequest request = new GetActiveApVendorListByApTypeRequest();
		request.setAuthentication(this.vendorServiceClient.getAuthentication());
		try {
			//Get vendor with DS type code
			request.setAPTYPCD(DS_TYPE_CODE);
			GetActiveApVendorListByApTypeReply replyDS = this.vendorServiceClient.getPort().getActiveApVendorListByApType(request);
			//Get vendor with AP type code
			request.setAPTYPCD(AP_TYPE_CODE);
			GetActiveApVendorListByApTypeReply replyAP = this.vendorServiceClient.getPort().getActiveApVendorListByApType(request);
			Set<Vendor> vendors = new HashSet<>();
			if(replyDS != null && replyDS.getActiveApVendorList() != null) {
				replyDS.getActiveApVendorList().forEach((v) -> vendors.add(this.objectConverter.convert(v)));
			}
			if(replyAP != null && replyAP.getActiveApVendorList() != null) {
				replyAP.getActiveApVendorList().forEach((v) -> vendors.add(this.objectConverter.convert(v)));
			}
			return vendors;
		} catch (Fault fault) {
			throw new SoapException(fault.getMessage(), fault.getCause());
		}
	}

	/**
	 * Sets the object that makes the actual calls to the vendor service. This method is primarily used for testing.
	 *
	 * @param vendorServiceClient The object that makes the actual calls to the vendor service.
	 */
	public void setVendorServiceClient(VendorServiceClient vendorServiceClient) {
		this.vendorServiceClient = vendorServiceClient;
	}
}
