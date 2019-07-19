package com.heb.pm.scaleManagement;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.Hits;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.Nutrient;
import com.heb.pm.entity.NutrientRoundingRule;
import com.heb.pm.entity.NutrientStatementHeader;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.NonEmptyParameterValidator;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import com.heb.util.jpa.PageableResult;
import com.heb.util.list.ListFormatter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * REST endpoint for accessing Nutrient information.
 * @author m594201
 * @since 2.1.0
 */
@RestController
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + NutrientsController.NUTRIENTS_URL)
@AuthorizedResource(ResourceConstants.SCALE_MANAGEMENT_NUTRIENTS)
public class NutrientsController {

	private static final Logger logger = LoggerFactory.getLogger(NutrientsController.class);

	/**
	 * The constant NUTRIENTS_URL.
	 */
	protected static final String NUTRIENTS_URL = "/nutrients";

	private static final String NUTRIENT_CD_MESSAGE_KEY = "NutrientsController.missingNutrientCode";
	private static final String DEFAULT_NO_NUTRIENT_CD_MESSAGE = "Must search for at least one nutrient code.";
	private static final String UPDATE_SUCCESS_MESSAGE_KEY ="NutrientsController.updateSuccessful";
	private static final String DEFAULT_UPDATE_SUCCESS_MESSAGE ="Nutrient: %s updated successfully.";
	private static final String DEFAULT_ADD_SUCCESS_MESSAGE ="Nutrient Code: %s added successfully.";
	private static final String ADD_SUCCESS_MESSAGE_KEY ="NutrientsController.addSuccessful";
	private static final String DEFAULT_UPDATE_ROUNDING_RULE_SUCCESS_MESSAGE ="Nutrient code: %d rounding rules" +
			" updated successfully.";
	private static final String DEFAULT_DELETE_SUCCESS_MESSAGE ="Nutrient Code: %s deleted successfully.";
	private static final String DELETE_SUCCESS_MESSAGE_KEY ="NutrientsController.deleteSuccessful";
	// Keys to user facing messages in the message resource bundle.
	private static final String UPDATE_ROUNDING_RULE_SUCCESS_MESSAGE_KEY ="NutrientsController.updateRoundingRulesSuccessful";

	//log messages
	private static final String FIND_BY_NUTRIENT_CODE_MESSAGE =
			"User %s from IP %s has requested nutrient data for the following NutrientCodes: [%s]";
	private static final String FIND_BY_DESCRIPTION_MESSAGE =
			"User %s from IP %s has requested nutrient data containing the wildcard description: %s";
	private static final String FIND_ROUNDING_RULES_BY_NUTRIENT_CODE_MESSAGE =
			"User %s from IP %s has requested rounding rules data for the following nutrient code %s";
	private static final String UPDATE_ROUNDING_RULE_MESSAGE = "User %s from IP %s has requested to update the " +
			"rounding rules for the nutrient code: %d.";
	private static final String ADD_SCALE_NUTRIENT_CODE_MESSAGE = "User %s from IP %s has requested to add a " +
			"new nutrient: '%s'.";
	private static final String DELETE_SCALE_NUTRIENT_CODE_MESSAGE = "User %s from IP %s has requested to delete the " +
			"ScaleNutrientCode with the nutrient code: %d.";
	private static final String UPDATE_NUTRIENT_LOG_MESSAE =
			"User %s from IP %s has requested to update nutrient %d with %s";


	private static final int DEFAULT_PAGE = 0;
	private static final int DEFAULT_PAGE_SIZE = 20;

	@Autowired
	private UserInfo userInfo;

	@Autowired
	private NutrientsService nutrientsService;

	@Autowired
	private MessageSource messageSource;

	private LazyObjectResolver<NutrientStatementHeader> nutrientStatementHeaderResolver = new NutrientStatementHeaderResolver();

	@Autowired private
	NonEmptyParameterValidator parameterValidator;

