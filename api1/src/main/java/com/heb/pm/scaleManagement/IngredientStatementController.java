/*
 *  IngredientStatementController
 *
 *  Copyright (c) 2017 HEB
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
import com.heb.pm.entity.*;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.StreamingExportException;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import com.heb.util.jpa.PageableResult;
import com.heb.util.list.ListFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * REST controller that returns all information related to ingredients statements.
 * @author s573181
 * @since 2.2.0
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + IngredientStatementController.INGREDIENT_STATEMENT_URL)
@AuthorizedResource(ResourceConstants.SCALE_MANAGEMENT_INGREDIENT_STATEMENTS)
public class IngredientStatementController {

	private static final Logger logger = LoggerFactory.getLogger(IngredientCategoryController.class);

	protected static final String INGREDIENT_STATEMENT_URL = "/ingredientStatement";
	protected static final String FIND_ALL_URL = "/allIngredientStatement";
	protected static final String FIND_BY_INGREDIENT_STATEMENT_URL = "/ingredientStatement";
	protected static final String FIND_BY_INGREDIENT_CODE_URL = "/ingredientCode";
	protected static final String FIND_BY_INGREDIENT_CODE_ORDERED_URL = "/ingredientCodeOrdered";
	protected static final String FIND_BY_DESCRIPTION_URL = "/description";
	protected static final String UPDATE_INGREDIENT_STATEMENT_URL = "/updateIngredientStatement";
	protected static final String DELETE_INGREDIENT_STATEMENT_URL = "/deleteIngredientStatement";
	protected static final String ADD_INGREDIENT_STATEMENT_URL = "/addIngredientStatement";
	protected static final String INGREDIENT_STATEMENT_HITS_URL = "/hits/ingredientStatements";
	protected static final String FIND_NEXT_AVAILABLE_STATEMENT_NUMBER_URL = "/nextIngredientStatement";
	protected static final String GET_NEW_STATEMENT_WITH_INGREDIENTS = "/newIngredientStatement";
	protected static final String GET_PLUS_FOR_STATEMENT_URL = "/scaleUpc";

	// Keys to user facing messages in the message resource bundle.
	private static final String DEFAULT_NO_INGREDIENT_CD_MESSAGE = "Must search for at least one ingredient code.";
	private static final String INGREDIENT_CD_MESSAGE_KEY =
			"IngredientStatementController.missingIngredientCode";
	private static final String DEFAULT_NO_INGREDIENT_STATEMENT_MESSAGE =
			"Must search for at least one ingredient statement.";
	private static final String INGREDIENT_STATEMENT_MESSAGE_KEY =
			"IngredientStatementController.missingIngredientStatement";
	private static final String DEFAULT_ADD_SUCCESS_MESSAGE ="Ingredient statement: %s added successfully.";
	private static final String ADD_SUCCESS_MESSAGE_KEY ="IngredientStatementController.addSuccessful";
	private static final String DEFAULT_DELETE_SUCCESS_MESSAGE ="Ingredient statement: %s deleted successfully.";
	private static final String DELETE_SUCCESS_MESSAGE_KEY ="IngredientStatementController.deleteSuccessful";
	private static final String DEFAULT_UPDATE_SUCCESS_MESSAGE ="Ingredient statement: %s updated successfully.";
	private static final String UPDATE_SUCCESS_MESSAGE_KEY ="IngredientStatementController.updateSuccessful";

	// Log messages.
	private static final String FIND_ALL_MESSAGE = "User %s from IP %s requested all ingredient statements";
	private static final String FIND_BY_INGREDIENT_CODE_MESSAGE =
			"User %s from IP %s requested the ingredient statements with the following ingredient codes [%s]";
	private static final String CREATE_NEW_MESSAGE =
			"User %s from IP %s requested a new statement with the following ingredient codes [%s]";
	private static final String FIND_BY_INGREDIENT_STATEMENT_MESSAGE =
			"User %s from IP %s requested the ingredient statements with the following " +
					"ingredient statements codes [%s]";
	private static final String ADD_INGREDIENT_STATEMENT_MESSAGE = "User %s from IP %s has requested to add a " +
			"ingredient statement with the ingredients: '%s'.";
	private static final String DELETE_INGREDIENT_STATEMENT_MESSAGE = "User %s from IP %s has requested to delete the " +
			"ingredient statement with the code: %d.";
	private static final String UPDATE_INGREDIENT_STATEMENT_CODE_MESSAGE =
			"User %s from IP %s has requested to update the ingredient with the statement number: %d " +
					"with the ingredients: '%s'.";
	private static final String FIND_NEXT_AVAILABLE_STATEMENT_NUMBER_MESSAGE =
			"User %s from IP %s has requested find the next available statement number greater than or equal " +
					"to %s.";
	private static final String EXPORT_ALL_STATEMENTS_MESSAGE = "User %s from IP %s requested to download all ingredient statements";
	private static final String EXPORT_STATEMENTS_BY_DESCRIPTION_MESSAGE = "User %s from IP %s requested to download" +
			"the ingredient statements with the following description [%s]";
	private static final String EXPORT_STATEMENTS_BY_INGREDIENT_CODE_MESSAGE = "User %s from IP %s requested to download" +
			"the ingredient statements with the following ingredient codes [%s]";
	private static final String EXPORT_STATEMENTS_BY_STATEMENT_CODE_MESSAGE = "User %s from IP %s requested to download" +
			"the ingredient statements with the following statement codes [%s]";
	private static final String EXPORT_STATEMENTS_BY_ORDERED_CODES_MESSAGE = "User %s from IP %s requested to download" +
			"the ingredient statements with the following ordered ingredient codes [%s]";




	// Defaults
	private static final boolean DEFAULT_INCLUDE_COUNTS = false;
	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_PAGE_SIZE = 10;

	@Autowired
	private IngredientStatementService service;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private NonEmptyParameterValidator parameterValidator;

	private IngredientStatementHeaderResolver resolver = new IngredientStatementHeaderResolver();

	private ScaleUpcResolver scaleUpcResolver = new ScaleUpcResolver();

	/**
	 * Resolver class for sIngredientStatementHeader searches.
	 */
	private class IngredientStatementHeaderResolver implements LazyObjectResolver<IngredientStatementHeader> {

		/**
		 * Loads the first and second label format codes into the ScaleUpc.
		 * @param ish The object to resolve.
		 */
		@Override
		public void fetch(IngredientStatementHeader ish) {
			ish.getIngredientStatementDetails().size();
			for(IngredientStatementDetail isd : ish.getIngredientStatementDetails()){
				isd.getIngredient().getIngredientCode();
				isd.getIngredient().getIngredientSubs().size();
				long count;
				for(IngredientSub sub: isd.getIngredient().getIngredientSubs()){
					count = sub.getSubIngredient().getIngredientSubs().size();
					if(count > 0){
						this.getCurrentIngredientSubSize(sub.getSubIngredient());
					}
				}
			}
		}

		/**
		 * Recursion to get all sub ingredients.
		 * @param ingredient The ingredient to get sub ingredients of.
		 */
		private void getCurrentIngredientSubSize(Ingredient ingredient){
			long count;
			for(IngredientSub sub: ingredient.getIngredientSubs()){
				count = sub.getSubIngredient().getIngredientSubs().size();
				if(count > 0){
					this.getCurrentIngredientSubSize(sub.getSubIngredient());
				}
			}
		}
	}

	/**
	 * Returns a new ingredient statement and adds supplied ingredient codes to it. This is used to create a new
	 * statement based off of searched for ingredients.
	 *
	 * @param ingredientCodes The ingredient codes to add to the new statement.
	 * @param request The HTTP request that initiated this call.
	 * @return A new ingredient statement header with the requested ingredients already added.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = IngredientStatementController.GET_NEW_STATEMENT_WITH_INGREDIENTS)
	public IngredientStatementHeader createNewStatementWithIngredientCodes (
			@RequestParam(value = "ingredientCodes") String ingredientCodes,
			HttpServletRequest request) {

		List<String> ingredientCodesList = new ArrayList<>();
		Collections.addAll(ingredientCodesList, ingredientCodes.split("[\\s,]+"));

		this.parameterValidator.validate(ingredientCodes,
				IngredientStatementController.DEFAULT_NO_INGREDIENT_CD_MESSAGE,
				IngredientStatementController.INGREDIENT_CD_MESSAGE_KEY, request.getLocale());

		this.logCreateNewStatement(request.getRemoteAddr(), ingredientCodesList);

		IngredientStatementHeader header = this.service.createNewHeaderWithCodes(ingredientCodesList);
		this.resolver.fetch(header);

		return header;
	}



	/**
	 * Finds a page of ingredient statements that contain all the ingredient codes in a list.
	 *
	 * @param ingredientCodes The list of ingredient codes to search on.
	 * @param includeCounts Whether or not to include total record counts.
	 * @param page The page of data to return.
	 * @param pageSize The number of records to return.
	 * @param request The HTTP request that initiated this call.
	 * @return A page of ingredient statements with specific codes.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = IngredientStatementController.FIND_BY_INGREDIENT_CODE_URL)
	public PageableResult<IngredientStatementHeader> findByIngredientCodes(
			@RequestParam(value = "ingredientCodes") String ingredientCodes,
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			HttpServletRequest request) {

		List<String> ingredientCodesList = new ArrayList<>();
		Collections.addAll(ingredientCodesList, ingredientCodes.split("[\\s,]+"));

		this.parameterValidator.validate(ingredientCodes,
				IngredientStatementController.DEFAULT_NO_INGREDIENT_CD_MESSAGE,
				IngredientStatementController.INGREDIENT_CD_MESSAGE_KEY, request.getLocale());

		this.logFindByIngredientCodes(request.getRemoteAddr(), ingredientCodesList);

		boolean ic = includeCounts == null ? IngredientStatementController.DEFAULT_INCLUDE_COUNTS : includeCounts;
		int pg = page == null ? IngredientStatementController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? IngredientStatementController.DEFAULT_PAGE_SIZE : pageSize;

		PageableResult<IngredientStatementHeader> result = this.service.findByIngredientCode(
				ingredientCodesList, ic, pg, ps);

		result.getData().forEach(this.resolver::fetch);
		return result;
	}

	/**
	 * Finds a page of ingredient statements that have requested ingredient codes as the first codes on the list.
	 *
	 * @param ingredientCodes The list of ingredient codes to search on.
	 * @param includeCounts Whether or not to include total record counts.
	 * @param page The page of data to return.
	 * @param pageSize The number of records to return.
	 * @param request The HTTP request that initiated this call.
	 * @return A page of ingredient statements with specific codes in the specified order.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET,
			value = IngredientStatementController.FIND_BY_INGREDIENT_CODE_ORDERED_URL)
	public PageableResult<IngredientStatementHeader> findByOrderedIngredientCodes(
			@RequestParam(value = "ingredientCodes") String ingredientCodes,
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			HttpServletRequest request) {

		List<String> ingredientCodesList = new ArrayList<>();
		Collections.addAll(ingredientCodesList, ingredientCodes.split("[\\s,]+"));

		this.parameterValidator.validate(ingredientCodes,
				IngredientStatementController.DEFAULT_NO_INGREDIENT_CD_MESSAGE,
				IngredientStatementController.INGREDIENT_CD_MESSAGE_KEY, request.getLocale());

		this.logFindByIngredientCodes(request.getRemoteAddr(), ingredientCodesList);

		boolean ic = includeCounts == null ? IngredientStatementController.DEFAULT_INCLUDE_COUNTS : includeCounts;
		int pg = page == null ? IngredientStatementController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? IngredientStatementController.DEFAULT_PAGE_SIZE : pageSize;

		PageableResult<IngredientStatementHeader> result = this.service.findByOrderedIngredientCodes(
				ingredientCodesList, ic, pg, ps);
		result.getData().forEach(this.resolver::fetch);
		return result;
	}

	/**
	 * Finds a page of ingredient statements by ingredient statements number.
	 *
	 * @param ingredientStatements The list of ingredient statements to search on.
	 * @param includeCounts Whether or not to include total record counts.
	 * @param page The page of data to return.
	 * @param pageSize The number of records to return.
	 * @param request The HTTP request that initiated this call.
	 * @return A page of label formats without constraint.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = IngredientStatementController.FIND_BY_INGREDIENT_STATEMENT_URL)
	public PageableResult<IngredientStatementHeader> findByIngredientStatements(
			@RequestParam(value = "ingredientStatements") List<Long> ingredientStatements,
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			HttpServletRequest request) {
		this.parameterValidator.validate(ingredientStatements,
				IngredientStatementController.DEFAULT_NO_INGREDIENT_STATEMENT_MESSAGE,
				IngredientStatementController.INGREDIENT_STATEMENT_MESSAGE_KEY, request.getLocale());
		this.logFindByIngredientStatements(request.getRemoteAddr(), ingredientStatements);

		boolean ic = includeCounts == null ? IngredientStatementController.DEFAULT_INCLUDE_COUNTS : includeCounts;
		int pg = page == null ? IngredientStatementController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? IngredientStatementController.DEFAULT_PAGE_SIZE : pageSize;

		PageableResult<IngredientStatementHeader> result = this.service.findByIngredientStatement(
				ingredientStatements,ic, pg, ps);
		result.getData().forEach(this.resolver::fetch);
		return result;
	}

	/**
	 * Finds a page of ingredient statements by their ingredients description.
	 *
	 * @param description The description of the ingredients to search on.
	 * @param includeCounts Whether or not to include total record counts.
	 * @param page The page of data to return.
	 * @param pageSize The number of records to return.
	 * @param request The HTTP request that initiated this call.
	 * @return A page of label formats without constraint.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = IngredientStatementController.FIND_BY_DESCRIPTION_URL)
	public PageableResult<IngredientStatementHeader> findByDescription(
			@RequestParam(value = "description") String description,
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			HttpServletRequest request) {


		if(description == null){
			throw new IllegalArgumentException("Description cannot be null");
		}
		this.logFindStatementByIngredientDescription(description, request.getRemoteAddr());
		boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
		int pg = page == null ? IngredientStatementController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? IngredientStatementController.DEFAULT_PAGE_SIZE : pageSize;

		PageableResult<IngredientStatementHeader> result =  this.service.findByDescription(description, ic, pg, ps);
		result.getData().forEach(this.resolver::fetch);
		return result;
	}

	/**
	 * Finds a page of label formats without constraint.
	 *
	 * @param includeCounts Whether or not to include total record counts.
	 * @param page The page of data to return.
	 * @param pageSize The number of records to return.
	 * @param request The HTTP request that initiated this call.
	 * @return A page of label formats without constraint.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = IngredientStatementController.FIND_ALL_URL)
	public PageableResult<IngredientStatementHeader> findAll(
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			HttpServletRequest request) {

		this.logFindAllRequest(request.getRemoteAddr());

		boolean ic = includeCounts == null ? IngredientStatementController.DEFAULT_INCLUDE_COUNTS : includeCounts;
		int pg = page == null ? IngredientStatementController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? IngredientStatementController.DEFAULT_PAGE_SIZE : pageSize;

		PageableResult<IngredientStatementHeader> result = this.service.findAll(ic, pg, ps);
		result.getData().forEach(this.resolver::fetch);
		return result;
	}

	/**
	 * Adds a new IngredientStatementHeader.
	 *
	 * @param ingredientStatement The ingredient statement to be added.
	 * @param request the HTTP request that initiated this call.
	 * @return The new IngredientStatementHeader and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST,  value = IngredientStatementController.ADD_INGREDIENT_STATEMENT_URL)
	public ModifiedEntity<IngredientStatementHeader> addIngredientStatementHeader(
			@RequestBody IngredientStatementHeader ingredientStatement, HttpServletRequest request) {

		this.logAdd(request.getRemoteAddr(), ingredientStatement.getIngredientsText());

		IngredientStatementHeader statement =  this.service.add(ingredientStatement);

		String updateMessage = this.messageSource.getMessage(IngredientStatementController.ADD_SUCCESS_MESSAGE_KEY,
				new Object[]{statement.getStatementNumber()}, IngredientStatementController.DEFAULT_ADD_SUCCESS_MESSAGE,
				request.getLocale());
		return new ModifiedEntity<>(statement, updateMessage);
	}

	/**
	 * Adds a new IngredientStatementHeader.
	 *
	 * @param statementCode The ingredient statement to be deleted's code.
	 * @param request the HTTP request that initiated this call.
	 * @return The new IngredientStatementHeader and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.DELETE,value = IngredientStatementController.DELETE_INGREDIENT_STATEMENT_URL)
	public ModifiedEntity<Long> deleteIngredientStatement(@RequestParam Long statementCode,
														  @RequestParam(value = "deptList", required = false) List<Long> deptList,
														  HttpServletRequest request) {

		this.logDelete(request.getRemoteAddr(),statementCode);

		this.service.delete(deptList, statementCode);

		String updateMessage = this.messageSource.getMessage(IngredientStatementController.DELETE_SUCCESS_MESSAGE_KEY,
				new Object[]{statementCode}, IngredientStatementController.DEFAULT_DELETE_SUCCESS_MESSAGE,
				request.getLocale());
		return new ModifiedEntity<>(statementCode, updateMessage);
	}

	/**
	 * Adds a new IngredientStatementHeader.
	 *
	 * @param statement The ingredient statement to be updated.
	 * @param request the HTTP request that initiated this call.
	 * @return The new IngredientStatementHeader and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST,  value = IngredientStatementController.UPDATE_INGREDIENT_STATEMENT_URL)
	public ModifiedEntity<IngredientStatementHeader> update(
			@RequestBody IngredientStatementHeader statement, HttpServletRequest request){
		this.logUpdate(request.getRemoteAddr(), statement);
		IngredientStatementHeader savedStatement = this.service.update(statement);
		String updateMessage = this.messageSource.getMessage(IngredientStatementController.UPDATE_SUCCESS_MESSAGE_KEY,
				new Object[]{statement.getStatementNumber()},
				IngredientStatementController.DEFAULT_UPDATE_SUCCESS_MESSAGE, request.getLocale());

		return new ModifiedEntity<>(savedStatement, updateMessage);
	}

	/**
	 * Finds the hits when searching for a list of ingredient statement numbers.
	 *
	 * @param ingredientStatements a list of ingredient statement numbers.
	 * @param request the HTTP request that initiated this call.
	 * @return the hits.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = IngredientStatementController.INGREDIENT_STATEMENT_HITS_URL)
	public Hits findHitsByIngredientStatementCode(@RequestParam("ingredientStatements") List<Long> ingredientStatements,
												  HttpServletRequest request){
		this.parameterValidator.validate(ingredientStatements,
				IngredientStatementController.DEFAULT_NO_INGREDIENT_STATEMENT_MESSAGE,
				IngredientStatementController.INGREDIENT_STATEMENT_MESSAGE_KEY, request.getLocale());
		this.logFindByIngredientStatements(request.getRemoteAddr(), ingredientStatements);
		return this.service.findHitsByIngredientStatementCodes(ingredientStatements);
	}

	/**
	 * Returns a list of scale upc that have a particular ingredient statement number.
	 *
	 * @param ingredientStatementNumber The ingredient statement number to be searched for.
	 * @param request The HTTP request that initiated this call.
	 * @return A list of scale upc data.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = IngredientStatementController.GET_PLUS_FOR_STATEMENT_URL)
	public PageableResult<ScaleUpc> findScaleUpcByIngredientStatementNumber(
			@RequestParam(value = "ingredientStatement", required = true) Long ingredientStatementNumber,
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			HttpServletRequest request){

		IngredientStatementController.logger.info(
				String.format("User %s from IP %s requested a list of all PLUs with the ingredient statement number %d",
						this.userInfo.getUserId(), request.getRemoteAddr(), ingredientStatementNumber)
		);

		boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
		int pg = page == null ? DEFAULT_PAGE : page;

		PageableResult<ScaleUpc> result = this.service.findByIngredientStatementNumber(ingredientStatementNumber,
				ic, pg, pageSize);
		result.getData().forEach(this.scaleUpcResolver::fetch);
		return result;
	}

	/**
	 * Finds the next available statement number greater than or equal to the number entered.
	 * @param statementNumber The statement number to look for.
	 * @param request the HTTP request that initiated this call.
	 * @return The next available statement number.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.GET,
			value = IngredientStatementController.FIND_NEXT_AVAILABLE_STATEMENT_NUMBER_URL)
	public List<Long> findNextAvailableStatementNumber(@RequestParam("statementNumber") Long statementNumber,
													   HttpServletRequest request){

		this.logFindNextAvailableStatementNumber(request.getRemoteAddr(), statementNumber);

		return  this.service.findNextAvailableStatementNumber(statementNumber);
	}


	/**
	 * Logs a users request for all ingredient statements.
	 *
	 * @param ipAddress The IP address the user is logged in from.
	 */
	private void logFindAllRequest(String ipAddress) {
		IngredientStatementController.logger.info(String.format(IngredientStatementController.FIND_ALL_MESSAGE,
				this.userInfo.getUserId(), ipAddress));
	}

	/**
	 * Logs a users request for all ingredient statements.
	 *
	 * @param ipAddress The IP address of the logged in user.
	 * @param ingredientCodes The ingredient codes they searched for.
	 */
	private void logFindByIngredientCodes(String ipAddress, List<String> ingredientCodes) {
		IngredientStatementController.logger.info(String.format(
				IngredientStatementController.FIND_BY_INGREDIENT_CODE_MESSAGE, this.userInfo.getUserId(),
				ipAddress, ListFormatter.formatAsString(ingredientCodes)));
	}

	/**
	 * Logs a users request for all ingredient statements.
	 *
	 * @param ipAddress The IP address of the logged in user.
	 * @param ingredientCodes The ingredient statements they searched for.
	 */
	private void logFindByIngredientStatements(String ipAddress, List<Long> ingredientCodes) {
		IngredientStatementController.logger.info(String.format(
				IngredientStatementController.FIND_BY_INGREDIENT_STATEMENT_MESSAGE, this.userInfo.getUserId(),
				ipAddress, ListFormatter.formatAsString(ingredientCodes)));
	}

	/**
	 * Logs a users request for statement headers by ingredient description.
	 *
	 * @param ipAddress The IP address of the logged in user.
	 * @param description The ingredient description they searched on.
	 */
	private void logFindStatementByIngredientDescription(String ipAddress, String description) {
		IngredientStatementController.logger.info(String.format(
				IngredientStatementController.FIND_BY_INGREDIENT_STATEMENT_MESSAGE, this.userInfo.getUserId(),
				ipAddress, description));
	}

	/**
	 * Logs a users request for all ingredient statements.
	 *
	 * @param ipAddress The IP address of the logged in user.
	 * @param ingredientsText The ingredient text.
	 */
	private void logAdd(String ipAddress, String ingredientsText){
		IngredientStatementController.logger.info
				(String.format(IngredientStatementController.ADD_INGREDIENT_STATEMENT_MESSAGE,
						this.userInfo.getUserId(), ipAddress, ingredientsText));
	}

	/**
	 * Logs a user's request to delete an ingredientStatement.
	 *
	 * @param ipAddress The IP address the user is logged in from.
	 * @param ingredientStatement The ingredientStatement to be deleted.
	 */
	private void logDelete(String ipAddress, Long ingredientStatement){
		IngredientStatementController.logger.info(String.format(
				IngredientStatementController.DELETE_INGREDIENT_STATEMENT_MESSAGE,
				this.userInfo.getUserId(), ipAddress, ingredientStatement));
	}

	/**
	 * Logs a user's request to update a scale action code.
	 *
	 * @param ipAddress The IP address the user is logged in from.
	 * @param statementHeader The IngredientStatementHeader to be updated.
	 */
	private void logUpdate(String ipAddress, IngredientStatementHeader statementHeader){
		IngredientStatementController.logger.info(String.format(
				IngredientStatementController.UPDATE_INGREDIENT_STATEMENT_CODE_MESSAGE,  this.userInfo.getUserId(),
				ipAddress, statementHeader.getStatementNumber(), statementHeader.getIngredientsText()));
	}

	/**
	 * Logs a user's request to find the next available statement number.
	 *
	 * @param ipAddress The IP address the user is logged in from.
	 * @param statementNumber The statement number searched upon.
	 */
	private void logFindNextAvailableStatementNumber(String ipAddress, Long statementNumber){
		IngredientStatementController.logger.info(String.format(
				IngredientStatementController.FIND_NEXT_AVAILABLE_STATEMENT_NUMBER_MESSAGE,  this.userInfo.getUserId(),
				ipAddress, statementNumber));
	}

	/**
	 * Logs an attempt to create a new, blank ingredient statement with a list of ingredient codes.
	 *
	 * @param ipAddress The IP address the user is logged in from.
	 * @param ingredientCodes The codes to add to the ingredient statement.
	 */
	private void logCreateNewStatement(String ipAddress, List<String> ingredientCodes) {
		IngredientStatementController.logger.info(String.format(
				IngredientStatementController.CREATE_NEW_MESSAGE, this.userInfo.getUserId(),
				ipAddress, ListFormatter.formatAsString(ingredientCodes)));
	}

	/**
	 * Logs an attempt to create a new, blank ingredient statement with a list of ingredient codes.
	 * @param ipAddress The IP address the user is logged in from.
	 * @param statementCodes The codes to add to the ingredient statement.
	 */
	private void logExportStatementByStatementCode(String ipAddress, String statementCodes) {
		IngredientStatementController.logger.info(String.format(
				IngredientStatementController.EXPORT_STATEMENTS_BY_STATEMENT_CODE_MESSAGE, this.userInfo.getUserId(),
				ipAddress, statementCodes));
	}

	/**
	 * Logs an attempt to create a new, blank ingredient statement with a list of ingredient codes.
	 * @param ipAddress The IP address the user is logged in from.
	 * @param ingredientCodes The codes to add to the ingredient statement.
	 */
	private void logExportStatementByIngredientCode(String ipAddress, String ingredientCodes) {
		IngredientStatementController.logger.info(String.format(
				IngredientStatementController.EXPORT_STATEMENTS_BY_INGREDIENT_CODE_MESSAGE, this.userInfo.getUserId(),
				ipAddress, ingredientCodes));
	}

	/**
	 * Logs an attempt to create a new, blank ingredient statement with a list of ingredient codes.
	 * @param ipAddress The IP address the user is logged in from.
	 * @param orderedCodes The codes to add to the ingredient statement.
	 */
	private void logExportStatementByOrderedCodes(String ipAddress, String orderedCodes) {
		IngredientStatementController.logger.info(String.format(
				IngredientStatementController.EXPORT_STATEMENTS_BY_ORDERED_CODES_MESSAGE, this.userInfo.getUserId(),
				ipAddress, orderedCodes));
	}

	/**
	 * Logs an attempt to create a new, blank ingredient statement with a list of ingredient codes.
	 * @param ipAddress The IP address the user is logged in from.
	 * @param description The codes to add to the ingredient statement.
	 */
	private void logExportStatementByDescription(String ipAddress, String description) {
		IngredientStatementController.logger.info(String.format(
				IngredientStatementController.EXPORT_STATEMENTS_BY_DESCRIPTION_MESSAGE, this.userInfo.getUserId(),
				ipAddress, description));
	}

	/**
	 * Logs an attempt to create a new, blank ingredient statement with a list of ingredient codes.
	 * @param ipAddress The IP address the user is logged in from.
	 */
	private void logExportAllStatements(String ipAddress) {
		IngredientStatementController.logger.info(String.format(
				IngredientStatementController.EXPORT_ALL_STATEMENTS_MESSAGE, this.userInfo.getUserId(),
				ipAddress));
	}

    /**
     * Calls for the service to stream the results of a statement code-based search to a CSV file
     * @param code the statement code to be used as the search key
     * @param downloadId the download Id
     * @param request the request
     * @param response the response
     */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "exportByStatementCode", headers = "Accept=text/csv" )
	public void exportIngredientStatementsByStatementCode(@RequestParam String code,
														   @RequestParam(value = "downloadId", required = false) String downloadId,
														   HttpServletRequest request, HttpServletResponse response){
        logExportStatementByStatementCode(request.getRemoteAddr(), code);

		List<String> stringList = new ArrayList<>();
        Collections.addAll(stringList, code.split("[\\s,]+"));
        List<Long> statementCodeList = new ArrayList<>();
        for(String data: stringList){
            statementCodeList.add(Long.parseLong(data));
        }


        if (downloadId != null) {
            Cookie c = new Cookie(downloadId, downloadId);
            c.setPath("/");
            response.addCookie(c);
        }
        try{
            this.service.streamStatementByStatementCode(response.getOutputStream(), statementCodeList);
        } catch (IOException e) {
            throw new StreamingExportException(e.getMessage(), e.getCause());
        }
	}

    /**
     * Calls for the service to stream the results of an ingredient code-based search to a CSV file
     * @param ingredientCode the ingredient code to be used as the search key
     * @param downloadId the download Id
     * @param request the request
     * @param response the response
     */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "exportByIngredientCode", headers = "Accept=text/csv" )
	public void exportIngredientStatementsByIngredientCode(@RequestParam String ingredientCode,
														   @RequestParam(value = "downloadId", required = false) String downloadId,
														   HttpServletRequest request, HttpServletResponse response){
        logExportStatementByIngredientCode(request.getRemoteAddr(), ingredientCode);

		List<String> codeList = new ArrayList<>();
        Collections.addAll(codeList, ingredientCode.split("[\\s,]+"));


        if (downloadId != null) {
            Cookie c = new Cookie(downloadId, downloadId);
            c.setPath("/");
            response.addCookie(c);
        }
        try{
            this.service.streamStatementByIngredientCode(response.getOutputStream(), codeList);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new StreamingExportException(e.getMessage(), e.getCause());
        }
	}


    /**
     * Calls for the service to stream the results of an ordered code-based search to a CSV file
     * @param orderedCodes the series of ordered ingredient codes to be used as the search key
     * @param downloadId the download Id
     * @param request the request
     * @param response the response
     */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "exportByOrderedCodes", headers = "Accept=text/csv" )
	public void exportIngredientStatementsByOrderedCodes(@RequestParam String orderedCodes,
														   @RequestParam(value = "downloadId", required = false) String downloadId,
														   HttpServletRequest request, HttpServletResponse response){
        logExportStatementByOrderedCodes(request.getRemoteAddr(), orderedCodes);

		List<String> codeList = new ArrayList<>();
        Collections.addAll(codeList, orderedCodes.split("[\\s,]+"));


        if (downloadId != null) {
            Cookie c = new Cookie(downloadId, downloadId);
            c.setPath("/");
            response.addCookie(c);
        }
        try{
            this.service.streamStatementByOrderedCodes(response.getOutputStream(), codeList);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new StreamingExportException(e.getMessage(), e.getCause());
        }
	}


    /**
     * Calls for the service to stream all IngredientStatements to a CSV file
     * @param downloadId the download Id
     * @param request the request
     * @param response the response
     */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "exportAll", headers = "Accept=text/csv" )
	public void exportAllIngredientStatements(@RequestParam(value = "downloadId", required = false) String downloadId, HttpServletRequest request, HttpServletResponse response){
        logExportAllStatements(request.getRemoteAddr());

		if (downloadId != null) {
            Cookie c = new Cookie(downloadId, downloadId);
            c.setPath("/");
            response.addCookie(c);
        }

        try{
            this.service.streamAllStatements(response.getOutputStream());
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new StreamingExportException(e.getMessage(), e.getCause());
        }
	}

    /**
     * Calls for the service to stream the results of a description-based search to a CSV file
     * @param description the description to be used as the search key
     * @param downloadId the download Id
     * @param request the request
     * @param response the response
     */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "exportByDescription", headers = "Accept=text/csv" )
	public void exportIngredientStatementListByDescription(@RequestParam String description,
														   @RequestParam(value = "downloadId", required = false) String downloadId,
																				   HttpServletRequest request, HttpServletResponse response){
		logExportStatementByDescription(request.getRemoteAddr(), description);

		if (downloadId != null) {
			Cookie c = new Cookie(downloadId, downloadId);
			c.setPath("/");
			response.addCookie(c);
		}

		try{
			this.service.streamStatementByDescription(response.getOutputStream(), description);
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new StreamingExportException(e.getMessage(), e.getCause());
		}
	}
}