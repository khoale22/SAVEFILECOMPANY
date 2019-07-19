package com.heb.pm.scaleManagement;

import com.heb.jaf.security.AuthorizedResource;
import com.heb.jaf.security.EditPermission;
import com.heb.jaf.security.ViewPermission;
import com.heb.pm.ApiConstants;
import com.heb.pm.ResourceConstants;
import com.heb.pm.entity.Ingredient;
import com.heb.pm.entity.IngredientStatementHeader;
import com.heb.util.controller.ModifiedEntity;
import com.heb.util.controller.StreamingExportException;
import com.heb.util.controller.UserInfo;
import com.heb.util.jpa.LazyObjectResolver;
import com.heb.util.jpa.PageableResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * REST controller that returns all information related to ingredients.
 *
 * @author m594201
 * @since 2.0.9
 */
@RestController()
@RequestMapping(ApiConstants.BASE_APPLICATION_URL + IngredientController.INGREDIENT_INFO_URL)
@AuthorizedResource(ResourceConstants.SCALE_MANAGEMENT_INGREDIENTS)
public class IngredientController {

    private static final Logger logger = LoggerFactory.getLogger(IngredientController.class);

    protected static final String INGREDIENT_INFO_URL = "/ingredient";

    // Defaults related to paging.
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 100;
    private static final int DEFAULT_REGEX_PAGE_SIZE = 20;

    //log messages
    private static final String FIND_BY_INGREDIENT_CODE_MESSAGE =
            "User %s from IP %s has requested ingredient data for the following ingredient codes [%s]";
    private static final String FIND_BY_INGREDIENT_STATEMENT_CODE_MESSAGE =
            "User %s from IP %s has requested ingredient data for the following ingredient statement codes [%s]";
    private static final String FIND_All_INGREDIENT_MESSAGE =
            "User %s from IP %s has requested all ingredient data";
    private static final String FIND_BY_INGREDIENT_DESCRIPTION_MESSAGE =
            "User %s from IP %s has requested ingredient data containing the wildcard description: %s";
    private static final String FIND_PLUS_BY_INGREDIENT_MESSAGE =
            "User %s from IP %s has requested PLU data for the following ingredient %s";
    private static final String ADD_INGREDIENT_MESSAGE = "User %s from IP %s has requested to add an " +
            "Ingredient with description: '%s'.";
    private static final String DELETE_INGREDIENT_MESSAGE = "User %s from IP %s has requested to delete the " +
            "Ingredient with ingredient code: %s.";
    private static final String UPDATE_INGREDIENT_MESSAGE = "User %s from IP %s has requested to update the " +
            "Ingredient with ingredient code: %s.";
    private static final String INGREDIENT_SEARCH_BY_PATTERN_LOG_MESSAGE =
            "User %s from IP %s searched for ingredients with the pattern '%s'";
    private static final String INGREDIENT_SEARCH_NEXT_INGREDIENT_CODE_LOG_MESSAGE =
            "User %s from IP %s searched for the next ingredient code available at or after %s";
    private static final String SUPER_INGREDIENT_SEARCH_LOG_MESSAGE =
            "User %s from IP %s searched for all ingredients that contain ingredient code %s";
    private static final String EXPORT_SUPER_INGREDIENTS_LOG_MESSAGE =
            "User %s from IP %s requested an ingredient report for ingredient code %s";
    private static final String EXPORT_INGREDIENT_BY_DESCRIPTION__LOG_MESSAGE =
            "User %s from IP %s requested an ingredient report for ingredient description '%s'";
    private static final String EXPORT_ALL_INGREDIENTS_LOG_MESSAGE =
            "User %s from IP %s requested an ingredient report of all ingredients";
    private static final String EXPORT_INGREDIENT_BY_CODE_LOG_MESSAGE =
            "User %s from IP %s requested an ingredient report for ingredient codes '%s";
	private static final String DEFAULT_ADD_SUCCESS_MESSAGE ="Ingredient Code: %s added successfully.";
	private static final String ADD_SUCCESS_MESSAGE_KEY ="IngredientController.addSuccessful";
	private static final String UPDATE_SUCCESS_MESSAGE_KEY ="IngredientController.updateSuccessful";
	private static final String DELETE_INGREDIENT_MESSAGE_KEY ="IngredientController.deleteSuccessful";
	private static final String DELETE_INGREDIENT_SUCCESS_MESSAGE ="Ingredient code: %s deleted successfully.";


