package com.heb.pm.taxCategory;

import com.heb.pm.ApiConstants;
import com.heb.pm.entity.VertexTaxCategory;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

/**
 * REST endpoint relating to Vertex tax category.
 *
 * @author d116773
 * @since 2.13.0
 */
@RestController
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + "/vertex")
public class TaxCategoryController {

	private static final Logger logger = LoggerFactory.getLogger(TaxCategoryController.class);

	private static final String FETCH_ALL_LOG_MESSAGE =
			"User %s from IP %s requested a list of all vertex tax categories";
	private static final String FETCH_ALL_QUALIFYING_LOG_MESSAGE =
			"User %s from IP %s requested a list of all vertex qualifying conditions";

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private TaxCategoryService taxCategoryService;

	/**
	 * Returns a list of all Vertex tax categories.
	 *
	 * @param request The HTTP request that initiated this call.
	 * @return A list of all Vertex tax categories.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public Collection<VertexTaxCategory> fetchAllTaxCategories(HttpServletRequest request) {

		this.logFetchAll(request.getRemoteAddr());

		return this.taxCategoryService.fetchAllTaxCategories();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/qualifyingConditions")
	public Collection<VertexTaxCategory> fetchAllQualifyingConditions(HttpServletRequest request) {

		this.logFetchAllQualifyingConditions(request.getRemoteAddr());

		return this.taxCategoryService.fetchAllQualifyingConditions();
	}

	/**
	 * Logs a user's request for all Vetrex categories.
	 *
	 * @param ipAddress The IP address the user is logged in from.
	 */
	private void logFetchAll(String ipAddress) {
			 TaxCategoryController.logger.info(
					 String.format(TaxCategoryController.FETCH_ALL_LOG_MESSAGE, this.userInfo.getUserId(), ipAddress));
	}

	/**
	 * Logs a user's request for all Vetrex qualifying conditions.
	 *
	 * @param ipAddress The IP address the user is logged in from.
	 */
	private void logFetchAllQualifyingConditions(String ipAddress) {
		TaxCategoryController.logger.info(
				String.format(TaxCategoryController.FETCH_ALL_QUALIFYING_LOG_MESSAGE,
						this.userInfo.getUserId(), ipAddress));
	}
}
