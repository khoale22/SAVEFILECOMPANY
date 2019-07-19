/*
 * IngredientCategoryController
 *
 *  Copyright (c) 2016 HEB
 *  All rights reserved.
 *
 *  This software is the confidential and proprietary information
 *  of HEB.
 */

package com.heb.pm.scaleManagement;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.Hits;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.IngredientCategory;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.PageableResult;
import com.heb.util.list.ListFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * REST controller that returns all information related to ingredients.
 * @author s573181
 * @since 2.1.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + IngredientCategoryController.INGREDIENT_CATEGORY_URL)
@AuthorizedResource(ResourceConstants.SCALE_MANAGEMENT_INGREDIENT_CATEGORIES)
public class IngredientCategoryController {

	private static final Logger logger = LoggerFactory.getLogger(IngredientCategoryController.class);

	protected static final String INGREDIENT_CATEGORY_URL = "/ingredientCategory";

	// Keys to user facing messages in the message resource bundle.
	private static final String CATEGORY_CD_MESSAGE_KEY = "IngredientCategoryController.missingCategoryCode";
	private static final String DEFAULT_NO_CATEGORY_CD_MESSAGE = "Must search for at least one category code.";
	private static final String CATEGORY_DESCRIPTION_MESSAGE_KEY =
			"IngredientCategoryController.missingCategoryCodeDescription";
	private static final String DEFAULT_NO_CATEGORY_DESCRIPTION_MESSAGE =
			"Must search for a category code description.";
	private static final String DEFAULT_ADD_SUCCESS_MESSAGE ="Ingredient category code: %s added successfully.";
	private static final String ADD_SUCCESS_MESSAGE_KEY = "IngredientCategoryController.addSuccessful";
	private static final String DEFAULT_DELETE_SUCCESS_MESSAGE ="Ingredient category code: %s deleted successfully.";
	private static final String DELETE_SUCCESS_MESSAGE_KEY = "IngredientCategoryController.deleteSuccessful";
	private static final String DEFAULT_UPDATE_SUCCESS_MESSAGE ="Ingredient category code: %s updated successfully.";
	private static final String UPDATE_SUCCESS_MESSAGE_KEY = "IngredientCategoryController.updateSuccessful";


	//log messages
	private static final String FIND_ALL_MESSAGE =
			"User %s from IP %s has requested all ingredient category data.";
	private static final String FIND_BY_CATEGORY_CODE_MESSAGE =
			"User %s from IP %s has requested ingredient category data for the following ingredient codes [%s]";
	private static final String FIND_BY_CATEGORY_DESCRIPTION_MESSAGE =
			"User %s from IP %s has requested ingredient category data for the following description '%s'";
	private static final String ADD_INGREDIENT_CATEGORY_MESSAGE =
			"User %s from IP %s has requested to add an ingredient category with the description '%s'";
	private static final String UPDATE_INGREDIENT_CATEGORY_MESSAGE =
			"User %s from IP %s has requested to update the ingredient category with the ingredient category code: %d " +
					" to have the description '%s'";
	private static final String DELETE_INGREDIENT_CATEGORY_MESSAGE =
			"User %s from IP %s has requested to delete the ingredient category with the ingredient category code: %d.";
	@Autowired
	private IngredientCategoryService service;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private	NonEmptyParameterValidator parameterValidator;