	/**
	 * Find all by nutrient code pageable result.
	 *
	 * @param nutrientCodes the nutrient codes
	 * @param page          the page
	 * @param includeCounts the include counts
	 * @param request       the request
	 * @return the pageable result
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "nutrientCodes")
	public PageableResult<Nutrient> findAllByNutrientCode(
			@RequestParam("nutrientCodes")List<Long> nutrientCodes,
			@RequestParam(value = "page", required = false) Integer page,
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "pageSize", required = false) Integer pageSize,
			HttpServletRequest request){

		logger.debug("In NutrientCode Controller");

		boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
		int pg = page == null ? NutrientsController.DEFAULT_PAGE : page;
		int ps =  pageSize == null ? NutrientsController.DEFAULT_PAGE_SIZE : pageSize;

		NutrientsController.logger.info(String.format(NutrientsController.FIND_BY_NUTRIENT_CODE_MESSAGE,
				this.userInfo.getUserId(), request.getRemoteAddr(), nutrientCodes));

		return this.nutrientsService.findByNutrientCode(nutrientCodes, ic, pg, ps);
	}

	/**
	 * Find all by description pageable result.
	 *
	 * @param nutrientDescription the nutrient description
	 * @param page                the page
	 * @param includeCounts       the include counts
	 * @param request             the request
	 * @return the pageable result
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "nutrientDescription")
	public PageableResult<Nutrient> findAllByDescription(@RequestParam("nutrientDescription") String nutrientDescription,
														 @RequestParam(value = "page", required = false) Integer page,
														 @RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
														 HttpServletRequest request){

		List<String> stringArray= new ArrayList<>();

		Collections.addAll(stringArray, nutrientDescription.split("\\s*[,]\\s*"));

		boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
		int pg = page == null ? NutrientsController.DEFAULT_PAGE : page;
		int ps =  NutrientsController.DEFAULT_PAGE_SIZE;

		NutrientsController.logger.info(String.format(NutrientsController.FIND_BY_DESCRIPTION_MESSAGE, this.userInfo.getUserId(),
				request.getRemoteAddr(), nutrientDescription));

		return this.nutrientsService.findByNutrientDescription(stringArray, ic, pg, ps);
	}

	/**
	 * Find all nutrient codes pageable result.
	 *
	 * @param page          the page
	 * @param includeCounts the include counts
	 * @param request       the request
	 * @return the pageable result
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "queryAllNutrientCodes")
	public PageableResult<Nutrient> findAllNutrientCodes(@RequestParam(value = "page", required = false) Integer page,
									@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
									@RequestParam(value = "pageSize", required = false) Integer pageSize,
									HttpServletRequest request){

		NutrientsController.logger.info(String.format(NutrientsController.FIND_BY_NUTRIENT_CODE_MESSAGE,
				this.userInfo.getUserId(), request.getRemoteAddr(), "All Nutrient Codes"));

		int pg = page == null ? NutrientsController.DEFAULT_PAGE : page;
		int ps = pageSize == null ? NutrientsController.DEFAULT_PAGE_SIZE : pageSize;

		return this.nutrientsService.findAll(includeCounts, pg, ps);
	}

	/**
	 * Finds rounding rules by the nutrient code.
	 *
	 * @param nutrientCode the nutrient code.
	 * @param request      the HTTP request that initiated this call.
	 * @return a List of rounding rules.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "nutrientRoundingRules")
	public List<NutrientRoundingRule> findNutrientRoundingRulesByNutrientCode(
			@RequestParam("nutrientCode") int nutrientCode, HttpServletRequest request){
		this.logFindRoundingRulesByNutrientCode(request.getRemoteAddr(), nutrientCode);
		return this.nutrientsService.findNutrientRoundingRulesByNutrientCode(nutrientCode);
	}

	/**
	 * Updates the rounding rules.
	 *
	 * @param rules   A list containing two lists, the old rules first, and then new rules to be added.
	 * @param request the HTTP request that initiated this call.
	 * @return The updated rounding rule and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.PUT, value = "updateRoundingRules")
	public ModifiedEntity<List<NutrientRoundingRule>> updateRoundingRules(
			@RequestBody List<List<NutrientRoundingRule>> rules, HttpServletRequest request){
		List<NutrientRoundingRule> newRules = rules.get(0);
		List<NutrientRoundingRule> oldRules = null;
		if(rules.size() > 1) {
			oldRules = rules.get(1);
		}
		this.logUpdateRoundingRules(request.getRemoteAddr(), newRules.get(0).getKey().getNutrientCode());
		String errorMessage = this.nutrientsService.update(newRules, oldRules);
		if(errorMessage == null) {
			String updateMessage = this.messageSource.getMessage(
					NutrientsController.UPDATE_ROUNDING_RULE_SUCCESS_MESSAGE_KEY,
					new Object[]{newRules.get(0).getKey().getNutrientCode()},
					NutrientsController.DEFAULT_UPDATE_ROUNDING_RULE_SUCCESS_MESSAGE, request.getLocale());
			return new ModifiedEntity<>(newRules, updateMessage);

		} else {
			return new ModifiedEntity<>(newRules, errorMessage);
		}
	}

	/**
	 * Logs a user's request to update a rounding rule.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param nutrientCode The nutrient code of the rounding rule to be updated.
	 */
	private void logUpdateRoundingRules(String ip, int nutrientCode) {
		NutrientsController.logger.info(String.format(NutrientsController.UPDATE_ROUNDING_RULE_MESSAGE,
				this.userInfo.getUserId(), ip, nutrientCode));
	}

