package com.heb.pm.productHierarchy;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.CoreTransactional;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.ItemClass;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * REST endpoint for accessing itemClass information.
 *
 * @author m314029
 * @since 2.6.0
 */
@RestController
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + ItemClassController.ITEM_CLASS_URL)
@AuthorizedResource(ResourceConstants.PRODUCT_HIERARCHY_CLASS_COMMODITYLIST)
public class ItemClassController {

	public static final Logger logger = LoggerFactory.getLogger(ItemClassController.class);

	// urls
	static final String ITEM_CLASS_URL = "/productHierarchy/itemClass";
	private static final String FIND_BY_REGULAR_EXPRESSION = "findByRegularExpression";
	private static final String GET_EMPTY = "getEmpty";
	private static final String UPDATE = "update";

	// errors
	private static final String NO_SEARCH_STRING_ERROR_KEY = "ItemClassController.missingSearchString";
	private static final String NO_SEARCH_STRING_DEFAULT_ERROR_MESSAGE = "Must have a pattern to search for.";
	private static final String NO_ITEM_CLASS_ERROR_KEY = "ItemClassController.missingItemClass";
	private static final String NULL_ITEM_CLASS_ERROR_MESSAGE = "Must have an item class to search for.";

	// logs
	private static final String ITEM_CLASS_SEARCH_BY_PATTERN_LOG_MESSAGE =
			"User %s from IP %s searched for item-classes with the pattern '%s'";
	private static final String ITEM_CLASS_GET_EMPTY_LOG_MESSAGE =
			"User %s from IP %s requested an empty item class.";
	private static final String ITEM_CLASS_UPDATE_LOG_MESSAGE =
			"User %s from IP %s requested to update item class: %s.";
	private static final String LOG_COMPLETE_MESSAGE =
			"The ItemClassController method: %s is complete.";
	private static final String ITEM_CLASS_CODE_LOG_MESSAGE =
			"User %s from IP %s requested item class code: %s.";

	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_PAGE_SIZE = 15;

	@Autowired
	private ItemClassService itemClassService;

	@Autowired
	private NonEmptyParameterValidator parameterValidator;

	@Autowired
	private UserInfo userInfo;

	/**
	 * Endpoint to search for item-classes by a regular expression.The String passed in is treated as the middle
	 * of a regular expression and any class with a name or ID that contains the string passed in will be
	 * returned. It supports paging and will return a subset of the item-classes that match the search.
	 *
	 * @param searchString The text you are looking for.
	 * @param page The page to look for.
	 * @param pageSize The maximum number of records to search for.
	 * @param request The HTTP request that initiated this call.
	 * @return A list of item-classes whose name or number match the supplied search string.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value=ItemClassController.FIND_BY_REGULAR_EXPRESSION)
	public PageableResult<ItemClass> findClassesByRegularExpression(
			@RequestParam("searchString") String searchString,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			HttpServletRequest request) {

		// Search string is required.
		this.parameterValidator.validate(searchString, ItemClassController.NO_SEARCH_STRING_DEFAULT_ERROR_MESSAGE,
				ItemClassController.NO_SEARCH_STRING_ERROR_KEY, request.getLocale());

		this.logItemClassSearch(request.getRemoteAddr(), searchString);

		// Sets defaults for page and pageSize if they are not passed in.
		int pg = page == null ? ItemClassController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? ItemClassController.DEFAULT_PAGE_SIZE : pageSize;

		return this.itemClassService.findItemClassesByRegularExpression(searchString, pg, ps);
	}

	/**
	 * Endpoint to retrieve an empty ItemClass used for editing.
	 *
	 * @param request The HTTP request that initiated this call.
	 * @return An empty ItemClass.
	 */
	@RequestMapping(method = RequestMethod.GET, value=ItemClassController.GET_EMPTY)
	public ItemClass getEmpty(HttpServletRequest request){
		this.logGetEmptyItemClass(request.getRemoteAddr());
		ItemClass itemClass = new ItemClass();
		itemClass.setItemClassDescription(StringUtils.EMPTY);
		return itemClass;
	}

