package com.heb.pm.productHierarchy;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.ProductPreferredUnitOfMeasure;
import com.heb.pm.entity.ProductPreferredUnitOfMeasureKey;
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
 * REST endpoint for accessing product preferred unit of measure information.
 *
 * @author m314029
 * @since 2.12.0
 */
@RestController
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + ProductPreferredUnitOfMeasureController.SUB_COMMODITY_UNIT_OF_MEASURE_URL)
@AuthorizedResource(ResourceConstants.PRODUCT_HIERARCHY_SUB_COMMODITY_UNIT_OF_MEASURE)
public class ProductPreferredUnitOfMeasureController {

	public static final Logger logger = LoggerFactory.getLogger(ProductPreferredUnitOfMeasureController.class);

	// urls
	public static final String SUB_COMMODITY_UNIT_OF_MEASURE_URL = "/productHierarchy/subCommodity/preferredUnitOfMeasure";
	private static final String UPDATE = "update";
	private static final String GET_EMPTY = "getEmpty";

	// logs
	private static final String LOG_COMPLETE_MESSAGE =
			"The ProductPreferredUnitOfMeasureController method: %s is complete.";
	private static final String UPDATE_LOG_MESSAGE =
			"User %s from IP %s requested to update product preferred units of measure: %s.";
	private static final String GET_EMPTY_LOG_MESSAGE =
			"User %s from IP %s requested an empty product preferred unit of measure.";

	// error messages
	private static final String NO_UNITS_OF_MEASURE_UPDATE_ERROR_MESSAGE = "Must have at least one unit of measure to update.";

	// properties file keys
	private static final String NO_UNITS_OF_MEASURE_UPDATE_ERROR_KEY = "ProductPreferredUnitOfMeasureController.missingUnitsOfMeasure";

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private ProductPreferredUnitOfMeasureService productPreferredUnitOfMeasureService;

	@Autowired
	private NonEmptyParameterValidator parameterValidator;

	/**
	 * Endpoint to update preferredUnitOfMeasures.
	 *
	 * @param preferredUnitOfMeasures The ProductPreferredUnitOfMeasures to update.
	 * @param request The HTTP request that initiated this call.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value=ProductPreferredUnitOfMeasureController.UPDATE)
	public List<ProductPreferredUnitOfMeasure> update(
			@RequestBody List<ProductPreferredUnitOfMeasure> preferredUnitOfMeasures, HttpServletRequest request){
		//list is required
		this.parameterValidator.validate(preferredUnitOfMeasures,
				ProductPreferredUnitOfMeasureController.NO_UNITS_OF_MEASURE_UPDATE_ERROR_MESSAGE,
				ProductPreferredUnitOfMeasureController.NO_UNITS_OF_MEASURE_UPDATE_ERROR_KEY, request.getLocale());
		this.logUpdate(request.getRemoteAddr(), preferredUnitOfMeasures);
		this.productPreferredUnitOfMeasureService.update(preferredUnitOfMeasures);
		List<ProductPreferredUnitOfMeasure> toReturn =
				this.productPreferredUnitOfMeasureService.findBySubCommodity(preferredUnitOfMeasures.get(0));
		this.logRequestComplete(ProductPreferredUnitOfMeasureController.UPDATE);
		return toReturn;
	}

	/**
	 * Endpoint to retrieve an empty ProductPreferredUnitOfMeasure used for editing.
	 *
	 * @param request The HTTP request that initiated this call.
	 * @return An empty ProductPreferredUnitOfMeasure.
	 */
	@RequestMapping(method = RequestMethod.GET, value=ProductPreferredUnitOfMeasureController.GET_EMPTY)
	public ProductPreferredUnitOfMeasure getEmpty(HttpServletRequest request){
		this.logGetEmpty(request.getRemoteAddr());
		ProductPreferredUnitOfMeasure toReturn = new ProductPreferredUnitOfMeasure();
		toReturn.setKey(new ProductPreferredUnitOfMeasureKey());
		return toReturn;
	}

	/**
	 * Logs a get empty for product preferred unit of measure.
	 *
	 * @param ip The IP address of the user.
	 */
	private void logGetEmpty(String ip) {
		ProductPreferredUnitOfMeasureController.logger.info(
				String.format(ProductPreferredUnitOfMeasureController.GET_EMPTY_LOG_MESSAGE,
						this.userInfo.getUserId(), ip)
		);
	}

	/**
	 * Logs an update for preferredUnitOfMeasures.
	 *
	 * @param ip The IP address of the user.
	 */
	private void logUpdate(String ip, List<ProductPreferredUnitOfMeasure> preferredUnitOfMeasures) {
		ProductPreferredUnitOfMeasureController.logger.info(
				String.format(ProductPreferredUnitOfMeasureController.UPDATE_LOG_MESSAGE,
						this.userInfo.getUserId(), ip, preferredUnitOfMeasures)
		);
	}

	/**
	 * Logs completion of an http request.
	 *
	 * @param method The method used in the request.
	 */
	private void logRequestComplete(String method) {
		ProductPreferredUnitOfMeasureController.logger.info(
				String.format(ProductPreferredUnitOfMeasureController.LOG_COMPLETE_MESSAGE, method));
	}

	/**
	 * Sets the ProductPreferredUnitOfMeasureService for this object to use. This is used for testing.
	 *
	 * @param productPreferredUnitOfMeasureService The ProductPreferredUnitOfMeasureService for this object to use.
	 */
	public void setProductPreferredUnitOfMeasureService(ProductPreferredUnitOfMeasureService productPreferredUnitOfMeasureService) {
		this.productPreferredUnitOfMeasureService = productPreferredUnitOfMeasureService;
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