    /**
     * The Ingredient service.
     */
    @Autowired
    IngredientService ingredientService;

    @Autowired private
    UserInfo userInfo;

    @Autowired
    private MessageSource messageSource;

    /**
     * The enum Include category.
     */
    public  enum  IncludeCategory {
        /**
         * Include include category.
         */
        INCLUDE,
        /**
         * Exclude include category.
         */
        EXCLUDE,
        /**
         * Empty include category.
         */
        EMPTY
    }

    private LazyObjectResolver<Ingredient> ingredientResolver = new IngredientResolver();

	private LazyObjectResolver<IngredientStatementHeader> ingredientStatementHeaderResolver =
            new IngredientStatementHeaderResolver();

    /**
     * Find by ingredient codes pageable result.
     *
     * @param ingredientCodes the ingredient codes
     * @param categoryCode    the category code
     * @param includeCategory the include category
     * @param includeCounts   the include counts
     * @param page            the page
     * @param pageSize        the page size
     * @param request         the request
     * @return the pageable result
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "ingredientCodes")
    public PageableResult<Ingredient> findByIngredientCodes(@RequestParam String ingredientCodes,
                                                            @RequestParam(value = "categoryCode", required = false) Long categoryCode,
                                                            @RequestParam(value = "includeCategory", required = false) IncludeCategory includeCategory,
                                                            @RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
                                                            @RequestParam(value = "page", required = false) Integer page,
                                                            @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                            HttpServletRequest request){

        List<String> ingredientCodesList = new ArrayList<>();
		Collections.addAll(ingredientCodesList, ingredientCodes.split("[\\s,]+"));
		this.logFindByIngredientCodes(ingredientCodesList, request.getRemoteAddr());

        boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
        int pg = page == null ? IngredientController.DEFAULT_PAGE : page;
        int ps = pageSize == null ? IngredientController.DEFAULT_PAGE_SIZE : pageSize;

        return this.resolveIngredientResults(ingredientService.findIngredientByCode(ingredientCodesList, categoryCode, includeCategory, ic, pg, ps));

    }


    /**
     * Searches for ingredients that contain a requested ingredient code as a sub-ingredient.
     *
     * @param ingredientCode The ingredient code to search for.
     * @param includeCounts Whether or not to include counts in the result.
     * @param page The page of data to search for.
     * @param pageSize The maximum number of records to return.
     * @param request The HTTP request that initiated this call.
     * @return A pageable list of ingredients that contain the requested ingredient as a sub-ingredient.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "superIngredients")
    public PageableResult<Ingredient> findSuperIngredients(@RequestParam(value="ingredientCode") String ingredientCode,
                                                 @RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
                                                 @RequestParam(value = "page", required = false) Integer page,
                                                 @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                 HttpServletRequest request){

        this.logSearchForSuperIngredients(ingredientCode, request.getRemoteAddr());

        boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
        int pg = page == null ? IngredientController.DEFAULT_PAGE : page;
        int ps = pageSize == null ? IngredientController.DEFAULT_PAGE_SIZE : pageSize;

        PageableResult<Ingredient> result = this.ingredientService.findSuperIngredients(ingredientCode, ic, pg, ps);
        result.getData().forEach(this.ingredientResolver::fetch);
        return result;
    }

    /**
     * Logs a user's request to get the ingredients a sub-ingredients part of.
     *
     * @param ingredientCode The ingredient to search for.
     * @param ip The IP address the user logged in from.
     */
    private void logSearchForSuperIngredients(String ingredientCode, String ip) {
        IngredientController.logger.info(String.format(IngredientController.SUPER_INGREDIENT_SEARCH_LOG_MESSAGE,
                this.userInfo.getUserId(), ip, ingredientCode));
    }