	/**
	 * Endpoint to update an ItemClass.
	 *
	 * @param itemClass The ItemClass to update.
	 * @param request The HTTP request that initiated this call.
	 */
	@CoreTransactional
	@RequestMapping(method = RequestMethod.POST, value=ItemClassController.UPDATE)
	public ItemClass update(@RequestBody ItemClass itemClass, HttpServletRequest request){
		this.logUpdateItemClass(request.getRemoteAddr(), itemClass);
		this.itemClassService.update(itemClass);
		ItemClass itemClassAfterUpdate = this.itemClassService.findOneByItemClassCode(itemClass.getItemClassCode());
		this.logRequestComplete(ItemClassController.UPDATE);
		return itemClassAfterUpdate;
	}

	/**
	 * Logs an update for item-class.
	 *
	 * @param ip The IP address of the user searching for classes.
	 * @param itemClass The item-class the user is updating.
	 */
	private void logUpdateItemClass(String ip, ItemClass itemClass) {
		ItemClassController.logger.info(
				String.format(ItemClassController.ITEM_CLASS_UPDATE_LOG_MESSAGE,
						this.userInfo.getUserId(), ip, itemClass.toString())
		);
	}

	/**
	 * Logs a get empty for item-class.
	 *
	 * @param ip The IP address of the user searching for classes.
	 */
	private void logGetEmptyItemClass(String ip) {
		ItemClassController.logger.info(
				String.format(ItemClassController.ITEM_CLASS_GET_EMPTY_LOG_MESSAGE,
						this.userInfo.getUserId(), ip)
		);
	}

	/**
	 * Logs a search for classes.
	 *
	 * @param ip The IP address of the user searching for classes.
	 * @param searchString The string to user is searching for classes by.
	 */
	private void logItemClassSearch(String ip, String searchString) {
		ItemClassController.logger.info(
				String.format(ItemClassController.ITEM_CLASS_SEARCH_BY_PATTERN_LOG_MESSAGE,
						this.userInfo.getUserId(), ip, searchString)
		);
	}

	/**
	 * Logs completion of an http request.
	 *
	 * @param method The method used in the request.
	 */
	private void logRequestComplete(String method) {
		ItemClassController.logger.info(
				String.format(ItemClassController.LOG_COMPLETE_MESSAGE, method));
	}

	/**
	 * Sets the item class service used for testing.
	 *
	 * @param itemClassService
	 */
	public void setItemClassService(ItemClassService itemClassService) {
		this.itemClassService = itemClassService;
	}

	/**
	 * Sets the parameter validator used for testing..
	 *
	 * @param parameterValidator
	 */
	public void setParameterValidator(NonEmptyParameterValidator parameterValidator) {
		this.parameterValidator = parameterValidator;
	}

	/**
	 * Sets the user info used for testing.
	 *
	 * @param userInfo
	 */
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}

	/**
	 * Endpoint to search for item-classes by a item class code.
	 *
	 * @param itemClassCode The key of the item class the user is looking for
	 * @param request The HTTP request that initiated this call.
	 * @return The item class the user searched for
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value="getCurrentItemClassInfo")
	public ItemClass getCurrentItemClassInfo(
			@RequestParam("itemClassCode") Integer itemClassCode, HttpServletRequest request) {
		// Search string is required.
		this.parameterValidator.validate(itemClassCode, ItemClassController.NULL_ITEM_CLASS_ERROR_MESSAGE,
				ItemClassController.NO_ITEM_CLASS_ERROR_KEY, request.getLocale());

		this.logItemClassCode(request.getRemoteAddr(), itemClassCode);

		ItemClass itemClass = itemClassService.findOneByItemClassCode(itemClassCode);
		BaseHierarchyResolver resolver = new BaseHierarchyResolver();
		resolver.resolveItemClass(itemClass);

		return itemClass;
	}

	/**
	 * Logs the item-class code the user accessing.
	 *
	 * @param ip The IP address of the user searching for classes.
	 * @param itemClassCode The item-class code the user is updating.
	 */
	private void logItemClassCode(String ip, Integer itemClassCode) {
		ItemClassController.logger.info(
				String.format(ItemClassController.ITEM_CLASS_CODE_LOG_MESSAGE,
						this.userInfo.getUserId(), ip, itemClassCode.toString())
		);
	}

}
