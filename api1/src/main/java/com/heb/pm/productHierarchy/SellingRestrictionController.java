package com.heb.pm.productHierarchy;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.SellingRestriction;
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
 * REST controller for all information related to product hierarchy selling restrictions.
 *
 * @author m314029
 * @since 2.8.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + SellingRestrictionController.PRODUCT_HIERARCHY_SELLING_RESTRICTION_URL)
@AuthorizedResource(ResourceConstants.PRODUCT_HIERARCHY_SELLING_RESTRICTIONS)
public class SellingRestrictionController {

	private static final Logger logger = LoggerFactory.getLogger(SellingRestrictionController.class);

	static final String PRODUCT_HIERARCHY_SELLING_RESTRICTION_URL = "/productHierarchy/sellingRestriction";

	// methods
	private static final String FIND_ALL = "findAll";

	// log messages
	private static final String LOG_FIND_ALL =
			"User %s from IP %s has requested all selling restrictions.";
	private static final String LOG_COMPLETE_MESSAGE =
			"The method: %s is complete.";

	@Autowired
	private SellingRestrictionService service;

	@Autowired
	private	UserInfo userInfo;

	/**
	 * Get all selling restrictions.
	 *
	 * @param request The request sent from the front end.
	 * @return The list of all selling restrictions.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = SellingRestrictionController.FIND_ALL)
	public List<SellingRestriction> findAll(HttpServletRequest request){

		this.logFindAll(request.getRemoteAddr());
		List<SellingRestriction> toReturn = this.service.findAll();
		this.logRequestComplete(SellingRestrictionController.FIND_ALL);
		return toReturn;
	}

	/**
	 * Logs a user's request to get all selling restriction records.
	 *
	 * @param ip The IP address the user is logged in from.
	 */
	private void logFindAll(String ip) {
		SellingRestrictionController.logger.info(
				String.format(SellingRestrictionController.LOG_FIND_ALL,
						this.userInfo.getUserId(), ip));
	}

	/**
	 * Logs completion of an http request.
	 *
	 * @param method The method used in the request.
	 */
	private void logRequestComplete(String method) {
		SellingRestrictionController.logger.info(
				String.format(SellingRestrictionController.LOG_COMPLETE_MESSAGE, method));
	}
}
