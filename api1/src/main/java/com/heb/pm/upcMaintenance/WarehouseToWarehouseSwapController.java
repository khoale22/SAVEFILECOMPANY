package com.heb.pm.upcMaintenance;


import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * REST endpoint for functions related to getting details for, and submitting, warehouse upc swaps.
 *
 * @author m594201
 * @since 2.4.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL +
		WarehouseToWarehouseSwapController.UPC_SWAP_WAREHOUSE_URL)
@AuthorizedResource(ResourceConstants.UPC_SWAP_WAREHOUSE_TO_WAREHOUSE)
public class WarehouseToWarehouseSwapController {

	private static final Logger logger = LoggerFactory.getLogger(WarehouseToWarehouseSwapController.class);

	/**
	 * The constant UPC_SWAP_WAREHOUSE_URL.
	 */
	protected static final String UPC_SWAP_WAREHOUSE_URL = "/upcMaintenance/warehouseToWarehouseSwap";
	protected static final String FETCH_DETAILS = "/fetchDetails";

	// Log Messages
	private static final String SUBMIT_UPC_SWAP =
			"User %s from IP %s has submitted UPC swaps : %s.";
	private static final String GET_UPC_SWAP_DETAILS = "User %s from IP %s has requested details on UPC swap with " +
			"source upc: %d, source item code: %d, destination upc: %d, and destination item code: %d.";

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private WarehouseToWarehouseService service;

	/**
	 * Get warehouse upc swap details.
	 *
	 * @param upcSwapList The list of upc swaps that needs the details filled in.
	 * @param request The Http request sent from front end.
	 * @return the list of Upc Swaps with details filled in
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = WarehouseToWarehouseSwapController.FETCH_DETAILS)
	public List<UpcSwap> getWarehouseSwapUpcSwapDetails(
			@RequestBody List<UpcSwap> upcSwapList,
			HttpServletRequest request){

		this.logGetWarehouseUpcSwapDetails(request.getRemoteAddr(), upcSwapList);

		return this.service.findAllWarehouseSwap(upcSwapList);
	}

	/**
	 * Submit whs to whs swap.
	 *
	 * @param whsUpcSwapList the whs upc swap list
	 * @param request        the request
	 * @return the updated list of upc swaps.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST)
	public List<UpcSwap> submitWarehouseToWarehouseSwap(@RequestBody List<UpcSwap> whsUpcSwapList,
															HttpServletRequest request) {
		this.logSubmitWarehouseToWarehouseSwap(request.getRemoteAddr(), whsUpcSwapList);

		return this.service.submitWarehouseToWarehouseSwap(whsUpcSwapList);
	}

	/**
	 * Logs a user's request for getting warehouse to warehouse upc swap swap details.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param upcSwaps The list of upc swaps to get details for.
	 */
	private void logGetWarehouseUpcSwapDetails(String ip, List<UpcSwap> upcSwaps) {
		for(UpcSwap upcSwap : upcSwaps){
			WarehouseToWarehouseSwapController.logger.info(
					String.format(WarehouseToWarehouseSwapController.GET_UPC_SWAP_DETAILS, this.userInfo.getUserId(),
							ip, upcSwap.getSourceUpc(), upcSwap.getSource().getItemCode(),
							upcSwap.getDestinationPrimaryUpc(), upcSwap.getDestination().getItemCode())
			);
		}

	}

	/**
	 * Logs a user's request for submitting warehouse to warehouse upc swap swap.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param upcSwaps The list of warehouse to warehouse upc swap swap to submit.
	 */
	private void logSubmitWarehouseToWarehouseSwap(String ip, List<UpcSwap> upcSwaps) {
		for (UpcSwap whsToWhsSwap : upcSwaps) {
			WarehouseToWarehouseSwapController.logger.info(
					String.format(WarehouseToWarehouseSwapController.SUBMIT_UPC_SWAP, this.userInfo.getUserId(),
							ip, whsToWhsSwap));
		}
	}
}