	/**
	 * Get all ingredient categories list.
	 *
	 * @param request the request
	 * @return the list
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "allCategoriesList")
	public List<IngredientCategory> findAllIngredientCategoriesList(HttpServletRequest request){
		this.logFindAll(request.getRemoteAddr());
		return this.service.findAllIngredientCategories();
	}

	/**
	 * Returns the current page of Ingredient Categories on the find all method.
	 *
	 * @param includeCounts True to return the total number of records and pages. False to just return the data.
	 * @param page The page you are looking for.
	 * @param request the HTTP request that initiated this call.
	 * @return a list of ingredient category codes from a list of action codes.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "allCategories")
	public PageableResult<IngredientCategory> findAll(
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize, HttpServletRequest request){
		this.logFindAll(request.getRemoteAddr());
		return this.service.findAll(includeCounts, page, pageSize);
	}

	/**
	 * Returns a list of IngredientCategory from a list of category codes.
	 *
	 * @param categoryCodes a list of category codes.
	 * @param includeCounts True to return the total number of records and pages. False to just return the data.
	 * @param page The page you are looking for.
	 * @param request the HTTP request that initiated this call.
	 * @return a list of IngredientCategory from a list of category codes.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "ingredientCode")
	public PageableResult<IngredientCategory> findByCategoryCode(
			@RequestParam("categoryCodes") List<Long> categoryCodes,
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize, HttpServletRequest request){
		this.logFindByCategoryCode(request.getRemoteAddr(), categoryCodes);
		this.parameterValidator.validate(categoryCodes, IngredientCategoryController.DEFAULT_NO_CATEGORY_CD_MESSAGE,
				IngredientCategoryController.CATEGORY_CD_MESSAGE_KEY, request.getLocale());
		return this.service.findByCategoryCode(categoryCodes, includeCounts, page, pageSize);
	}

	/**
	 * Returns a list of IngredientCategory from a category description.
	 *
	 * @param categoryDescription a category description.
	 * @param includeCounts True to return the total number of records and pages. False to just return the data.
	 * @param page The page you are looking for.
	 * @param request the HTTP request that initiated this call.
	 * @return a list of IngredientCategory from a list of category codes.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "ingredientDescription")
	public PageableResult<IngredientCategory> findByCategoryDescription(
			@RequestParam("categoryDescription")String categoryDescription,
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize, HttpServletRequest request){
		this.logFindByDescription(request.getRemoteAddr(), categoryDescription);
		this.parameterValidator.validate(categoryDescription,
				IngredientCategoryController.DEFAULT_NO_CATEGORY_DESCRIPTION_MESSAGE,
				IngredientCategoryController.CATEGORY_DESCRIPTION_MESSAGE_KEY, request.getLocale());
		return this.service.findByCategoryDescription(categoryDescription, includeCounts, page, pageSize);
	}

	/**
	 * Returns the Hits result with found and not found category codes.
	 *
	 * @param categoryCodes The category code to search on.
	 * @param request the HTTP request that initiated this call.
	 * @return Hits result with found and not found action codes.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "/hits/ingredientCode")
	public Hits findHitsByCategoryCode(@RequestParam("categoryCodes") List<Long> categoryCodes, HttpServletRequest request){
		this.parameterValidator.validate(categoryCodes, IngredientCategoryController.DEFAULT_NO_CATEGORY_CD_MESSAGE,
				IngredientCategoryController.CATEGORY_CD_MESSAGE_KEY, request.getLocale());
		this.logFindByCategoryCode(request.getRemoteAddr(), categoryCodes);
		return this.service.findHitsByCategoryCode(categoryCodes);
	}

	/**
	 * Adds a new Ingredient Category.
	 *
	 * @param ingredientCategory The ingredient category to be added.
	 * @param request The HTTP request that initiated this call.
	 * @return The new ingredeint category and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = "addIngredientCategory")
	public ModifiedEntity<IngredientCategory> addIngredientCategory(@RequestBody IngredientCategory ingredientCategory,
																	HttpServletRequest request){
		this.logAdd(request.getRemoteAddr(), ingredientCategory.getCategoryDescription());
		IngredientCategory newIngredientCategory = this.service.add(ingredientCategory.getCategoryDescription());
		String updateMessage = this.messageSource.getMessage(IngredientCategoryController.ADD_SUCCESS_MESSAGE_KEY,
				new Object[]{newIngredientCategory.getCategoryCode()},
				IngredientCategoryController.DEFAULT_ADD_SUCCESS_MESSAGE, request.getLocale());
		return new ModifiedEntity<>(newIngredientCategory, updateMessage);
	}

	/**
	 * Updates an existing Ingredient Category.
	 *
	 * @param ingredientCategory The ingredient category to be added.
	 * @param request The HTTP request that initiated this call.
	 * @return The new ingredient category and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.PUT, value = "updateIngredientCategory")
	public ModifiedEntity<IngredientCategory> updateIngredientCategory(
			@RequestBody IngredientCategory ingredientCategory, HttpServletRequest request) {
		this.logUpdate(request.getRemoteAddr(), ingredientCategory);
		this.service.update(ingredientCategory);
		String updateMessage = messageSource.getMessage(IngredientCategoryController.UPDATE_SUCCESS_MESSAGE_KEY,
				new Object[]{ingredientCategory.getCategoryCode()}, IngredientCategoryController.DEFAULT_UPDATE_SUCCESS_MESSAGE,
				request.getLocale());
		return new ModifiedEntity<>(ingredientCategory,updateMessage);
	}

	/**
	 * Deletes an existing ingredient category.
	 *
	 * @param ingredientCode The ingredient code to be added.
	 * @param request The HTTP request that initiated this call.
	 * @return The new ingredient category that was deleted and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.DELETE, value = "deleteIngredientCategory")
	public ModifiedEntity<Long> deleteIngredientCategory(
			@RequestParam Long ingredientCode, HttpServletRequest request) {
		this.logDelete(request.getRemoteAddr(), ingredientCode);
		this.service.delete(ingredientCode);
		String updateMessage = this.messageSource.getMessage(IngredientCategoryController.DELETE_SUCCESS_MESSAGE_KEY,
				new Object[]{ingredientCode}, IngredientCategoryController.DEFAULT_DELETE_SUCCESS_MESSAGE,
				request.getLocale());
		return new ModifiedEntity<>(ingredientCode, updateMessage);
	}

	/**
	 * Logs a user's request to delete an ingredient category.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param ingredientCode the ingredient category to be deleted.
	 */
	private void logDelete(String ip, Long ingredientCode) {
		IngredientCategoryController.logger.info(String.format(
				IngredientCategoryController.DELETE_INGREDIENT_CATEGORY_MESSAGE, this.userInfo.getUserId(), ip,
				ingredientCode));
	}

