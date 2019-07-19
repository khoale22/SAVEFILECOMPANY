package com.heb.pm.codeTable;

import com.heb.pm.ApiConstants;
import com.heb.pm.entity.RetailUnitOfMeasure;
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
 * REST endpoint for accessing retail units of measure information.
 *
 * @author m314029
 * @since 2.12.0
 */
@RestController
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + RetailUnitOfMeasureController.RETAIL_UNIT_OF_MEASURE_URL)
public class RetailUnitOfMeasureController {

	public static final Logger logger = LoggerFactory.getLogger(RetailUnitOfMeasureController.class);

	// urls
	public static final String RETAIL_UNIT_OF_MEASURE_URL = "/codeTable/retailUnitOfMeasure";
	private static final String FIND_ALL = "findAll";

	// logs
	private static final String LOG_COMPLETE_MESSAGE =
			"The RetailUnitOfMeasureController method: %s is complete.";
	private static final String FIND_ALL_LOG_MESSAGE =
			"User %s from IP %s requested to find all retail units of measure.";

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private RetailUnitOfMeasureService retailUnitOfMeasureService;

	/**
	 * Get all retail units of measure.
	 *
	 * @param request The request sent from the front end.
	 * @return The list of all retail units of measure.
	 */
	@RequestMapping(method = RequestMethod.GET, value = RetailUnitOfMeasureController.FIND_ALL)
	public List<RetailUnitOfMeasure> findAll(HttpServletRequest request){

		this.logFindAll(request.getRemoteAddr());
		List<RetailUnitOfMeasure> toReturn = this.retailUnitOfMeasureService.findAll();
		this.logRequestComplete(RetailUnitOfMeasureController.FIND_ALL);
		return toReturn;
	}

	/**
	 * Logs a user's request to get all retail units of measure records.
	 *
	 * @param ip The IP address the user is logged in from.
	 */
	private void logFindAll(String ip) {
		RetailUnitOfMeasureController.logger.info(
				String.format(RetailUnitOfMeasureController.FIND_ALL_LOG_MESSAGE,
						this.userInfo.getUserId(), ip));
	}

	/**
	 * Logs completion of an http request.
	 *
	 * @param method The method used in the request.
	 */
	private void logRequestComplete(String method) {
		RetailUnitOfMeasureController.logger.info(
				String.format(RetailUnitOfMeasureController.LOG_COMPLETE_MESSAGE, method));
	}

	/**
	 * Sets the RetailUnitOfMeasureService for this object to use. This is used for testing.
	 *
	 * @param retailUnitOfMeasureService The RetailUnitOfMeasureService for this object to use.
	 */
	public void setRetailUnitOfMeasureService(RetailUnitOfMeasureService retailUnitOfMeasureService) {
		this.retailUnitOfMeasureService = retailUnitOfMeasureService;
	}

	/**
	 * Sets the UserInfo for this object to use. This is used for testing.
	 *
	 * @param userInfo The UserInfo for this object to use.
	 */
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
}
