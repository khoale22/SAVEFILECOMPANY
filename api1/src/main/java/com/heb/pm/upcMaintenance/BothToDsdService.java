/*
 *  BothToDsdService
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.upcMaintenance;


import com.heb.pm.ws.CheckedSoapException;
import com.heb.pm.ws.ProductManagementServiceClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Holds all business logic related to both to DSD requests.
 *
 * @author s573181
 * @since 2.1.0
 */
@Service
public class BothToDsdService {

	@Autowired
	private UpcSwapUtils upcSwapUtils;

	// initial count for first source upc found or first destination item code make primary found
	private static final int INITIAL_COUNT = 1;

	private static final String STRING_SUCCESS = "Success";

	// error messages
	private static final String ERROR_ONE_SOURCE_UPC_ONLY= "Upc: %d cannot be the source upc more than once.";

	@Autowired
	private ProductManagementServiceClient productManagementServiceClient;

	/**
	 * Returns a list of all UPC swap information from a list of DSD UPCs and matching item codes.
	 *
	 * @return A list of all UPC swaps.
	 */
	public List<UpcSwap> findAll(List<Long> upcList) {
		return upcSwapUtils.findAll(upcList, new ArrayList<Long>(), UpcSwapUtils.BOTH_TO_DSD);
	}

	/**
	 * Submits a list of boths to DSD.
	 *
	 * @param upcSwapList List of boths to DSD to save.
	 * @return The updated list of boths to DSD.
	 */
	public List<UpcSwap> update(List<UpcSwap> upcSwapList) {

		this.validateUpcSwapList(upcSwapList);
		String statusMessage;
		List<UpcSwap> returnList = new ArrayList<>();
		for(UpcSwap upcSwap : upcSwapList) {
			statusMessage = StringUtils.EMPTY;
			if (upcSwap.getSource().getErrorMessage() == null) {
				try {
					this.productManagementServiceClient.submitBothToDsd(upcSwap);
					upcSwap.setStatusMessage(STRING_SUCCESS);
				} catch (CheckedSoapException e){
					upcSwap.setErrorFound(true);
					upcSwap.setStatusMessage(e.getMessage());
					upcSwap.getSource().setErrorMessage(e.getMessage());
				}
			} else {
				upcSwap.setStatusMessage(statusMessage.concat(upcSwap.getSource().getErrorMessage()));
				upcSwap.setErrorFound(true);
			}
		}
		returnList.addAll(upcSwapUtils.getUpdatedAssociatedUpcList(upcSwapList));
		return returnList;
	}

	/**
	 * Validates the list of UPC swaps. If there is the same upc more than once as the source UPC, add an error message.
	 *
	 * @param upcSwapList List of boths to DSD to validate.
	 */
	private void validateUpcSwapList(List<UpcSwap> upcSwapList) {
		Map<Long, Integer> sourceUpcMap = new HashMap<>();
		int count;
		// look for errors
		for(UpcSwap upcSwap: upcSwapList){
			if(sourceUpcMap.containsKey(upcSwap.getSourceUpc())){
				count = sourceUpcMap.get(upcSwap.getSourceUpc());
				sourceUpcMap.put(upcSwap.getSourceUpc(), ++count);
			} else{
				sourceUpcMap.put(upcSwap.getSourceUpc(), INITIAL_COUNT);
			}
		}
		// add error messages to all necessary records
		for(UpcSwap upcSwap: upcSwapList){
			if(	sourceUpcMap.containsKey(upcSwap.getSourceUpc()) && sourceUpcMap.get(upcSwap.getSourceUpc()) > INITIAL_COUNT){
				upcSwap.getSource().setErrorMessage(String.format(ERROR_ONE_SOURCE_UPC_ONLY, upcSwap.getSourceUpc()));
			}
		}
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
	 * Sets the productManagementServiceClient.
	 * @param productManagementServiceClient the productManagementServiceClient.
	 */
	public void setProductManagementServiceClient(ProductManagementServiceClient productManagementServiceClient){
		this.productManagementServiceClient = productManagementServiceClient;
	}
}
