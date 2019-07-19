package com.heb.pm.productHierarchy;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.SellingRestrictionHierarchyLevel;
import com.heb.pm.entity.SellingRestrictionHierarchyLevelKey;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * REST controller for all information related to product hierarchy level selling restrictions.
 *
 * @author m314029
 * @since 2.8.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + SellingRestrictionHierarchyLevelController.BASE_URL)
@AuthorizedResource(ResourceConstants.PRODUCT_HIERARCHY_SELLING_RESTRICTIONS)
public class SellingRestrictionHierarchyLevelController {

	private static final Logger logger = LoggerFactory.getLogger(SellingRestrictionHierarchyLevelController.class);

	static final String BASE_URL = "/productHierarchy/sellingRestrictionHierarchyLevel";

	// methods
	private static final String GET_TEMPLATE = "getTemplate";
	private static final String UPDATE = "update";
	private static final String GET_VIEWABLE = "getViewable";

	// log messages
	private static final String LOG_GET_TEMPLATE =
			"User %s from IP %s has requested a hierarchy level selling restriction with department: %s, sub-department: " +
					"%s, item class: %d, commodity: %d, and sub-commodity: %d.";
	private static final String LOG_UPDATE =
			"User %s from IP %s has requested an update to selling restrictions: %s.";
	private static final String LOG_COMPLETE_MESSAGE =
			"The method: %s is complete.";

	// error messages
	private static final String NO_RESTRICTIONS_UPDATE_ERROR_MESSAGE = "Must have a selling restriction to update.";

	// properties file keys
	private static final String NO_RESTRICTIONS_UPDATE_ERROR_KEY = "SellingRestrictionHierarchyLevelController.missingRestrictions";

	@Autowired
	private SellingRestrictionHierarchyLevelService service;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private NonEmptyParameterValidator parameterValidator;