	/**
	 * Update nutrition data modified entity.
	 *
	 * @param nutrient the nutrient
	 * @param request  the request
	 * @return the modified entity
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.PUT, value = "updateNutritionData")
	public ModifiedEntity<Nutrient>  updateNutritionData(@RequestBody Nutrient nutrient, HttpServletRequest request){

		this.logUpdateNutrient(request.getRemoteAddr(), nutrient);

		Nutrient updatedNutrient = this.nutrientsService.updateNutrient(nutrient);

		String updateMessage = this.messageSource.getMessage(NutrientsController.UPDATE_SUCCESS_MESSAGE_KEY,
				new Object[]{nutrient.getNutrientCode()}, String.format(NutrientsController.DEFAULT_UPDATE_SUCCESS_MESSAGE, nutrient.getNutrientDescription()),
				request.getLocale());

		return new ModifiedEntity<>(updatedNutrient, updateMessage);

	}

	/**
	 * Adds a new Scale Nutrient Code.
	 *
	 * @param nutrient the nutrient
	 * @param request  the HTTP request that initiated this call.
	 * @return The new nutrient and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.POST, value = "addNutrientData")
	public ModifiedEntity<Nutrient> addScaleNutrientCode(@RequestBody Nutrient nutrient,
															  HttpServletRequest request) {

		this.logAdd(request.getRemoteAddr(), nutrient);

		Nutrient newNutrient =  this.nutrientsService.addNutrient(nutrient);

		String updateMessage = this.messageSource.getMessage(NutrientsController.ADD_SUCCESS_MESSAGE_KEY,
				new Object[]{newNutrient.getNutrientCode()}, NutrientsController.DEFAULT_ADD_SUCCESS_MESSAGE,
				request.getLocale());
		return new ModifiedEntity<>(newNutrient, updateMessage);
	}

	/**
	 * Deletes a ScaleNutrientCode.
	 *
	 * @param nutrientCode The ScaleNutrientCode nutrientCode to be deleted.
	 * @param request      the HTTP request that initiated this call.
	 * @return The deleted ScaleActionCode and a message for the front end.
	 */
	@EditPermission
	@RequestMapping(method = RequestMethod.DELETE, value = "deleteNutrientData")
	public ModifiedEntity<Long> deleteScaleNutrientCode(@RequestParam Long nutrientCode,
													  HttpServletRequest request){
		this.logDelete(request.getRemoteAddr(), nutrientCode);
		this.nutrientsService.deleteNutrient(nutrientCode);
		String updateMessage = this.messageSource.getMessage(NutrientsController.DELETE_SUCCESS_MESSAGE_KEY,
				new Object[]{nutrientCode}, NutrientsController.DEFAULT_DELETE_SUCCESS_MESSAGE, request.getLocale());

		return new ModifiedEntity<>(nutrientCode, updateMessage);
	}

