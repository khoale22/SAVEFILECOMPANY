package com.heb.pm.codeTable;

import com.heb.pm.ApiConstants;
import com.heb.pm.entity.ProductStateWarning;
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
 * REST endpoint for accessing sub-commodity state warning information.
 *
 * @author m314029
 * @since 2.12.0
 */
@RestController
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + ProductStateWarningController.PRODUCT_STATE_WARNING_URL)
public class ProductStateWarningController {

	public static final Logger logger = LoggerFactory.getLogger(ProductStateWarningController.class);

	// urls
	public static final String PRODUCT_STATE_WARNING_URL = "/codeTable/productStateWarning";
	private static final String FIND_ALL = "findAll";

	// logs
	private static final String LOG_COMPLETE_MESSAGE =
			"The ProductStateWarningController method: %s is complete.";
	private static final String FIND_ALL_LOG_MESSAGE =
			"User %s from IP %s requested to find all product state warnings.";

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private ProductStateWarningService productStateWarningService;

	/**
	 * Get all product state warnings.
	 *
	 * @param request The request sent from the front end.
	 * @return The list of all product state warnings.
	 */
	@RequestMapping(method = RequestMethod.GET, value = ProductStateWarningController.FIND_ALL)
	public List<ProductStateWarning> findAll(HttpServletRequest request){

		this.logFindAll(request.getRemoteAddr());
		List<ProductStateWarning> toReturn = this.productStateWarningService.findAll();
		this.logRequestComplete(ProductStateWarningController.FIND_ALL);
		return toReturn;
	}

	/**
	 * Logs a user's request to get all product state warning records.
	 *
	 * @param ip The IP address the user is logged in from.
	 */
	private void logFindAll(String ip) {
		ProductStateWarningController.logger.info(
				String.format(ProductStateWarningController.FIND_ALL_LOG_MESSAGE,
						this.userInfo.getUserId(), ip));
	}

	/**
	 * Logs completion of an http request.
	 *
	 * @param method The method used in the request.
	 */
	private void logRequestComplete(String method) {
		ProductStateWarningController.logger.info(
				String.format(ProductStateWarningController.LOG_COMPLETE_MESSAGE, method));
	}

	/**
	 * Sets the ProductStateWarningService for this object to use. This is used for testing.
	 *
	 * @param productStateWarningService The ProductStateWarningService for this object to use.
	 */
	public void setProductStateWarningService(ProductStateWarningService productStateWarningService) {
		this.productStateWarningService = productStateWarningService;
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