	/**
	 * Get selling restriction that has the product hierarchy attributes already filled in.
	 *
	 * @param department The department for a selling restriction hierarchy level template being requested.
	 * @param subDepartment The subDepartment for a selling restriction hierarchy level template being requested.
	 * @param itemClass The itemClass for a selling restriction hierarchy level template being requested.
	 * @param commodity The commodity for a selling restriction hierarchy level template being requested.
	 * @param subCommodity The subCommodity for a selling restriction hierarchy level template being requested.
	 * @param request The request sent from the front end.
	 * @return The list of all selling restrictions.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.GET, value = SellingRestrictionHierarchyLevelController.GET_TEMPLATE)
	public SellingRestrictionHierarchyLevel getTemplate(
			@RequestParam String department, @RequestParam String subDepartment, @RequestParam Integer itemClass,
			@RequestParam Integer commodity, @RequestParam Integer subCommodity, HttpServletRequest request){

		this.logGetTemplate(request.getRemoteAddr(), department, subDepartment, itemClass, commodity, subCommodity);
		SellingRestrictionHierarchyLevel sellingRestriction = new SellingRestrictionHierarchyLevel();
		SellingRestrictionHierarchyLevelKey key = new SellingRestrictionHierarchyLevelKey();
		key.setDepartment(department);
		key.setSubDepartment(subDepartment);
		key.setItemClass(itemClass);
		key.setCommodity(commodity);
		key.setSubCommodity(subCommodity);
		sellingRestriction.setKey(key);
		this.logRequestComplete(SellingRestrictionHierarchyLevelController.GET_TEMPLATE);
		return sellingRestriction;
	}

	/**
	 * Update a list of selling restrictions.
	 *
	 * @param sellingRestrictions Selling restrictions to update.
	 * @param request The request sent from the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = SellingRestrictionHierarchyLevelController.UPDATE)
	public void updateSellingRestrictions(
			@RequestBody List<SellingRestrictionHierarchyLevel> sellingRestrictions, HttpServletRequest request){
		//list is required
		this.parameterValidator.validate(sellingRestrictions,
				SellingRestrictionHierarchyLevelController.NO_RESTRICTIONS_UPDATE_ERROR_MESSAGE,
				SellingRestrictionHierarchyLevelController.NO_RESTRICTIONS_UPDATE_ERROR_KEY, request.getLocale());

		this.logUpdateSellingRestrictions(sellingRestrictions, request.getRemoteAddr());
		this.service.update(sellingRestrictions);
		this.logRequestComplete(SellingRestrictionHierarchyLevelController.UPDATE);
	}

	/**
	 * Get selling restrictions for all current and above hierarchy levels to show on front end.
	 *
	 * @param department The department for a selling restriction hierarchy level being requested.
	 * @param subDepartment The subDepartment for a selling restriction hierarchy level being requested.
	 * @param itemClass The itemClass for a selling restriction hierarchy level being requested.
	 * @param commodity The commodity for a selling restriction hierarchy level being requested.
	 * @param subCommodity The subCommodity for a selling restriction hierarchy level being requested.
	 * @param request The request sent from the front end.
	 * @return The list of all selling restrictions.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = SellingRestrictionHierarchyLevelController.GET_VIEWABLE)
	public List<SellingRestrictionHierarchyLevel> getAllViewableSellingRestrictions(
			@RequestParam String department, @RequestParam String subDepartment, @RequestParam Integer itemClass,
			@RequestParam Integer commodity, @RequestParam Integer subCommodity, HttpServletRequest request){

		this.logGetAllViewableSellingRestrictions(request.getRemoteAddr(), department, subDepartment, itemClass, commodity, subCommodity);
		List<SellingRestrictionHierarchyLevel> toReturn = this.service.
				getAllViewableSellingRestrictions(department, subDepartment, itemClass, commodity, subCommodity);
		this.logRequestComplete(SellingRestrictionHierarchyLevelController.GET_VIEWABLE);
		return toReturn;
	}

	/**
	 * Logs a user's request to get all viewable selling restrictions.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param department The department for a selling restriction hierarchy level being requested.
	 * @param subDepartment The subDepartment for a selling restriction hierarchy level being requested.
	 * @param itemClass The itemClass for a selling restriction hierarchy level being requested.
	 * @param commodity The commodity for a selling restriction hierarchy level being requested.
	 * @param subCommodity The subCommodity for a selling restriction hierarchy level being requested.
	 */
	private void logGetAllViewableSellingRestrictions(String ip, String department, String subDepartment, Integer itemClass,
													   Integer commodity, Integer subCommodity) {
		SellingRestrictionHierarchyLevelController.logger.info(
				String.format(SellingRestrictionHierarchyLevelController.LOG_GET_TEMPLATE,
						this.userInfo.getUserId(), ip, department, subDepartment, itemClass,
						commodity, subCommodity));
	}

	/**
	 * Logs a user's request to get hierarchy filled selling restriction.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param department The department for a selling restriction hierarchy level template being requested.
	 * @param subDepartment The subDepartment for a selling restriction hierarchy level template being requested.
	 * @param itemClass The itemClass for a selling restriction hierarchy level template being requested.
	 * @param commodity The commodity for a selling restriction hierarchy level template being requested.
	 * @param subCommodity The subCommodity for a selling restriction hierarchy level template being requested.
	 */
	private void logGetTemplate(String ip, String department, String subDepartment, Integer itemClass,
								Integer commodity, Integer subCommodity) {
		SellingRestrictionHierarchyLevelController.logger.info(
				String.format(SellingRestrictionHierarchyLevelController.LOG_GET_TEMPLATE,
						this.userInfo.getUserId(), ip, department, subDepartment, itemClass,
						commodity, subCommodity));
	}

	/**
	 * Logs selling restrictions update.
	 *
	 * @param sellingRestrictions Selling restrictions to be updated.
	 * @param ip The IP address the user is logged in from.
	 */
	private void logUpdateSellingRestrictions(List<SellingRestrictionHierarchyLevel> sellingRestrictions, String ip) {
		SellingRestrictionHierarchyLevelController.logger.info(
				String.format(SellingRestrictionHierarchyLevelController.LOG_UPDATE, this.userInfo.getUserId(), ip, sellingRestrictions)
		);
	}

	/**
	 * Logs completion of an http request.
	 *
	 * @param method The method used in the request.
	 */
	private void logRequestComplete(String method) {
		SellingRestrictionHierarchyLevelController.logger.info(
				String.format(SellingRestrictionHierarchyLevelController.LOG_COMPLETE_MESSAGE, method));
	}
}