	/**
	 * Returns the Hits result with found and not found action codes.
	 *
	 * @param nutrientCodes The nutrient code to search on.
	 * @param request       the HTTP request that initiated this call.
	 * @return Hits result with found and not found action codes.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "hits/nutrientCodes")
	public Hits findHitsByNutrientCode(@RequestParam("nutrientCodes") List<Long> nutrientCodes, HttpServletRequest request){
		this.parameterValidator.validate(nutrientCodes, NutrientsController.DEFAULT_NO_NUTRIENT_CD_MESSAGE,
				NutrientsController.NUTRIENT_CD_MESSAGE_KEY, request.getLocale());
		this.logFindByNutritionCodes(request.getRemoteAddr(), nutrientCodes);
		return this.nutrientsService.findHitsByNutrientCodeList(nutrientCodes);
	}

	/**
	 * Search for available nutrient statement string.
	 *
	 * @param nutrientStatementCode The code to search for.
	 * @param request The HTTP request that initiated this call.
	 * @return the string
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "searchForAvailableNutrientCode")
	public String searchForAvailableNutrientCode(@RequestParam("nutrientCode") Long nutrientStatementCode, HttpServletRequest request) {

		return this.nutrientsService.searchForAvailableNutrientCode(nutrientStatementCode);
	}

	/**
	 * Logs a user's request to get all records for multiple action codes.
	 *
	 * @param ip The IP address th user is logged in from.
	 * @param nutrientCodes The action codes list the user is searching for.
	 */
	private void logFindByNutritionCodes(String ip, List<Long> nutrientCodes) {
		NutrientsController.logger.info(
				String.format(NutrientsController.FIND_BY_NUTRIENT_CODE_MESSAGE,
						this.userInfo.getUserId(), ip, ListFormatter.formatAsString(nutrientCodes)));
	}

	/**
	 * Log's a user's request to get all records matching a description.
	 *
	 * @param ip The IP address th user is logged in from.
	 * @param description The description the user is searching for.
	 */
	private void logFindByDescription(String ip, String description) {
		NutrientsController.logger.info(
				String.format(NutrientsController.FIND_BY_DESCRIPTION_MESSAGE,
						this.userInfo.getUserId(), ip, description));
	}

	/**
	 * Logs a user's request to add a scale action code.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param nutrient The nutrient information the user is trying to add.
	 */
	private void logAdd(String ip, Nutrient nutrient){
		NutrientsController.logger.info(String.format(NutrientsController.ADD_SCALE_NUTRIENT_CODE_MESSAGE,
				this.userInfo.getUserId(), ip, nutrient));
	}

	/**
	 * Logs a user's request to delete a scale action code.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param nutrientCode The scale nutrient code to be deleted.
	 */
	private void logDelete(String ip, Long nutrientCode){
		NutrientsController.logger.info(String.format(NutrientsController.DELETE_SCALE_NUTRIENT_CODE_MESSAGE,
				this.userInfo.getUserId(), ip, nutrientCode));
	}

	/**
	 * Logs a user's request to get all rounding rules that have a specific nutrient code.
	 * @param ip The IP address th user is logged in from.
	 * @param nutrientCode The nutrient code.
	 */
	private void logFindRoundingRulesByNutrientCode(String ip, int nutrientCode){
		NutrientsController.logger.info(String.format(NutrientsController.FIND_ROUNDING_RULES_BY_NUTRIENT_CODE_MESSAGE,
				this.userInfo.getUserId(), ip, nutrientCode));
	}

	/**
	 * Logs a user's request to update a nutrient.
	 *
	 * @param ip The IP address the user is logged in from.
	 * @param nutrient The nutrient the user wishes to update.
	 */
	private void logUpdateNutrient(String ip, Nutrient nutrient) {
		NutrientsController.logger.info(String.format(NutrientsController.UPDATE_NUTRIENT_LOG_MESSAE,
				this.userInfo.getUserId(), ip, nutrient.getNutrientCode(), nutrient));
	}
	/**
	 * Gets nutrients by regular expression.
	 *
	 * @param searchString            the search string
	 * @param currentNutrientCodeList the current nutrient code list
	 * @param request                 the request
	 * @return the nutrients by regular expression
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "getNutrientsByRegularExpression")
	public PageableResult<Nutrient> getNutrientsByRegularExpression(@RequestParam("searchString") String searchString,
																	@RequestParam(value = "currentNutrientList", required = false) String currentNutrientCodeList,
																	HttpServletRequest request) {

		List<Long> nutrientCodes = new ArrayList<>();
		if(!StringUtils.isBlank(currentNutrientCodeList)) {
			for(String currentString : currentNutrientCodeList.split("[\\s,]+")){
				nutrientCodes.add(Long.valueOf(currentString));
			}
		}

		return this.nutrientsService.findByRegularExpression(searchString, nutrientCodes);

	}

}