	/**
		 * Logs a user's request to update a ingredient category.
		 *
		 * @param ip The IP address the user is logged in from.
		 * @param ingredientCategory The ingredient category to be updated.
		 */
	private void logUpdate(String ip, IngredientCategory ingredientCategory) {
		IngredientCategoryController.logger.info(String.format(
				IngredientCategoryController.UPDATE_INGREDIENT_CATEGORY_MESSAGE, this.userInfo.getUserId(), ip,
				ingredientCategory.getCategoryCode(), ingredientCategory.getCategoryDescription()));
	}

	/**
	 * Logs a user's request to add a ingredient category.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param description The description of the ingredient category to be added.
	 */
	private void logAdd(String ip, String description) {
		IngredientCategoryController.logger.info(String.format(
				IngredientCategoryController.ADD_INGREDIENT_CATEGORY_MESSAGE, this.userInfo.getUserId(),
				ip, description));
	}

	/**
	 * Logs a user's request to get Ingredient Category information based on a description.
	 *
	 * @param ip The IP address th user is logged in from.
	 * @param categoryDescription The category description
	 */
	private void logFindByDescription(String ip, String categoryDescription) {
		IngredientCategoryController.logger.info(
				String.format(IngredientCategoryController.FIND_BY_CATEGORY_DESCRIPTION_MESSAGE,
						this.userInfo.getUserId(), ip, categoryDescription));
	}

	/**
	 * Logs a user's request to get Ingredient Category information based on a list of category codes.
	 *
	 * @param ip  The IP address th user is logged in from.
	 * @param categoryCodes The category codes to search on.
	 */
	private void logFindByCategoryCode(String ip, List<Long> categoryCodes) {
		IngredientCategoryController.logger.info(
				String.format(IngredientCategoryController.FIND_BY_CATEGORY_CODE_MESSAGE,
						this.userInfo.getUserId(), ip, ListFormatter.formatAsString(categoryCodes)));
	}

	/**
	 * Logs a user's request to find all category codes.
	 *
	 * @param ip The category codes to search on.
	 */
	private void logFindAll(String ip){
		IngredientCategoryController.logger.info(
				String.format(IngredientCategoryController.FIND_ALL_MESSAGE, this.userInfo.getUserId(), ip));
	}
}
