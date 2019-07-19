/*
 *  supportFunctions.js
 *
 *  Copyright (c) 2017 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */


/**
 * Returns a formatted string of all the extra information on a UPC Swap endpoint that can be shown in a popup.
 *
 * @param upcSwapEndpoint the UpcSwap.SwappableEndPoint that contains the extra information to show.
 * @returns {string} The extra information in the endpoint formatted for easy display in a popup.
 */

function formatSwapInformation(upcSwapEndpoint) {

	if (upcSwapEndpoint == null) {
		return null;
	}

	var pog =(upcSwapEndpoint.onLiveOrPendingPog === null ? "?" : (upcSwapEndpoint.onLiveOrPendingPog ? "Y" : "N"));

	return "Size: " + upcSwapEndpoint.itemSize + "\n" +
		"BOH: " + upcSwapEndpoint.balanceOnHand + "\n" +
		"BOO: " + upcSwapEndpoint.balanceOnOrder + "\n" +
		"Retail Link: " + upcSwapEndpoint.productRetailLink + "\n" +
		"PO: " + upcSwapEndpoint.purchaseOrderDisplayText  + "\n" +
		"POG: " + pog;
}