    /**
     * Generates a CSV of an ingredient. This includes all other ingredients this ingredient is part of,
     * all the ingredient statements those are tied to, and all the UPCs those statements are tied to.
     *
     * @param ingredientCode The code to search super-ingredients for.
     * @param downloadId An ID to put into a cookie so the front end can identify this download.
     * @param request The HTTP request that initiated the request.
     * @param response The HTTP response that the report will be streamed to.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "exportToCSV", headers = "Accept=text/csv")
    public void exportIngredient(@RequestParam("ingredientCode") String ingredientCode,
                                            @RequestParam(value = "downloadId", required = false) String downloadId,
                                            HttpServletRequest request, HttpServletResponse response) {

        this.logExportIngredientReportRequest(ingredientCode, request.getRemoteAddr());

        if (downloadId != null) {
            Cookie c = new Cookie(downloadId, downloadId);
            c.setPath("/");
            response.addCookie(c);
        }

        try {
            this.ingredientService.streamIngredient(response.getOutputStream(), ingredientCode);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new StreamingExportException(e.getMessage(), e.getCause());
        }
    }


    /**
     * Generates a CSV of a list of ingredients searched for by description.
     *
     * @param description The code to search ingredients for.
     * @param categoryCode the category code
     * @param includeCategory the include category
     * @param downloadId An ID to put into a cookie so the front end can identify this download.
     * @param request The HTTP request that initiated the request.
     * @param response The HTTP response that the report will be streamed to.
     */

    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "exportByDescription", headers = "Accept=text/csv" )
    public void exportIngredientListByDescription(@RequestParam String description,
                                                  @RequestParam(value = "categoryCode", required = false) Long categoryCode,
                                                  @RequestParam(value = "includeCategory", required = false) IncludeCategory includeCategory,
                                                  @RequestParam(value = "downloadId", required = false) String downloadId,
                                                  HttpServletRequest request, HttpServletResponse response){
        this.logExportIngredientByDescriptionReportRequest(description, request.getRemoteAddr());

        if (downloadId != null) {
            Cookie c = new Cookie(downloadId, downloadId);
            c.setPath("/");
            response.addCookie(c);
        }

        try{
            this.ingredientService.streamIngredientsByDescription(response.getOutputStream(), description, categoryCode, includeCategory);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new StreamingExportException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Generates a CSV of a list of ingredients searched for by code.
     *
     * @param ingredientCodes A string that is to be split into a collection of item codes.
     * @param categoryCode the category code
     * @param includeCategory the include category
     * @param downloadId An ID to put into a cookie so the front end can identify this download.
     * @param request The HTTP request that initiated the request.
     * @param response The HTTP response that the report will be streamed to.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "exportByCode", headers = "Accept=text/csv" )
    public void exportIngredientListByCode(@RequestParam(value = "ingredientCode", required = false) String ingredientCodes,
                                                  @RequestParam(value = "categoryCode", required = false) Long categoryCode,
                                                  @RequestParam(value = "includeCategory", required = false) IncludeCategory includeCategory,
                                                  @RequestParam(value = "downloadId", required = false) String downloadId,
                                                  HttpServletRequest request, HttpServletResponse response){
        this.logExportIngredientByCodesReportRequest(ingredientCodes, request.getRemoteAddr());

        List<String> ingredientCodeList = new ArrayList<>();
        Collections.addAll(ingredientCodeList, ingredientCodes.split("[\\s,]+"));

        if (downloadId != null) {
            Cookie c = new Cookie(downloadId, downloadId);
            c.setPath("/");
            response.addCookie(c);
        }

        try{
            this.ingredientService.streamIngredientListByCode(response.getOutputStream(), ingredientCodeList, categoryCode, includeCategory);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new StreamingExportException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Generates a CSV list of all ingredients.
     * @param categoryCode    the category code
     * @param includeCategory the include category
     * @param request         the request
     * @param downloadId An ID to put into a cookie so the front end can identify this download.
     * @param request The HTTP request that initiated the request.
     * @param response The HTTP response that the report will be streamed to.
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "exportAllIngredients", headers = "Accept=text/csv" )
    public void exportAllIngredients(@RequestParam(value = "categoryCode", required = false) Long categoryCode,
                                           @RequestParam(value = "includeCategory", required = false) IncludeCategory includeCategory,
                                           @RequestParam(value = "downloadId", required = false) String downloadId,
                                           HttpServletRequest request, HttpServletResponse response){
        this.logExportAllIngredientsReportRequest(request.getRemoteAddr());

        if (downloadId != null) {
            Cookie c = new Cookie(downloadId, downloadId);
            c.setPath("/");
            response.addCookie(c);
        }

        try{
            this.ingredientService.streamAllIngredients(response.getOutputStream(), categoryCode, includeCategory);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new StreamingExportException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Find by description pageable result.
     *
     * @param description     the description
     * @param categoryCode    the category code
     * @param includeCategory the include category
     * @param includeCounts   the include counts
     * @param page            the page
     * @param pageSize        the page size
     * @param request         the request
     * @return the pageable result
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "ingredientDescription")
    public PageableResult<Ingredient> findByDescription(@RequestParam String description,
                                                        @RequestParam(value = "categoryCode", required = false) Long categoryCode,
                                                        @RequestParam(value = "includeCategory", required = false) IncludeCategory includeCategory,
                                                        @RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
                                                        @RequestParam(value = "page", required = false) Integer page,
                                                        @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                        HttpServletRequest request){
        this.logFindByIngredientDescription(description, request.getRemoteAddr());

        boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
        int pg = page == null ? IngredientController.DEFAULT_PAGE : page;
        int ps = pageSize == null ? IngredientController.DEFAULT_PAGE_SIZE : pageSize;

        return this.resolveIngredientResults(ingredientService.findByDescription(description, categoryCode, includeCategory, ic, pg, ps));
    }

    /**
     * Find by ingredient statement pageable result.
     *
     * @param statementCodes  the statement codes
     * @param categoryCode    the category code
     * @param includeCategory the include category
     * @param includeCounts   the include counts
     * @param page            the page
     * @param pageSize        the page size
     * @param request         the request
     * @return the pageable result
     */
    @ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "ingredientStatement")
    public PageableResult<Ingredient> findByIngredientStatement(@RequestParam List<Long> statementCodes,
                                                                @RequestParam(value = "categoryCode", required = false) Long categoryCode,
                                                                @RequestParam(value = "includeCategory", required = false) IncludeCategory includeCategory,
                                                                @RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
                                                                @RequestParam(value = "page", required = false) Integer page,
                                                                @RequestParam(value = "pageSize", required = false) Integer pageSize,
                                                                HttpServletRequest request){

        this.logFindByIngredientStatement(statementCodes, request.getRemoteAddr());
        boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
        int pg = page == null ? IngredientController.DEFAULT_PAGE : page;
        int ps = pageSize == null ? IngredientController.DEFAULT_PAGE_SIZE : pageSize;

        return this.resolveIngredientResults(ingredientService.findByIngredientStatement(statementCodes, categoryCode, includeCategory, ic, pg, ps));

    }

    /**
     * Add a new ingredient.
     *
     * @param request the request
     * @return the added ingredient
     */
    @EditPermission
    @RequestMapping(method = RequestMethod.POST, value = "addIngredient")
    public ModifiedEntity<Ingredient> addIngredient(@RequestBody Ingredient ingredient, HttpServletRequest request){
        this.logAdd(request.getRemoteAddr(), ingredient.getIngredientDescription());
        Ingredient savedIngredient = this.ingredientService.add(ingredient);
        String updateMessage = this.messageSource.getMessage(IngredientController.ADD_SUCCESS_MESSAGE_KEY,
                new Object[]{savedIngredient.getIngredientCode()}, IngredientController.DEFAULT_ADD_SUCCESS_MESSAGE,
                request.getLocale());
        return new ModifiedEntity<>(this.resolveIngredientResult(savedIngredient), updateMessage);
    }

	/**
	 * Find ingredient by regular expression.
	 *
	 * @param searchString              the search string
	 * @param currentIngredientCode     the current ingredient code
	 * @param currentIngredientCodeList the current ingredient code list
	 * @param page                      the page
	 * @param pageSize                  the page size
	 * @param request                   the request
	 * @return the list
	 */
	@ViewPermission
    @RequestMapping(method = RequestMethod.GET, value = "ingredientRegex")
    public PageableResult<Ingredient> findByRegularExpression(
            @RequestParam("searchString") String searchString,
            @RequestParam(value = "currentIngredientCode", required = false) String currentIngredientCode,
            @RequestParam(value = "currentIngredientCodeList", required = false) String currentIngredientCodeList,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            HttpServletRequest request) {

        List<String> ingredientCodesList = new ArrayList<>();
        if(!StringUtils.isBlank(currentIngredientCodeList)) {
            Collections.addAll(ingredientCodesList, currentIngredientCodeList.split("[\\s,]+"));
        }
        this.logIngredientRegexSearch(request.getRemoteAddr(), searchString);

        // Set defaults if page and page size are not passed in.
        int pg = page == null ? IngredientController.DEFAULT_PAGE : page;
        int ps = pageSize == null ? IngredientController.DEFAULT_REGEX_PAGE_SIZE : pageSize;

        return this.ingredientService.findByRegularExpression(searchString, currentIngredientCode, ingredientCodesList, pg, ps);
    }

    /**
     * Updates ingredient.
     *
     * @param ingredient ingredient to update
     * @param request the request
     * @return the updated ingredient
     */
    @EditPermission
	@RequestMapping(method = RequestMethod.POST, value = "updateIngredient")
	public ModifiedEntity<Ingredient> updateIngredient(@RequestBody Ingredient ingredient, HttpServletRequest request){
		this.logUpdate(request.getRemoteAddr(), ingredient);
		Ingredient savedIngredient = this.ingredientService.update(ingredient);
		String updateMessage = this.messageSource.getMessage(IngredientController.UPDATE_SUCCESS_MESSAGE_KEY,
				new Object[]{savedIngredient.getIngredientCode()}, IngredientController.UPDATE_INGREDIENT_MESSAGE,
				request.getLocale());
		return new ModifiedEntity<>(this.resolveIngredientResult(savedIngredient), updateMessage);
	}

    /**
     * Removes ingredient.
     *
     * @param ingredient ingredient to remove
     * @param request the request
     * @return the ingredient that was deleted
     */
    @EditPermission
	@RequestMapping(method = RequestMethod.POST, value = "deleteIngredient")
	public ModifiedEntity<Ingredient> deleteIngredient(@RequestBody Ingredient ingredient, HttpServletRequest request){
		this.logDelete(request.getRemoteAddr(), ingredient.getIngredientCode());
		this.ingredientService.delete(ingredient);
		String deleteMessage = this.messageSource.getMessage(IngredientController.DELETE_INGREDIENT_MESSAGE_KEY,
				new Object[]{ingredient.getIngredientCode()}, IngredientController.DELETE_INGREDIENT_SUCCESS_MESSAGE,
				request.getLocale());
		return new ModifiedEntity<>(ingredient, deleteMessage);
	}

	/**
	 * Returns a page of all Ingredients.
	 *
     * @param categoryCode    the category code
     * @param includeCategory the include category
	 * @param includeCounts True to return the total number of records and pages. False to just return the data.
	 * @param page The page you are looking for.
	 * @param request the HTTP request that initiated this call.
	 * @return a list of all ScaleActionCodes.
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "allIngredients")
	public PageableResult<Ingredient> findAllIngredients(
			@RequestParam(value = "categoryCode", required = false) Long categoryCode,
			@RequestParam(value = "includeCategory", required = false) IncludeCategory includeCategory,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
			@RequestParam(value = "includeCounts", required = false) Boolean includeCounts,
			@RequestParam(value = "page", required = false) Integer page, HttpServletRequest request){


		this.logFindAllIngredients(request.getRemoteAddr());
		boolean ic = includeCounts == null ? Boolean.FALSE : includeCounts;
		int pg = page == null ? IngredientController.DEFAULT_PAGE : page;
		int ps = pageSize == null ?  IngredientController.DEFAULT_PAGE_SIZE : pageSize;

		return resolveIngredientResults(this.ingredientService.findAll(categoryCode, includeCategory, ic, pg, ps));
	}

	/**
	 * Find ingredient by regular expression.
	 *
	 * @param request the request
	 * @return the list
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "nextIngredientCode")
	public List<String> findByNextIngredientCode(
			@RequestParam("searchString") Long searchString,
			HttpServletRequest request) {

		this.logIngredientCodeSearch(request.getRemoteAddr(), searchString);

		return this.ingredientService.findByNextIngredientCode(searchString, DEFAULT_PAGE, DEFAULT_PAGE_SIZE);
	}
	
	/**
	 * Find nutrients by nutrient statement list.
	 *
	 * @param ingredientStatement the nutrient statement
	 * @param request           the request
	 * @return the list
	 */
	@ViewPermission
	@RequestMapping(method = RequestMethod.GET, value = "ingredientByIngredientStatement")
	public IngredientStatementHeader findIngredientByIngredientStatementNumber(
			@RequestParam("ingredientsStatement") long ingredientStatement, HttpServletRequest request){
		IngredientStatementHeader header =  this.ingredientService.findIngredientByIngredientStatementNumber(ingredientStatement);
		this.ingredientStatementHeaderResolver.fetch(header);
		return header;
	}

	/**
     * Resolve the ingredient results
     * @param results ingredient results
     * @return a pageable result
     */
    private PageableResult<Ingredient> resolveIngredientResults(PageableResult<Ingredient> results) {
        results.getData().forEach(this.ingredientResolver::fetch);
        return results;
    }

    /**
     * Resolve the ingredient results
     * @param result ingredient results
     * @return a pageable result
     */
    private Ingredient resolveIngredientResult(Ingredient result) {
        this.ingredientResolver.fetch(result);
        return result;
    }

    /**
     * Logs a user's request to get all ingredient records.
     *
     * @param ip The IP address th user is logged in from.
     */
    private void logFindAllIngredients(String ip) {
        IngredientController.logger.info(
                String.format(IngredientController.FIND_All_INGREDIENT_MESSAGE,
                        this.userInfo.getUserId(), ip));
    }

    /**
     * Logs a user's request to get all ingredient records by ingredientCodes.
     *
	 * @param ingredientCodes the ingredient codes to look for
	 * @param ip The IP address th user is logged in from.
	 */
    private void logFindByIngredientCodes(List<String> ingredientCodes, String ip) {
        IngredientController.logger.info(
                String.format(IngredientController.FIND_BY_INGREDIENT_CODE_MESSAGE,
                        this.userInfo.getUserId(), ip, ingredientCodes));
    }

    /**
     * Logs a user's request to get all ingredient records by description.
     *
	 * @param description the ingredient description to look for
	 * @param ip The IP address th user is logged in from.
	 */
    private void logFindByIngredientDescription(String description, String ip) {
        IngredientController.logger.info(
                String.format(IngredientController.FIND_BY_INGREDIENT_DESCRIPTION_MESSAGE,
                        this.userInfo.getUserId(), ip, description));
    }

    /**
     * Logs a user's request to get all ingredient records by ingredientStatement.
     *
     * @param ip The IP address th user is logged in from.
     */
    private void logFindByIngredientStatement(List<Long> statementCodes, String ip) {
        IngredientController.logger.info(
                String.format(IngredientController.FIND_BY_INGREDIENT_STATEMENT_CODE_MESSAGE,
                        this.userInfo.getUserId(), ip, statementCodes));
    }

    /**
     * Logs a user's request to update an ingredient.
     *
     * @param ip The IP address the user is logged in from.
     * @param ingredient The ingredient to be updated.
     */
    private void logUpdate(String ip, Ingredient ingredient){
        IngredientController.logger.info(String.format(IngredientController.UPDATE_INGREDIENT_MESSAGE,
                this.userInfo.getUserId(), ip, ingredient.getIngredientCode()));
    }

    /**
     * Logs a user's request to add an ingredient.
     *
     * @param ip The IP address the user is logged in from.
     * @param description The ingredient description of the ingredient to be added.
     */
    private void logAdd(String ip, String description){
        IngredientController.logger.info(String.format(IngredientController.ADD_INGREDIENT_MESSAGE,
                this.userInfo.getUserId(), ip, description));
    }

    /**
     * Logs a user's request to delete an ingredient.
     *  @param ip The IP address the user is logged in from.
     * @param ingredientCode The ingredient code to be deleted.
	 */
    private void logDelete(String ip, String ingredientCode){
        IngredientController.logger.info(String.format(IngredientController.DELETE_INGREDIENT_MESSAGE,
                this.userInfo.getUserId(), ip, ingredientCode));
    }

    /**
     * Logs the user's search for an ingredient by regex.
     *
     * @param ip The IP address the user is logged in from.
     * @param searchString The search string the user is looking for ingredients that match.
     */
    private void logIngredientRegexSearch(String ip, String searchString) {
        IngredientController.logger.info(String.format(IngredientController.INGREDIENT_SEARCH_BY_PATTERN_LOG_MESSAGE,
                this.userInfo.getUserId(), ip, searchString));
    }

	/**
	 * Logs the user's search for the next available ingredient code.
	 *  @param ip The IP address the user is logged in from.
	 * @param searchString The search string the user typed in.
	 */
	private void logIngredientCodeSearch(String ip, Long searchString) {
		IngredientController.logger.info(String.format(
				IngredientController.INGREDIENT_SEARCH_NEXT_INGREDIENT_CODE_LOG_MESSAGE,
				this.userInfo.getUserId(), ip, searchString));
	}

    /**
     * Logs a user's request for an export of super ingredients.
     *
     * @param ingredientCode The code they are looking for.
     * @param ip The IP address the request came from.
     */
    private void logExportIngredientReportRequest(String ingredientCode, String ip) {
        IngredientController.logger.info(String.format(IngredientController.EXPORT_SUPER_INGREDIENTS_LOG_MESSAGE,
                this.userInfo.getUserId(), ip, ingredientCode));
    }

    /**
     * Logs a user's request for an export of ingredients by Description.
     *
     * @param ingredientDescription The ingredient description they are looking for.
     * @param ip The IP address the request came from.
     */
    private void logExportIngredientByDescriptionReportRequest(String ingredientDescription, String ip) {
        IngredientController.logger.debug(String.format(IngredientController.EXPORT_INGREDIENT_BY_DESCRIPTION__LOG_MESSAGE,
                this.userInfo.getUserId(), ip, ingredientDescription));
    }

    /**
     * Logs a user's request for an export of ingredients by ingredient code.
     *
     * @param ingredientCodes The code they are looking for.
     * @param ip The IP address the request came from.
     */
    private void logExportIngredientByCodesReportRequest(String ingredientCodes, String ip) {
        IngredientController.logger.debug(String.format(IngredientController.EXPORT_INGREDIENT_BY_CODE_LOG_MESSAGE,
                this.userInfo.getUserId(), ip, ingredientCodes));
    }

    /**
     * Logs a user's request for an export of all ingredients.
     * @param ip The IP address the request came from.
     */
    private void logExportAllIngredientsReportRequest(String ip){
        IngredientController.logger.debug(String.format(IngredientController.EXPORT_ALL_INGREDIENTS_LOG_MESSAGE,
                this.userInfo.getUserId(), ip));
    }
}