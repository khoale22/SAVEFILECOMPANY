package com.heb.pm.productDiscontinue;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.util.controller.ModifiedEntity;
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

/**
 * REST endpoint for functions related to viewing and modifying product discontinue admin rules.
 *
 * @author d116773
 * @since 2.0.2
 */
@RestController
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + DiscontinueParametersController.PRODUCT_DISCONTINUE_ADMIN_URL)
@AuthorizedResource(ResourceConstants.PRODUCT_DISCONTINUE_DEFAULT_PARAMETERS)
public class DiscontinueParametersController {

	private static final Logger logger = LoggerFactory.getLogger(DiscontinueParametersController.class);

	protected static final String PRODUCT_DISCONTINUE_ADMIN_URL = "/productDiscontinue/parameters";

	// Log messages
	private static final String FIND_ALL_MESSAGE =
			"User %s from IP %s has requested a list of default product discontinue rules.";
	private static final String UPDATE_MESSAGE =
			"User %s from IP %s has requested and update of product discontinue rules to %s";

	// Errors for the front-end
	private static final String NO_DISCONTINUE_RULES_OBJECT_MESSAGE_KEY =
			"DiscontinueParametersController.missingDiscontinueRulesObject";
	private static final String DEFAULT_NO_DISCONTINUE_RULES_OBJECT_MESSAGE =
			"New default product discontinue rules cannot be empty.";

	@Autowired private DiscontinueParametersService discontinueParametersService;
	@Autowired private UserInfo userInfo;
	@Autowired private NonEmptyParameterValidator parameterValidator;

	/**
	 * Returns an object containing the default product discontinue rules.
	 *
	 * @param request The HTTP request that initiated this call.
	 * @return The default product discontinue rules.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET)
	public DiscontinueRules findAll(HttpServletRequest request){

		this.logFindAll(request.getRemoteAddr());
		return this.discontinueParametersService.findAll();
	}

	/**
	 * Updates the default product discontinue parameters.
	 *
	 * @param newParameters DiscontinueRules with DiscontinueParameters.
	 * @param request The HTTP request that initiated this call.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.PUT)
	public ModifiedEntity<DiscontinueRules> updateAll(
			@RequestBody DiscontinueRules newParameters,
			HttpServletRequest request){

		this.parameterValidator.validate(newParameters,
				DiscontinueParametersController.DEFAULT_NO_DISCONTINUE_RULES_OBJECT_MESSAGE,
				DiscontinueParametersController.NO_DISCONTINUE_RULES_OBJECT_MESSAGE_KEY,
				request.getLocale());

		this.logUpdate(request.getRemoteAddr(), newParameters);

		return new ModifiedEntity<>(this.discontinueParametersService.update(newParameters),
				"Save successful");
	}

	/**
	 * Logs a user's request for default product discontinue rules.
	 *
	 * @param ip The IP address the user is logged in from.
	 */
	private void logFindAll(String ip) {
		DiscontinueParametersController.logger.info(String.format(DiscontinueParametersController.FIND_ALL_MESSAGE,
				this.userInfo.getUserId(), ip));
	}

	/**
	 * Logs a user's request to update product discontinue rules.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param discontinueRules The rules they would like to be saved.
	 */
	private void logUpdate(String ip, DiscontinueRules discontinueRules) {
		DiscontinueParametersController.logger.info(String.format(DiscontinueParametersController.UPDATE_MESSAGE,
				this.userInfo.getUserId(), ip, discontinueRules));
	}
}
