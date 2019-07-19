package com.heb.pm.upcMaintenance;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * REST endpoint for common functions related to all upc swap operations.
 *
 * @author m314029
 * @since 2.7.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL +
		UpcSwapCommonsController.UPC_SWAP_COMMONS)
@AuthorizedResource(ResourceConstants.UPC_SWAP_COMMON)
public class UpcSwapCommonsController {

	private static final Logger logger = LoggerFactory.getLogger(UpcSwapCommonsController.class);

	protected static final String UPC_SWAP_COMMONS = "/upcMaintenance/upcSwapCommons";
	protected static final String GET_EMPTY_SWAP = "/emptySwap";

	// log messages
	private static final String GET_EMPTY_SWAP_LOG_MESSAGE =
			"User %s from IP %s has requested an empty UPC swap object";

	@Autowired
	private UserInfo userInfo;

	/**
	 * Returns an empty UPC Swap object.
	 *
	 * @param request The HTTP Request that initiated this call.
	 * @return An empty UPC Swap object.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = UpcSwapCommonsController.GET_EMPTY_SWAP)
	public UpcSwap getEmptySwap(HttpServletRequest request) {

		this.logGetEmptySwap(request.getRemoteAddr());

		UpcSwap emptyUpcSwap = new UpcSwap();

		UpcSwap.SwappableEndPoint source = emptyUpcSwap.new SwappableEndPoint();
		emptyUpcSwap.setSource(source);

		UpcSwap.SwappableEndPoint destination = emptyUpcSwap.new SwappableEndPoint();
		emptyUpcSwap.setDestination(destination);

		return emptyUpcSwap;
	}

	/**
	 * Logs a user's request for an empty UPC swap object.
	 *
	 * @param ipAddress The IP address the request came from.
	 */
	private void logGetEmptySwap(String ipAddress) {
		UpcSwapCommonsController.logger.info(String.format(UpcSwapCommonsController.GET_EMPTY_SWAP_LOG_MESSAGE,
				this.userInfo.getUserId(), ipAddress));
	}
}
