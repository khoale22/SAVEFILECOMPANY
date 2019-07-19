package com.heb.pm.productHierarchy;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.SellingRestrictionCode;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * REST controller that returns all information related to product hierarchy shipping restrictions.
 *
 * @author m314029
 * @since 2.8.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + ShippingRestrictionController.PRODUCT_HIERARCHY_SHIPPING_RESTRICTION_URL)
@AuthorizedResource(ResourceConstants.PRODUCT_HIERARCHY_SHIPPING_RESTRICTIONS)
public class ShippingRestrictionController {

	private static final Logger logger = LoggerFactory.getLogger(ShippingRestrictionController.class);

	static final String PRODUCT_HIERARCHY_SHIPPING_RESTRICTION_URL = "/productHierarchy/shippingRestriction";

	// log messages
	private static final String GET_ALL_SHIPPING_RESTRICTIONS_MESSAGE =
			"User %s from IP %s has requested all shipping restrictions.";
	private static final String LOG_COMPLETE_MESSAGE =
			"The method: %s is complete.";

	@Autowired
	private SellingRestrictionCodeService service;

	@Autowired
	private UserInfo userInfo;

	/**
	 * Get all selling restrictions.
	 *
	 * @param request The request sent from the front end.
	 * @return The list of all selling restrictions.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "findAll")
	public List<SellingRestrictionCode> findAll(HttpServletRequest request){

		this.logFindAll(request.getRemoteAddr());
		List<SellingRestrictionCode> toReturn = this.service.findAllShippingRestrictions();
		this.logRequestComplete(request.getMethod());
		return toReturn;
	}

	/**
	 * Logs a user's request to get all selling restriction records.
	 *
	 * @param ip The IP address the user is logged in from.
	 */
	private void logFindAll(String ip) {
		ShippingRestrictionController.logger.info(
				String.format(ShippingRestrictionController.GET_ALL_SHIPPING_RESTRICTIONS_MESSAGE,
						this.userInfo.getUserId(), ip));
	}

	/**
	 * Logs completion of an http request.
	 *
	 * @param method The method used in the request.
	 */
	private void logRequestComplete(String method) {
		ShippingRestrictionController.logger.info(
				String.format(ShippingRestrictionController.LOG_COMPLETE_MESSAGE, method));
	}
}
