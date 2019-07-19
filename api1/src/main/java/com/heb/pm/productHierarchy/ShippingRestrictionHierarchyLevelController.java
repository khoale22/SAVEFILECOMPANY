package com.heb.pm.productHierarchy;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.ShippingRestrictionHierarchyLevel;
import com.heb.pm.entity.ShippingRestrictionHierarchyLevelKey;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * REST controller for all information related to product hierarchy level shipping restrictions.
 *
 * @author m314029
 * @since 2.8.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + ShippingRestrictionHierarchyLevelController.BASE_URL)
@AuthorizedResource(ResourceConstants.PRODUCT_HIERARCHY_SHIPPING_RESTRICTIONS)
public class ShippingRestrictionHierarchyLevelController {

	private static final Logger logger = LoggerFactory.getLogger(ShippingRestrictionHierarchyLevelController.class);

	static final String BASE_URL = "/productHierarchy/shippingRestrictionHierarchyLevel";

	// methods
	private static final String GET_TEMPLATE = "getTemplate";
	private static final String UPDATE = "update";
	private static final String GET_VIEWABLE = "getViewable";

	// log messages
	private static final String LOG_GET_TEMPLATE =
			"User %s from IP %s has requested a hierarchy level shipping restriction with department: %s, " +
					"sub-department: %s, item class: %d, commodity: %d, and sub-commodity: %d.";
	private static final String LOG_UPDATE =
			"User %s from IP %s has requested an update to shipping restrictions: %s.";
	private static final String LOG_COMPLETE_MESSAGE =
			"The ShippingRestrictionHierarchyLevelController method: %s is complete.";

	// error messages
	private static final String NO_RESTRICTIONS_UPDATE_ERROR_MESSAGE = "Must have a shipping restriction to update.";

	// properties file keys
	private static final String NO_RESTRICTIONS_UPDATE_ERROR_KEY = "ShippingRestrictionHierarchyLevelController.missingRestrictions";

	@Autowired
	private ShippingRestrictionHierarchyLevelService service;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private NonEmptyParameterValidator parameterValidator;

