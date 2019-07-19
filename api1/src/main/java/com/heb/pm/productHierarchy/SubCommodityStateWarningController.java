package com.heb.pm.productHierarchy;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.SubCommodityStateWarning;
import com.heb.pm.entity.SubCommodityStateWarningKey;
import com.heb.util.controller.NonEmptyParameterValidator;
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
 * REST endpoint for accessing sub-commodity state warning information.
 *
 * @author m314029
 * @since 2.12.0
 */
@RestController
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + SubCommodityStateWarningController.SUB_COMMODITY_STATE_WARNING_URL)
@AuthorizedResource(ResourceConstants.PRODUCT_HIERARCHY_SUB_COMMODITY_STATE_WARNINGS)
public class SubCommodityStateWarningController {

	public static final Logger logger = LoggerFactory.getLogger(SubCommodityStateWarningController.class);

	// urls
	public static final String SUB_COMMODITY_STATE_WARNING_URL = "/productHierarchy/subCommodity/stateWarning";
	private static final String GET_EMPTY = "getEmpty";
	private static final String UPDATE = "update";

	// logs
	private static final String LOG_COMPLETE_MESSAGE =
			"The SubCommodityStateWarningController method: %s is complete.";
	private static final String UPDATE_LOG_MESSAGE =
			"User %s from IP %s requested to update sub-commodity state warnings: %s.";
	private static final String GET_EMPTY_LOG_MESSAGE =
			"User %s from IP %s requested an empty sub-commodity state warning.";

	// error messages
	private static final String NO_STATE_WARNINGS_UPDATE_ERROR_MESSAGE = "Must have at least one state warning to update.";

	// properties file keys
	private static final String NO_STATE_WARNINGS_UPDATE_ERROR_KEY = "SubCommodityStateWarningController.missingStateWarnings";

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private SubCommodityStateWarningService subCommodityStateWarningService;

	@Autowired
	private NonEmptyParameterValidator parameterValidator;

	/**
	 * Endpoint to update subCommodityStateWarnings.
	 *
	 * @param subCommodityStateWarnings The SubCommodityStateWarnings to update.
	 * @param request The HTTP request that initiated this call.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value=SubCommodityStateWarningController.UPDATE)
	public List<SubCommodityStateWarning> update(
			@RequestBody List<SubCommodityStateWarning> subCommodityStateWarnings, HttpServletRequest request){
		//list is required
		this.parameterValidator.validate(subCommodityStateWarnings,
				SubCommodityStateWarningController.NO_STATE_WARNINGS_UPDATE_ERROR_MESSAGE,
				SubCommodityStateWarningController.NO_STATE_WARNINGS_UPDATE_ERROR_KEY, request.getLocale());
		this.logUpdate(request.getRemoteAddr(), subCommodityStateWarnings);
		this.subCommodityStateWarningService.update(subCommodityStateWarnings);
		List <SubCommodityStateWarning> toReturn =
				this.subCommodityStateWarningService.findBySubCommodity(subCommodityStateWarnings.get(0));
		this.logRequestComplete(SubCommodityStateWarningController.UPDATE);
		return toReturn;
	}

	/**
	 * Endpoint to retrieve an empty SubCommodityStateWarning used for editing.
	 *
	 * @param request The HTTP request that initiated this call.
	 * @return An empty SubCommodityStateWarning.
	 */
	@RequestMapping(method = RequestMethod.GET, value=SubCommodityStateWarningController.GET_EMPTY)
	public SubCommodityStateWarning getEmpty(HttpServletRequest request){
		this.logGetEmpty(request.getRemoteAddr());
		SubCommodityStateWarning toReturn = new SubCommodityStateWarning();
		toReturn.setKey(new SubCommodityStateWarningKey());
		return toReturn;
	}

	/**
	 * Logs a get empty for sub-commodity state warning.
	 *
	 * @param ip The IP address of the user.
	 */
	private void logGetEmpty(String ip) {
		SubCommodityStateWarningController.logger.info(
				String.format(SubCommodityStateWarningController.GET_EMPTY_LOG_MESSAGE,
						this.userInfo.getUserId(), ip)
		);
	}

	/**
	 * Logs an update for subCommodityStateWarnings.
	 *
	 * @param ip The IP address of the user.
	 */
	private void logUpdate(String ip, List<SubCommodityStateWarning> subCommodityStateWarnings) {
		SubCommodityStateWarningController.logger.info(
				String.format(SubCommodityStateWarningController.UPDATE_LOG_MESSAGE,
						this.userInfo.getUserId(), ip, subCommodityStateWarnings)
		);
	}

	/**
	 * Logs completion of an http request.
	 *
	 * @param method The method used in the request.
	 */
	private void logRequestComplete(String method) {
		SubCommodityStateWarningController.logger.info(
				String.format(SubCommodityStateWarningController.LOG_COMPLETE_MESSAGE, method));
	}

	/**
	 * Sets the SubCommodityStateWarningService for this object to use. This is used for testing.
	 *
	 * @param service The SubCommodityStateWarningService for this object to use.
	 */
	public void setSubCommodityService(SubCommodityStateWarningService service) {
		this.subCommodityStateWarningService = service;
	}

	/**
	 * Sets the UserInfo for this object to use. This is used for testing.
	 *
	 * @param userInfo The UserInfo for this object to use.
	 */
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	/**
	 * Sets the NonEmptyParameterValidator for this object to use. This is used for testing.
	 *
	 * @param parameterValidator The NonEmptyParameterValidator for this object to use.
	 */
	public void setParameterValidator(NonEmptyParameterValidator parameterValidator) {
		this.parameterValidator = parameterValidator;
	}
}
