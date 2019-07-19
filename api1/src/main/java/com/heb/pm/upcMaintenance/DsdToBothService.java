/*
 *  DsdToBothService
 *
 *   Copyright (c) 2016 HEB
 *   All rights reserved.
 *
 *   This software is the confidential and proprietary information
 *    of HEB.
 */

package com.heb.pm.upcMaintenance;

import com.heb.pm.ws.CheckedSoapException;
import com.heb.pm.ws.ProductManagementServiceClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds all business logic related to DSD to both requests.
 *
 * @author s573181
 * @since 2.1.0
 */
@Service
public class DsdToBothService {

	@Autowired
	private UpcSwapUtils upcSwapUtils;

	private static final String STRING_SUCCESS = "Success";

	@Autowired
	private ProductManagementServiceClient productManagementServiceClient;

	/**
	 * Returns a list of all UPC swap information from a list of DSD UPCs and matching item codes.
	 *
	 * @return A list of all UPC swaps.
	 */
	public List<UpcSwap> findAll(List<Long> upcList, List<Long> itemCodeList) {
		return upcSwapUtils.findAll(upcList, itemCodeList, UpcSwapUtils.DSD_TO_BOTH);
	}

	/**
	 * Submits a list of DSDs to Both.
	 *
	 * @param upcSwapList List of DSDs to both to save.
	 * @return The updated list of DSDs to both.
	 */
	public List<UpcSwap> update(List<UpcSwap> upcSwapList) {

		upcSwapUtils.validateUpcSwapList(upcSwapList);
		String statusMessage;
		List<UpcSwap> returnList = new ArrayList<>();
		for(UpcSwap upcSwap : upcSwapList) {
			statusMessage = StringUtils.EMPTY;
			if (upcSwap.getSource().getErrorMessage() == null && upcSwap.getDestination().getErrorMessage() == null) {
				try {
					this.productManagementServiceClient.submitDsdToBoth(upcSwap);
					upcSwap.setStatusMessage(STRING_SUCCESS);
				} catch(CheckedSoapException e){
					upcSwap.setErrorFound(true);
					upcSwap.setStatusMessage(e.getMessage());
					upcSwap.getDestination().setErrorMessage(e.getMessage());
				}
			} else {
				if (upcSwap.getSource().getErrorMessage() != null) {
					statusMessage = statusMessage.concat(upcSwap.getSource().getErrorMessage());
					upcSwap.setErrorFound(true);
				}
				if (upcSwap.getDestination().getErrorMessage() != null) {
					statusMessage = statusMessage.concat(upcSwap.getDestination().getErrorMessage());
					upcSwap.setErrorFound(true);
				}
				upcSwap.setStatusMessage(statusMessage);
			}
		}
		returnList.addAll(upcSwapUtils.getUpdatedAssociatedUpcList(upcSwapList));
		return returnList;
	}

	/**
	 * These functions are to be used for support in testing.
	 */
	/**
	 * Sets the UpcSwapUtils.
	 * @param utils the UpcSwapUtils.
	 */
	public void setUpcSwapUtils(UpcSwapUtils utils){
		this.upcSwapUtils = utils;
	}

	/**
	 * Sets the ProductManagementServiceClient.
	 * @param productManagementServiceClient the ProductManagementServiceClient.
	 */
	public void setProductManagementServiceClient(ProductManagementServiceClient productManagementServiceClient){
		this.productManagementServiceClient = productManagementServiceClient;
	}
}