	/**
	 * Get shipping restriction that has the product hierarchy attributes already filled in.
	 *
	 * @param department The department for a shipping restriction hierarchy level template being requested.
	 * @param subDepartment The subDepartment for a shipping restriction hierarchy level template being requested.
	 * @param itemClass The itemClass for a shipping restriction hierarchy level template being requested.
	 * @param commodity The commodity for a shipping restriction hierarchy level template being requested.
	 * @param subCommodity The subCommodity for a shipping restriction hierarchy level template being requested.
	 * @param request The request sent from the front end.
	 * @return The shipping restriction template matching requested hierarchy levels.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.GET, value = ShippingRestrictionHierarchyLevelController.GET_TEMPLATE)
	public ShippingRestrictionHierarchyLevel getTemplate(
			@RequestParam String department, @RequestParam String subDepartment, @RequestParam Integer itemClass,
			@RequestParam Integer commodity, @RequestParam Integer subCommodity, HttpServletRequest request){

		this.logGetTemplate(request.getRemoteAddr(), department, subDepartment, itemClass, commodity, subCommodity);
		ShippingRestrictionHierarchyLevel shippingRestriction = new ShippingRestrictionHierarchyLevel();
		ShippingRestrictionHierarchyLevelKey key = new ShippingRestrictionHierarchyLevelKey();
		key.setDepartment(department);
		key.setSubDepartment(subDepartment);
		key.setItemClass(itemClass);
		key.setCommodity(commodity);
		key.setSubCommodity(subCommodity);
		shippingRestriction.setKey(key);
		this.logRequestComplete(ShippingRestrictionHierarchyLevelController.GET_TEMPLATE);
		return shippingRestriction;
	}

	/**
	 * Get shipping restrictions for all current and above hierarchy levels to show on front end.
	 *
	 * @param department The department for a shipping restriction hierarchy level being requested.
	 * @param subDepartment The subDepartment for a shipping restriction hierarchy level being requested.
	 * @param itemClass The itemClass for a shipping restriction hierarchy level being requested.
	 * @param commodity The commodity for a shipping restriction hierarchy level being requested.
	 * @param subCommodity The subCommodity for a shipping restriction hierarchy level being requested.
	 * @param request The request sent from the front end.
	 * @return The list of all shipping restrictions.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = ShippingRestrictionHierarchyLevelController.GET_VIEWABLE)
	public List<ShippingRestrictionHierarchyLevel> getAllViewableShippingRestrictions(
			@RequestParam String department, @RequestParam String subDepartment, @RequestParam Integer itemClass,
			@RequestParam Integer commodity, @RequestParam Integer subCommodity, HttpServletRequest request){

		this.logGetAllViewableShippingRestrictions(request.getRemoteAddr(), department, subDepartment, itemClass, commodity, subCommodity);
		List<ShippingRestrictionHierarchyLevel> toReturn = this.service.
				getAllViewableShippingRestrictions(department, subDepartment, itemClass, commodity, subCommodity);
		this.logRequestComplete(ShippingRestrictionHierarchyLevelController.GET_VIEWABLE);
		return toReturn;
	}

	/**
	 * Update a list of shipping restrictions.
	 *
	 * @param shippingRestrictions Shipping restrictions to update.
	 * @param request The request sent from the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = ShippingRestrictionHierarchyLevelController.UPDATE)
	public void updateShippingRestrictions(
			@RequestBody List<ShippingRestrictionHierarchyLevel> shippingRestrictions, HttpServletRequest request){
		//list is required
		this.parameterValidator.validate(shippingRestrictions,
				ShippingRestrictionHierarchyLevelController.NO_RESTRICTIONS_UPDATE_ERROR_MESSAGE,
				ShippingRestrictionHierarchyLevelController.NO_RESTRICTIONS_UPDATE_ERROR_KEY, request.getLocale());

		this.logUpdateShippingRestrictions(shippingRestrictions, request.getRemoteAddr());
		this.service.update(shippingRestrictions);
		this.logRequestComplete(ShippingRestrictionHierarchyLevelController.UPDATE);
	}

	/**
	 * Logs a user's request to get all viewable shipping restrictions.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param department The department for a shipping restriction hierarchy level being requested.
	 * @param subDepartment The subDepartment for a shipping restriction hierarchy level being requested.
	 * @param itemClass The itemClass for a shipping restriction hierarchy level being requested.
	 * @param commodity The commodity for a shipping restriction hierarchy level being requested.
	 * @param subCommodity The subCommodity for a shipping restriction hierarchy level being requested.
	 */
	private void logGetAllViewableShippingRestrictions(String ip, String department, String subDepartment, Integer itemClass,
								Integer commodity, Integer subCommodity) {
		ShippingRestrictionHierarchyLevelController.logger.info(
				String.format(ShippingRestrictionHierarchyLevelController.LOG_GET_TEMPLATE,
						this.userInfo.getUserId(), ip, department, subDepartment, itemClass,
						commodity, subCommodity));
	}

	/**
	 * Logs a user's request to get hierarchy filled shipping restriction.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param department The department for a shipping restriction hierarchy level template being requested.
	 * @param subDepartment The subDepartment for a shipping restriction hierarchy level template being requested.
	 * @param itemClass The itemClass for a shipping restriction hierarchy level template being requested.
	 * @param commodity The commodity for a shipping restriction hierarchy level template being requested.
	 * @param subCommodity The subCommodity for a shipping restriction hierarchy level template being requested.
	 */
	private void logGetTemplate(String ip, String department, String subDepartment, Integer itemClass,
								Integer commodity, Integer subCommodity) {
		ShippingRestrictionHierarchyLevelController.logger.info(
				String.format(ShippingRestrictionHierarchyLevelController.LOG_GET_TEMPLATE,
						this.userInfo.getUserId(), ip, department, subDepartment, itemClass,
						commodity, subCommodity));
	}

	/**
	 * Logs shipping restrictions update.
	 *
	 * @param shippingRestrictions Shipping restrictions to be updated.
	 * @param ip The IP address the user is logged in from.
	 */
	private void logUpdateShippingRestrictions(List<ShippingRestrictionHierarchyLevel> shippingRestrictions, String ip) {
		ShippingRestrictionHierarchyLevelController.logger.info(
				String.format(ShippingRestrictionHierarchyLevelController.LOG_UPDATE, this.userInfo.getUserId(), ip, shippingRestrictions)
		);
	}

	/**
	 * Logs completion of an http request.
	 *
	 * @param method The method used in the request.
	 */
	private void logRequestComplete(String method) {
		ShippingRestrictionHierarchyLevelController.logger.info(
				String.format(ShippingRestrictionHierarchyLevelController.LOG_COMPLETE_MESSAGE, method));
	}
}
